package com.core.realwear.sdk.util.replacements;

@FunctionalInterface
public interface UnaryOperator<T> {
    T apply(T value);
}
