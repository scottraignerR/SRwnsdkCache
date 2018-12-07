/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk.util;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandru.ghitulescu on 27/12/2017.
 * <p>
 * Utilities class that contains methods for getting the current storage path
 */

public class StorageUtils {
    private static final String TAG = "StorageUtils";

    private static final long SIZE_KB = 1024L;
    private static final long SIZE_MB = SIZE_KB * SIZE_KB;

    @SuppressWarnings("WeakerAccess")
    public static final int INTERNAL_STORAGE = 0;
    @SuppressWarnings("WeakerAccess")
    public static final int SD_STORAGE = 1;

    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpace() {
        long availableSpace;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        return availableSpace;
    }

    /**
     * @return Number of kilo bytes available on External storage
     */
    public static long getAvailableSpaceKB() {
        return getAvailableSpace() / SIZE_KB;
    }

    /**
     * @return Number of Mega bytes available on External storage
     */
    public static long getAvailableSpaceMB() {
        return getAvailableSpace() / SIZE_MB;
    }

    /**
     * {@link StorageUtils#INTERNAL_STORAGE} for internal storage
     * {@link StorageUtils#SD_STORAGE} for sd card
     *
     * @return the index of the storage location
     */
    public static int getDefaultStorage() {
        if (Environment.isExternalStorageRemovable()) {
            return SD_STORAGE;
        } else {
            return INTERNAL_STORAGE;
        }
    }

    /**
     * Uses internal {@link Environment}
     *
     * @return a list of files pointing to mount locations or null if there was an error
     * @deprecated Use {@link Environment#getExternalStorageDirectory()} directly
     */
    @Deprecated
    public static List<File> getExternalStorageDirectories() {
        try {
            Field sCurrentUserField = Environment.class.getDeclaredField("sCurrentUser");
            sCurrentUserField.setAccessible(true);

            Object sCurrentUser = sCurrentUserField.get(null);
            Method getExternalDirs = sCurrentUser.getClass().getDeclaredMethod("getExternalDirs");
            File[] paths = (File[]) getExternalDirs.invoke(sCurrentUser);
            ArrayList<File> usablePaths = new ArrayList<>();
            for (File file : paths) {
                if (file.getTotalSpace() > 0) {
                    usablePaths.add(file);
                }
            }
            return usablePaths;
        } catch (Exception e) {
            Log.w(TAG, "", e);
            return null;
        }
    }

    /**
     * Uses internal {@link Environment}
     *
     * @param type see {@link Environment#getExternalStoragePublicDirectory(String)}
     * @return a list of files pointing to mount locations for given type or null if there was an error
     * @deprecated Use {@link Environment#getExternalStoragePublicDirectory(String)} directly
     */
    @Deprecated
    public static List<File> getExternalStoragePublicDirectories(String type) {
        try {
            Field sCurrentUserField = Environment.class.getDeclaredField("sCurrentUser");
            sCurrentUserField.setAccessible(true);

            Object sCurrentUser = sCurrentUserField.get(null);
            Method buildExternalStoragePublicDirs = sCurrentUser.getClass().getDeclaredMethod("buildExternalStoragePublicDirs", String.class);

            File[] paths = (File[]) buildExternalStoragePublicDirs.invoke(sCurrentUser, type);
            ArrayList<File> usablePaths = new ArrayList<>();
            for (File file : paths) {
                if (file.getParentFile().getTotalSpace() > 0) {
                    usablePaths.add(file);
                }
            }
            return usablePaths;
        } catch (Exception e) {
            Log.w(TAG, "", e);
            return null;
        }
    }

    public static boolean hasSDCard() {
        List<File> paths = getExternalStorageDirectories();
        return paths != null && paths.size() > 1;
    }
}
