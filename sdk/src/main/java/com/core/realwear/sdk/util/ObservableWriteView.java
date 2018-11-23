/*
 * RealWear Development Software, Source Code and Object Code.
 * Copyright (C) 2015, 2016, 2017 RealWear, Inc. - All rights reserved
 *
 * Contact info@realwear.com for further information about the use of this code.
 *
 * Filename: ObservableWriteView.java
 * Class: ObservableWriteView
 * Author: douglas.mearns
 *
 */

package com.core.realwear.sdk.util;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @param <T>
 * @Deprecated Use the Realwear styles library version of this class instead
 */
@Deprecated
public class ObservableWriteView<T> {
    public ObservableWriteView() {
        this(null);
    }

    public ObservableWriteView(T initialValue) {
        mCurrentValue = new AtomicReference<>(initialValue);
    }

    public ObservableReadValue<T> getObservableReadValue() {
        return mObservableReadValue;
    }

    public void update(T newValue) {
        final T oldValue = mCurrentValue.getAndSet(newValue);
        if (!Objects.equals(oldValue, newValue)) {
            mObservableReadValue.notify(newValue);
        }
    }

    public T get() {
        return mCurrentValue.get();
    }

    private final AtomicReference<T> mCurrentValue;
    private final ObservableReadValue<T> mObservableReadValue = new ObservableReadValue<>(this);
}
