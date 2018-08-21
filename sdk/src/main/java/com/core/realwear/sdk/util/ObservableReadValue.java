/*
 * RealWear Development Software, Source Code and Object Code.
 * Copyright (C) 2015, 2016, 2017 RealWear, Inc. - All rights reserved
 *
 * Contact info@realwear.com for further information about the use of this code.
 *
 * Filename: Publisher.java
 * Class: Publisher
 * Author: douglas.mearns
 *
 */

package com.core.realwear.sdk.util;

public class ObservableReadValue<T> extends Publisher<T> {

    ObservableReadValue(ObservableWriteView<T> parent) {
        mParent = parent;
    }

    public T getCurrentValue() {
        return mParent.get();
    }

    private final ObservableWriteView<T> mParent;
}