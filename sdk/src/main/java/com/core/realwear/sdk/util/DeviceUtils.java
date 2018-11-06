package com.core.realwear.sdk.util;

import android.os.Build;

public class DeviceUtils {
    public static boolean isHoneywellDevice() {
        return Build.DISPLAY.endsWith("HW");
    }

    public static String getBrandingFolder() {
        String brandFolder;
        if (Build.DISPLAY.length() >= 2) {
            brandFolder = Build.DISPLAY.substring(Build.DISPLAY.length() - 2).toLowerCase();
        } else {
            brandFolder = "rw";
        }

        return "/etc/wearhf/branding/" + brandFolder + "/";
    }
}
