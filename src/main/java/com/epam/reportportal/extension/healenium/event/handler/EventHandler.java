package com.epam.reportportal.extension.healenium.event.handler;

public interface EventHandler<T> {

    void handle(T event);
}
