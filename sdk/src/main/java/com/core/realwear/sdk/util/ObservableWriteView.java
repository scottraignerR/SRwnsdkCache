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

import java.util.concurrent.atomic.AtomicReference;

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
        mCurrentValue.set(newValue);
        mObservableReadValue.notify(newValue);
    }

    public T get() {
        return mCurrentValue.get();
    }

    private final AtomicReference<T> mCurrentValue;
    private final ObservableReadValue<T> mObservableReadValue = new ObservableReadValue<>(this);
}
