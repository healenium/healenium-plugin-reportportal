package com.epam.reportportal.extension.healenium.command;

import com.epam.reportportal.extension.PluginCommand;


public interface NamedPluginCommand<T> extends PluginCommand<T> {

    String getName();

}
