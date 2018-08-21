package com.core.realwear.sdk.util;

import android.support.annotation.NonNull;

import com.core.realwear.sdk.util.replacements.Consumer;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Publisher<T> {

    protected void notify(T value) {
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

    private final Collection<Consumer<T>> mStrongListeners = new CopyOnWriteArrayList<>();
    private final Collection<WeakReference<Consumer<T>>> mWeakListeners = new CopyOnWriteArrayList<>();
}
