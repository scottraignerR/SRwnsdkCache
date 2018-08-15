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

import android.support.annotation.NonNull;

import com.core.realwear.sdk.util.replacements.Consumer;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class Observable<T> {

    Observable(ObservableWriteView<T> parent) {
        mParent = parent;
    }

    void notify(T value) {
        for (Consumer<T> listener : mStrongListeners) {
            listener.accept(value);
        }
        for (WeakReference<Consumer<T>> listenerRef : mWeakListeners) {
            Consumer<T> listener = listenerRef.get();
            if (listener != null) {
                listener.accept(value);
            }
        }
    }

    public T getCurrentValue() {
        return mParent.get();
    }

    public void addListener(Consumer<T> newListener) {
        mStrongListeners.add(newListener);
    }

    public void removeListener(@NonNull Consumer<T> listener) {
        mStrongListeners.remove(listener);
        for (WeakReference<Consumer<T>> ref : mWeakListeners) {
            if (listener.equals(ref)) {
                mWeakListeners.remove(ref);
            }
        }
    }

    public void addWeakReferenceListener(Consumer<T> newListener) {
        mWeakListeners.add(new WeakReference<>(newListener));
    }

    private final ObservableWriteView<T> mParent;
    private final Collection<Consumer<T>> mStrongListeners = new CopyOnWriteArrayList<>();
    private final Collection<WeakReference<Consumer<T>>> mWeakListeners = new CopyOnWriteArrayList<>();
}