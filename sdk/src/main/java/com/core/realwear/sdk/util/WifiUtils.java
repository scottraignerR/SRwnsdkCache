package com.core.realwear.sdk.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.Objects;

public class WifiUtils {

    @TargetApi(Build.VERSION_CODES.M)
    public static WifiConfiguration getCurrentWifiConfiguration(Context context) {
        final WifiManager wifiManager = context.getSystemService(WifiManager.class);
        assert wifiManager != null;
        final WifiInfo connection = wifiManager.getConnectionInfo();

        if (connection != null) {
            return findWifiConfigurationForSSID(connection.getSSID(), context);
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    @TargetApi(Build.VERSION_CODES.M)
    public static WifiConfiguration findWifiConfigurationForSSID(String ssid, Context context) {
        final WifiManager wifiManager = context.getSystemService(WifiManager.class);
        assert wifiManager != null;
        for (WifiConfiguration conf : wifiManager.getConfiguredNetworks()) {
            if (Objects.equals(conf.SSID, ssid)) {
                return conf;
            }
        }
        return null;
    }

    /**
     * Does the supplied wifi configuration refer to an open network?
     * <p>
     * Note: The code for this is based on Android source code and needs to be rechecked in future
     * to confirm it is still valid. Based on code from com.android.settingslib.wifi.AccessPoint
     * (https://android.googlesource.com/platform/frameworks/base/+/master/packages/SettingsLib/src/com/android/settingslib/wifi/AccessPoint.java)
     * specifically the function <code>static int getSecurity(WifiConfiguration config)</code>
     *
     * @param config The wifi configuration to check.
     * @return True if the network lacks security such as WPA, WEP, etc. False if security is found.
     */
    public static boolean isOpenAccess(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK) ||
                config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) ||
                config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return false;
        }
        return config.wepKeys[0] == null;
    }
}
