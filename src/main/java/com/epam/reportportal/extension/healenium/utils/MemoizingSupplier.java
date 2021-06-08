package com.epam.reportportal.extension.healenium.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;


public class MemoizingSupplier<T> implements Supplier<T> {

    private final Supplier<T> delegate;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private T value;

    public MemoizingSupplier(Supplier<T> delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public synchronized T get() {
        if (!initialized.get()) {
            if (!initialized.get()) {
                T t = delegate.get();
                value = t;
                initialized.set(true);
                return t;
            }
        }
        return value;
    }

}
