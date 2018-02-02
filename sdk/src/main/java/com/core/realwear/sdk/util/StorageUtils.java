package com.core.realwear.sdk.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
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
    private static final String KEY_DEFAULT_STORAGE = "default_storage";

    private static final long SIZE_KB = 1024L;
    private static final long SIZE_MB = SIZE_KB * SIZE_KB;

    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpace(Context context) {
        long availableSpace;
        StatFs stat = new StatFs(getExternalStorageDirectory(context).getPath());
        availableSpace = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        return availableSpace;
    }

    /**
     * @return Number of kilo bytes available on External storage
     */
    public static long getAvailableSpaceKB(Context context) {
        return getAvailableSpace(context) / SIZE_KB;
    }

    /**
     * @return Number of Mega bytes available on External storage
     */
    public static long getAvailableSpaceMB(Context context) {
        return getAvailableSpace(context) / SIZE_MB;
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

    public static boolean hasSDCard() {
        List<File> paths = getExternalStorageDirectories();
        return paths != null && paths.size() > 1;
    }
}
