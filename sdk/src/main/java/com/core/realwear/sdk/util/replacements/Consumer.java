/*
 * RealWear Development Software, Source Code and Object Code.
 * Copyright (C) 2015, 2016, 2017 RealWear, Inc. - All rights reserved
 *
 * Contact info@realwear.com for further information about the use of this code.
 *
 * Filename: Consumer.java
 * Class: Consumer
 * Author: douglas.mearns
 *
 * Replaces the consumer interface in later versions of android as we can't access them in this API
 * level.
 */

package com.core.realwear.sdk.util.replacements;

public interface Consumer<T> {
    void accept(T t);
}
