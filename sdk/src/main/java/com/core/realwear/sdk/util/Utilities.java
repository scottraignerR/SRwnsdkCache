package com.core.realwear.sdk.util;

import android.os.Environment;
import android.os.StatFs;

public final class Utilities {
    private static final long SIZE_KB = 1024L;
    private static final long SIZE_MB = SIZE_KB * SIZE_KB;

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
}
