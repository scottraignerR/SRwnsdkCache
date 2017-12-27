package com.core.realwear.sdk.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
     * Returns the external storage directory
     * @param context used to determine if it should use
     * @return
     */
    public static File getExternalStorageDirectory(Context context) {
        int defaultStorage = Settings.Global.getInt(context.getContentResolver(), KEY_DEFAULT_STORAGE, -1);

        if (defaultStorage < 0) {
            defaultStorage = 0;
        }

        File[] dirs = getExternalStorageDirectories();
        if (dirs != null && defaultStorage < dirs.length) {
            File storage = dirs[defaultStorage];
            if (!storage.exists()) {
                storage.mkdirs();
            }
            return storage;
        }
        return Environment.getExternalStorageDirectory();
    }

    public static File[] getExternalStorageDirectories() {
        try {
            Field sCurrentUserField = Environment.class.getDeclaredField("sCurrentUser");
            sCurrentUserField.setAccessible(true);

            Object sCurrentUser = sCurrentUserField.get(null);
            Method getExternalDirs = sCurrentUser.getClass().getDeclaredMethod("getExternalDirs");
            return (File[]) getExternalDirs.invoke(sCurrentUser);
        } catch (Exception e) {
            Log.w(TAG, "", e);
            return null;
        }
    }

    public static File getExternalStoragePublicDirectory(Context context, String type) {
        int defaultStorage = Settings.Global.getInt(context.getContentResolver(), KEY_DEFAULT_STORAGE, -1);

        if (defaultStorage < 0) {
            defaultStorage = 0;
        }

        File[] dirs = getExternalStoragePublicDirectories(type);
        if (dirs != null && defaultStorage < dirs.length) {
            File storage = dirs[defaultStorage];
            if (!storage.exists()) {
                storage.mkdirs();
            }
            return storage;
        }
        return Environment.getExternalStoragePublicDirectory(type);
    }

    public static File[] getExternalStoragePublicDirectories(String type) {
        try {
            Field sCurrentUserField = Environment.class.getDeclaredField("sCurrentUser");
            sCurrentUserField.setAccessible(true);

            Object sCurrentUser = sCurrentUserField.get(null);
            Method buildExternalStoragePublicDirs = sCurrentUser.getClass().getDeclaredMethod("buildExternalStoragePublicDirs", String.class);

            return (File[]) buildExternalStoragePublicDirs.invoke(sCurrentUser, type);
        } catch (Exception e) {
            Log.w(TAG, "", e);
            return null;
        }
    }
}
