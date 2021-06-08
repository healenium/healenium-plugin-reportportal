package com.epam.reportportal.extension.healenium;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class HealeniumPlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public HealeniumPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}
