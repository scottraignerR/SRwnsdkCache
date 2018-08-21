package com.core.realwear.sdk.util;

import java.util.concurrent.atomic.AtomicReference;

public final class Observable<T> extends Publisher<T> {

    public Observable() {
        this(null);
    }

    public Observable(T initialValue) {
         mCurrentValue = new AtomicReference<>(initialValue);
    }

    public void setValue(T newValue) {
        mCurrentValue.set(newValue);
        notify(newValue);
    }

    public T getValue() {
        return mCurrentValue.get();
    }

    private final AtomicReference<T> mCurrentValue;
}
