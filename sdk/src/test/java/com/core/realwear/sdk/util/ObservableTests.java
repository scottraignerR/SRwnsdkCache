/*
 * RealWear Development Software, Source Code and Object Code.
 * Copyright (C) 2015, 2016, 2017 RealWear, Inc. - All rights reserved
 *
 * Contact info@realwear.com for further information about the use of this code.
 *
 * Filename: PublisherTests.java
 * Class: PublisherTests
 * Author: douglas.mearns
 *
 */

package com.core.realwear.sdk.util;

import com.core.realwear.sdk.util.replacements.Consumer;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ObservableTests {

    @Test
    public void currentValueUpdatesOk() {
        assertThat(objectUnderTestView.getCurrentValue(), equalTo(INITIAL_VALUE));
        final int value = randomInt();
        objectUnderTest.update(value);
        assertThat(objectUnderTestView.getCurrentValue(), equalTo(value));
    }

    @Test
    public void strongListenerWorks() {
        final int value = randomInt();
        objectUnderTestView.addListener(flag::set);
        objectUnderTest.update(value);
        assertThat(flag.get(), equalTo(value));
    }

    @Test
    public void multipleStrongListenersWork() {
        final int value = randomInt();
        final AtomicInteger flagA = new AtomicInteger(INITIAL_VALUE);
        final AtomicInteger flagB = new AtomicInteger(INITIAL_VALUE);
        objectUnderTestView.addListener(flag::set);
        objectUnderTestView.addListener(flagA::set);
        objectUnderTestView.addListener(flagB::set);
        objectUnderTest.update(value);
        assertThat(flag.get(), equalTo(value));
        assertThat(flagA.get(), equalTo(value));
        assertThat(flagB.get(), equalTo(value));
    }

    @Test
    public void weakListenerWorks() {
        final int value = randomInt();
        final Consumer<Integer> strongRef = flag::set;
        objectUnderTestView.addWeakReferenceListener(strongRef);
        System.gc(); // NB: This might not call the garbage collector so the test can not be guaranteed reliable.
        objectUnderTest.update(value);
        assertThat(flag.get(), equalTo(value));
    }

    @Test
    public void weakListenerCleansUpWorks() {
        final int value = randomInt();
        objectUnderTestView.addWeakReferenceListener(flag::set);
        System.gc(); // NB: This might not call the garbage collector so the test can not be guaranteed reliable.
        objectUnderTest.update(value);
        assertThat(flag.get(), equalTo(INITIAL_VALUE));
    }


    private int randomInt() {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    private static final int INITIAL_VALUE = -1;
    private final ObservableWriteView<Integer> objectUnderTest = new ObservableWriteView<>(INITIAL_VALUE);
    private final Observable<Integer> objectUnderTestView = objectUnderTest.getObservable();
    private final AtomicInteger flag = new AtomicInteger(INITIAL_VALUE);
}

