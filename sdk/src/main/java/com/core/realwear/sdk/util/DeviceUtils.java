package com.core.realwear.sdk.util;

import android.os.Build;

public class DeviceUtils {
    public static boolean isHoneywellDevice() {
        return Build.DISPLAY.endsWith("HW");
    }
}
