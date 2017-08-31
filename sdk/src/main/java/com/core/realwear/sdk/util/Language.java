package com.core.realwear.sdk.util;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */


import android.util.Log;

import java.util.Locale;

public class Language {

    public static Locale getLanguage() {
        return Locale.getDefault();
    }

    /**
     * Requests the system to update the system locale. Note that the system looks halted
     * for a while during the Locale migration, so the caller need to take care of it.
     */
    public static void setLanguage(Locale locale) {
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Object config = am.getClass().getMethod("getConfiguration").invoke(am);
            config.getClass().getDeclaredField("locale").set(config, locale);
            config.getClass().getDeclaredField("userSetLocale").setBoolean(config, true);

            am.getClass().getMethod("updateConfiguration", android.content.res.Configuration.class).invoke(am, config);
        } catch (Exception e) {
            Log.e("Language", "", e);
        }
    }
}
