/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
                        .setDefaultFontPath("fonts/Montserrat-Medium.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mCurrentActivity = activity;
        Resources.Theme theme = activity.getTheme();
        TypedArray ta = activity.getTheme().obtainStyledAttributes(R.styleable.Themes);
        int darkStyle = ta.getResourceId(R.styleable.Themes_styleDark, -1);
        int lightStyle = ta.getResourceId(R.styleable.Themes_styleLight, -1);

        ta.recycle();
        //Settings.System.putInt(getContentResolver(), "")
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
        //int themeSaved = Settings.System.getInt(getContentResolver(), "Theme", -1);
        int themeSaved = settings.getInt("Theme", -1);

        switch (themeSaved) {
            case 0:
                /*if (darkStyle != -1)
                    theme.applyStyle(darkStyle, true);
                else
                    theme.applyStyle(R.style.WearDark, true);
                break;*/
            case -1:
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
