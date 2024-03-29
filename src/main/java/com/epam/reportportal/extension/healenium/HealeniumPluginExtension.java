package com.epam.reportportal.extension.healenium;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.epam.reportportal.extension.classloader.DelegatingClassLoader;
import com.epam.reportportal.extension.common.IntegrationTypeProperties;
import com.epam.reportportal.extension.event.*;
import com.epam.reportportal.extension.healenium.command.EnableHealeniumPluginCommand;
import com.epam.reportportal.extension.healenium.command.GetFileCommand;
import com.epam.reportportal.extension.healenium.command.GetHealeniumPluginStatusCommand;
import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import com.epam.reportportal.extension.healenium.dao.impl.HealeniumDaoImpl;
import com.epam.reportportal.extension.healenium.event.launch.HealeniumLaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.healenium.event.plugin.PluginEventHandlerFactory;
import com.epam.reportportal.extension.healenium.event.plugin.PluginEventListener;
import com.epam.reportportal.extension.healenium.utils.MemoizingSupplier;
import com.epam.ta.reportportal.dao.*;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.epam.reportportal.extension.healenium.utils.Constants.HEALENIUM;

@Extension
@Component
public class HealeniumPluginExtension implements ReportPortalExtensionPoint, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealeniumPluginExtension.class);

    public static final String BINARY_DATA_PROPERTIES_FILE_ID = "healenium-binary-data.properties";

    public static final String SCHEMA_SCRIPTS_DIR = "schema";

    private final String resourcesDir;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DelegatingClassLoader delegatingClassLoader;

    @Autowired
    private IntegrationTypeRepository integrationTypeRepository;

    @Autowired
    private IntegrationRepository integrationRepository;

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private TestItemRepository testItemRepository;

    @Autowired
    private ItemAttributeRepository itemAttributeRepository;

    private final Supplier<HealeniumDao> healeniumDaoSupplier;
    private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping = new MemoizingSupplier<>(this::getCommands);
    private final Supplier<ApplicationListener<PluginEvent>> pluginLoadedListener;
    private final Supplier<ApplicationListener<LaunchAnalysisFinishEvent>> launchAutoAnalysisFinishEventListenerSupplier;

    public HealeniumPluginExtension(Map<String, Object> initParams) {
        resourcesDir = IntegrationTypeProperties.RESOURCES_DIRECTORY.getValue(initParams)
                .map(String::valueOf)
                .orElse("");
        pluginLoadedListener = new MemoizingSupplier<>(() -> new PluginEventListener(HEALENIUM,
                new PluginEventHandlerFactory(resourcesDir,
                        integrationTypeRepository,
                        integrationRepository
                )
        ));
        healeniumDaoSupplier = new MemoizingSupplier<>(() -> new HealeniumDaoImpl(jdbcTemplate));
        launchAutoAnalysisFinishEventListenerSupplier = new MemoizingSupplier<>(() ->
                new HealeniumLaunchAutoAnalysisFinishEvent(launchRepository, testItemRepository,
                        itemAttributeRepository, healeniumDaoSupplier.get()));
    }

    @Override
    public Map<String, ?> getPluginParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
        return params;
    }

    @Override
    public CommonPluginCommand getCommonCommand(String commandName) {
        return null;
    }

    @Override
    public PluginCommand getIntegrationCommand(String commandName) {
        return pluginCommandMapping.get().get(commandName);
    }

    @PostConstruct
    public void createIntegration() throws IOException {
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class
        );
        applicationEventMulticaster.addApplicationListener(pluginLoadedListener.get());
        applicationEventMulticaster.addApplicationListener(launchAutoAnalysisFinishEventListenerSupplier.get());
        delegatingClassLoader.addLoader(HEALENIUM, getClass().getClassLoader());
        initBillingSchema();
    }

    private void initBillingSchema() throws IOException {
        try (Stream<Path> paths = Files.list(Paths.get(resourcesDir, SCHEMA_SCRIPTS_DIR))) {
            FileSystemResource[] scriptResources = paths.sorted().map(FileSystemResource::new).toArray(FileSystemResource[]::new);
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(scriptResources);
            resourceDatabasePopulator.execute(dataSource);
        }
    }

    private Map<String, PluginCommand<?>> getCommands() {
        HashMap<String, PluginCommand<?>> pluginCommandMapping = new HashMap<>();
        GetFileCommand getFileCommand = new GetFileCommand(resourcesDir);
        EnableHealeniumPluginCommand enableHealeniumPluginCommand = new EnableHealeniumPluginCommand(healeniumDaoSupplier.get());
        GetHealeniumPluginStatusCommand getHealeniumPluginStatusCommand = new GetHealeniumPluginStatusCommand(healeniumDaoSupplier.get());

        pluginCommandMapping.put(getFileCommand.getName(), getFileCommand);
        pluginCommandMapping.put(enableHealeniumPluginCommand.getName(), enableHealeniumPluginCommand);
        pluginCommandMapping.put(getHealeniumPluginStatusCommand.getName(), getHealeniumPluginStatusCommand);
        return pluginCommandMapping;
    }

    @Override
    public void destroy() {
        removeListeners();
    }

    private void removeListeners() {
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class
        );
        applicationEventMulticaster.removeApplicationListener(pluginLoadedListener.get());
    }
}
