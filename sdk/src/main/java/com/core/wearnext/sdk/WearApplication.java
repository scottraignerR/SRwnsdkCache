package com.core.wearnext.sdk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.core.wearnext.sdk.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Fin on 29/08/2016.
 */
public class WearApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private Activity mCurrentActivity;
    private boolean mDontReload = false;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Montserrat-Bold.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mCurrentActivity = activity;
        Resources.Theme theme = activity.getTheme();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        TypedArray ta = activity.getTheme().obtainStyledAttributes(R.styleable.Themes);
        int darkStyle = ta.getResourceId(R.styleable.Themes_styleDark, -1);
        int lightStyle = ta.getResourceId(R.styleable.Themes_styleLight, -1);

        ta.recycle();
        //Settings.System.putInt(getContentResolver(), "")
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
        //int themeSaved = Settings.System.getInt(getContentResolver(), "Theme", -1);
        int themeSaved = settings.getInt("Theme", -1);

        switch (themeSaved) {
            case -1:
            case 0:
                if (darkStyle != -1)
                    theme.applyStyle(darkStyle, true);
                else
                    theme.applyStyle(R.style.WearDark, true);
                break;
            case 1:
                if (lightStyle != -1)
                    theme.applyStyle(lightStyle, true);
                else
                    theme.applyStyle(R.style.WearLight, true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
