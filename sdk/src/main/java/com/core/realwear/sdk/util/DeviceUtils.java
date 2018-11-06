package com.core.realwear.sdk.util;

import android.os.Build;

import java.io.File;

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

        String folder = "/etc/wearhf/branding/" + brandFolder + "/";
        File file = new File(folder);
        if (file.exists()) {
            return folder;
        } else {
            // default to realwear
            return "/etc/wearhf/branding/rw/";
        }
    }
}
