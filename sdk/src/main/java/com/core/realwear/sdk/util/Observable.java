package com.core.realwear.sdk.util;

import com.core.realwear.sdk.util.replacements.UnaryOperator;

import java.util.Objects;

public final class Observable<T> extends Publisher<T> {

    public Observable() {
        this(null);
    }

    public Observable(T initialValue) {
        mCurrentValue = initialValue;
    }

    public synchronized void setValue(T newValue) {
        T previous = mCurrentValue;
        mCurrentValue = newValue;

        if (!Objects.equals(newValue, previous)) {
            notify(newValue);
        }
    }

    public synchronized T getValue() {
        return mCurrentValue;
    }

    public synchronized T modify(UnaryOperator<T> modifyOperation) {
        T next = modifyOperation.apply(mCurrentValue);
        setValue(next);
        return next;
    }

    private T mCurrentValue;
}
