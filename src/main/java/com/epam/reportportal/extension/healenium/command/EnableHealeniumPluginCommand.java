package com.epam.reportportal.extension.healenium.command;

import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import com.epam.ta.reportportal.entity.integration.Integration;

import java.util.Map;

import static com.epam.reportportal.extension.healenium.utils.Constants.ENABLE_HEALENIUM_PLUGIN;

public class EnableHealeniumPluginCommand implements NamedPluginCommand<Boolean> {

    private final HealeniumDao healeniumDao;

    public EnableHealeniumPluginCommand(HealeniumDao healeniumDao) {
        this.healeniumDao = healeniumDao;
    }

    @Override
    public Boolean executeCommand(Integration integration, Map<String, Object> params) {
        healeniumDao.updateStatus((Boolean) params.get("state"));
        return true;
    }

    @Override
    public String getName() {
        return ENABLE_HEALENIUM_PLUGIN;
    }
}
