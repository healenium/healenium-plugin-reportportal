package com.epam.reportportal.extension.healenium.event;

import com.epam.reportportal.extension.healenium.event.handler.EventHandler;


public interface EventHandlerFactory<T> {

    EventHandler<T> getEventHandler(String key);
}
