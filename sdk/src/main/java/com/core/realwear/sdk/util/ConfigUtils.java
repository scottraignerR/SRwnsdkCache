/*
 * RealWear Development Software, Source Code and Object Code.
 * (C) RealWear, Inc. All rights reserved.
 *
 * Contact info@realwear.com for further information about the use of this code.
 */

package com.core.realwear.sdk.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

public final class ConfigUtils {
    private ConfigUtils() {

    }

    @NonNull
    public static Bitmap decodeBase64Image(@NonNull String stringImage) {
        final byte[] decodedString = Base64.decode(stringImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
