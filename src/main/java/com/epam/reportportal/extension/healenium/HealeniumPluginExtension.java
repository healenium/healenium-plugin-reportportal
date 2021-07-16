package com.epam.reportportal.extension.healenium;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.epam.reportportal.extension.classloader.DelegatingClassLoader;
import com.epam.reportportal.extension.common.IntegrationTypeProperties;
import com.epam.reportportal.extension.event.LaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.healenium.command.GetFileCommand;
import com.epam.reportportal.extension.healenium.event.launch.HealeniumLaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.healenium.event.plugin.PluginEventHandlerFactory;
import com.epam.reportportal.extension.healenium.event.plugin.PluginEventListener;
import com.epam.reportportal.extension.healenium.utils.MemoizingSupplier;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.dao.ItemAttributeRepository;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.TestItemRepository;
import org.pf4j.Extension;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.epam.reportportal.extension.healenium.utils.Constants.HEALENIUM;

@Extension
@Component
public class HealeniumPluginExtension implements ReportPortalExtensionPoint, DisposableBean {

    public static final String BINARY_DATA_PROPERTIES_FILE_ID = "healenium-binary-data.properties";

    private final Supplier<Map<String, PluginCommand>> pluginCommandMapping = new MemoizingSupplier<>(this::getCommands);

    private final String resourcesDir;

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

    private final Supplier<ApplicationListener<PluginEvent>> pluginLoadedListener;
    private final Supplier<ApplicationListener<LaunchAutoAnalysisFinishEvent>> launchAutoAnalysisFinishEventListenerSupplier;

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

        launchAutoAnalysisFinishEventListenerSupplier = new MemoizingSupplier<>(() ->
                new HealeniumLaunchAutoAnalysisFinishEvent(launchRepository, testItemRepository, itemAttributeRepository));
    }

    @Override
    public Map<String, ?> getPluginParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
        return params;
    }

    @Override
    public PluginCommand getCommandToExecute(String commandName) {
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
    }

    @Override
    public void destroy() {
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class
        );
        applicationEventMulticaster.removeApplicationListener(pluginLoadedListener.get());
        applicationEventMulticaster.removeApplicationListener(launchAutoAnalysisFinishEventListenerSupplier.get());
        delegatingClassLoader.removeLoader(HEALENIUM);
    }

    private Map<String, PluginCommand> getCommands() {
        HashMap<String, PluginCommand> pluginCommandMapping = new HashMap<>();
        GetFileCommand getFileCommand = new GetFileCommand(resourcesDir);

        pluginCommandMapping.put(getFileCommand.getName(), getFileCommand);
        return pluginCommandMapping;
    }
}
