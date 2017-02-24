package com.core.realwear.sdk.Util;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */


import java.util.Locale;
import android.app.backup.BackupManager;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.util.Log;

public class Language {

    public static Locale getLanguage()
    {
        return Locale.getDefault();
    }

    /**
     * Requests the system to update the system locale. Note that the system looks halted
     * for a while during the Locale migration, so the caller need to take care of it.
     */
    public static void setLanguage(Locale locale) {

		/*IActivityManager am = ActivityManagerNative.getDefault();
		Configuration config;
		try {
			config = am.getConfiguration();
			config.locale = Locale.CHINA;
			am.updateConfiguration(config);
			//Trigger the dirty bit for the Settings Provider.
			BackupManager.dataChanged("com.android.providers.settings");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Object am=activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
			Object config=am.getClass().getMethod("getConfiguration").invoke(am);
			config.getClass().getDeclaredField("locale").set(config, locale);
			config.getClass().getDeclaredField("userSetLocale").setBoolean(config, true);

			am.getClass().getMethod("updateConfiguration",android.content.res.Configuration.class).invoke(am,config);

		}catch (Exception e) {
			e.printStackTrace();
		}
    }

}
