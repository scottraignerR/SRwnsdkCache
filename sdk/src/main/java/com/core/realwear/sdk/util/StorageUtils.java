/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by alexandru.ghitulescu on 27/12/2017.
 * <p>
 * Utilities class that contains methods for getting the current storage path
 */

public final class StorageUtils {
    private static final String TAG = "StorageUtils";
    private static final String KEY_DEFAULT_STORAGE = "default_storage";

    private static final long SIZE_KB = 1024L;
    private static final long SIZE_MB = SIZE_KB * SIZE_KB;

    public static class SpaceInformation {
        private final long mAvailableSpaceBytes;
        private final long mTotalSpaceBytes;

        private SpaceInformation(final long availableSpace, final long totalSpace) {
            mAvailableSpaceBytes = availableSpace;
            mTotalSpaceBytes = totalSpace;
        }

        public long getAvailableSpace() {
            return mAvailableSpaceBytes;
        }

        public long getTotalSpace() {
            return mTotalSpaceBytes;
        }

        public long getAvailableSpaceKiB() {
            return mAvailableSpaceBytes / SIZE_KB;
        }

        public long getTotalSpaceKiB() {
            return mTotalSpaceBytes / SIZE_KB;
        }

        public long getAvailableSpaceMiB() {
            return mAvailableSpaceBytes / SIZE_MB;
        }

        public long getTotalSpaceMiB() {
            return mTotalSpaceBytes / SIZE_MB;
        }
    }

    /**
     * @return Number of bytes available on External storage
     */
    public static SpaceInformation getSpaceInformation(Context context) {
        StatFs stat = new StatFs(getExternalStorageDirectory(context).getPath());
        return new SpaceInformation(stat.getAvailableBytes(), stat.getTotalBytes());
    }

    /**
     * @param context used to determine what is the default storage
     * @return the index of the storage location
     */
    public static int getDefaultStorage(Context context) {
        int defaultStorage = Settings.Global.getInt(context.getContentResolver(), KEY_DEFAULT_STORAGE, -1);

        if (defaultStorage < 0) {
            defaultStorage = 0;
        }
        return defaultStorage;
    }

    /**
     * Returns the external storage directory
     *
     * @param context used to determine what is the default storage
     * @return the default storage or {@link Environment#getExternalStorageDirectory()} in case of error
     */
    public static File getExternalStorageDirectory(Context context) {
        int defaultStorage = getDefaultStorage(context);

        List<File> dirs = getExternalStorageDirectories();
        if (dirs != null && defaultStorage < dirs.size()) {
            File storage = dirs.get(defaultStorage);
            if (!storage.exists()) {
                storage.mkdirs();
            }
            return storage;
        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * Uses internal {@link Environment}
     *
     * @return a list of files pointing to mount locations or null if there was an error
     */
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
     * Returns the external storage directory for given type
     *
     * @param context used to determine what is the default storage
     * @param type    see {@link Environment#getExternalStoragePublicDirectory(String)}
     * @return the default storage for given type or
     * {@link Environment#getExternalStoragePublicDirectory(String)} in case of error
     */
    public static File getExternalStoragePublicDirectory(Context context, String type) {
        int defaultStorage = getDefaultStorage(context);

        List<File> dirs = getExternalStoragePublicDirectories(type);
        if (dirs != null && defaultStorage < dirs.size()) {
            File storage = dirs.get(defaultStorage);
            if (!storage.exists()) {
                storage.mkdirs();
            }
            return storage;
        }
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * Uses internal {@link Environment}
     *
     * @param type see {@link Environment#getExternalStoragePublicDirectory(String)}
     * @return a list of files pointing to mount locations for given type or null if there was an error
     */
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
