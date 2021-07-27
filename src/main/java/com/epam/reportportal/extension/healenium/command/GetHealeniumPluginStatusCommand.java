package com.epam.reportportal.extension.healenium.command;

import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import com.epam.ta.reportportal.entity.integration.Integration;

import java.util.Map;

import static com.epam.reportportal.extension.healenium.utils.Constants.GET_HEALENIUM_PLUGIN_STATUS;

public class GetHealeniumPluginStatusCommand implements NamedPluginCommand<Boolean> {

    private final HealeniumDao healeniumDao;

    public GetHealeniumPluginStatusCommand(HealeniumDao healeniumDao) {
        this.healeniumDao = healeniumDao;
    }

    @Override
    public String getName() {
        return GET_HEALENIUM_PLUGIN_STATUS;
    }

    @Override
    public Boolean executeCommand(Integration integration, Map<String, Object> params) {
        return healeniumDao.isActive();
    }
}
