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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Observable<T> {

    public Observable() {
        this(null);
    }

    public Observable(T initialValue) {
        mCurrentValue.set(initialValue);
    }

    public void update(T value) {
        mCurrentValue.set(value);
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
        return mCurrentValue.get();
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

    private final AtomicReference<T> mCurrentValue = new AtomicReference<>();
    private final Collection<Consumer<T>> mStrongListeners = new CopyOnWriteArrayList<>();
    private final Collection<WeakReference<Consumer<T>>> mWeakListeners = new CopyOnWriteArrayList<>();
}
