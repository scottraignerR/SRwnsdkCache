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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class PublicObservableTests {

    @Test
    public void currentValueUpdatesOk() {
        assertThat(objectUnderTest.getValue(), equalTo(INITIAL_VALUE));
        final int value = randomInt();
        objectUnderTest.setValue(value);
        assertThat(objectUnderTest.getValue(), equalTo(value));
    }

    @Test
    public void strongListenerWorks() {
        final int value = randomInt();
        objectUnderTest.addListener(flag::set);
        objectUnderTest.setValue(value);
        assertThat(flag.get(), equalTo(value));
    }

    @Test
    public void multipleStrongListenersWork() {
        final int value = randomInt();
        final AtomicInteger flagA = new AtomicInteger(INITIAL_VALUE);
        final AtomicInteger flagB = new AtomicInteger(INITIAL_VALUE);
        objectUnderTest.addListener(flag::set);
        objectUnderTest.addListener(flagA::set);
        objectUnderTest.addListener(flagB::set);
        objectUnderTest.setValue(value);
        assertThat(flag.get(), equalTo(value));
        assertThat(flagA.get(), equalTo(value));
        assertThat(flagB.get(), equalTo(value));
    }

    @Test
    public void weakListenerWorks() {
        final int value = randomInt();
        final Consumer<Integer> strongRef = flag::set;
        objectUnderTest.addWeakReferenceListener(strongRef);
        System.gc(); // NB: This might not call the garbage collector so the test can not be guaranteed reliable.
        objectUnderTest.setValue(value);
        assertThat(flag.get(), equalTo(value));
    }

    @Test
    public void weakListenerCleansUpWorks() {
        final int value = randomInt();
        objectUnderTest.addWeakReferenceListener(flag::set);
        System.gc(); // NB: This might not call the garbage collector so the test can not be guaranteed reliable.
        objectUnderTest.setValue(value);
        assertThat(flag.get(), equalTo(INITIAL_VALUE));
    }

    @Test
    public void updateWithSameDoesNotNotify() {
        final int value = randomInt();
        objectUnderTest.setValue(value);
        Consumer<Integer> testListener = mock(Consumer.class);
        objectUnderTest.addListener(testListener);
        objectUnderTest.setValue(value);
        verify(testListener, never()).accept(any());
    }

    private int randomInt() {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    private static final int INITIAL_VALUE = -1;
    private final Observable<Integer> objectUnderTest = new Observable<>(INITIAL_VALUE);
    private final AtomicInteger flag = new AtomicInteger(INITIAL_VALUE);
}

