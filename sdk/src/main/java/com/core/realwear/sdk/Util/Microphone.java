package com.core.realwear.sdk.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fin on 26/09/2016.
 */
public class Microphone {
    private static final String ACTION_RELEASE_MIC = "com.realwear.wearhf.intent.action.RELEASE_MIC";
    private static final String ACTION_MIC_RELEASED = "com.realwear.wearhf.intent.action.MIC_RELEASED";

    private static final String EXTRA_MUTE_TEXT = "com.realwear.wearhf.intent.extra.MUTE_TEXT";
    private static final String EXTRA_SOURCE_PACKAGE = "com.realwear.wearhf.intent.extra.SOURCE_PACKAGE";

    private static final String PACKAGE_NAME = "com.realwear.wearhf";


    private static BroadcastReceiver broadcastReceiver = null;
    private static List<OnMicReleased> micReleasedListeners = new ArrayList<>();

    // TODO: MAKE CONSTANT AND TIDY UP
    public static void stopMicrophone(Context context, String text, final OnMicReleased listener) {
        final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(ACTION_MIC_RELEASED)) {
                    if (listener != null) {
                        listener.onReleased();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(ACTION_MIC_RELEASED);
        context.registerReceiver(mBroadcastReceiver, filter);

        Intent intent = new Intent();
        intent.setPackage(PACKAGE_NAME);
        intent.setAction(ACTION_RELEASE_MIC);
        intent.putExtra(EXTRA_MUTE_TEXT, text);
        intent.putExtra(EXTRA_SOURCE_PACKAGE, context.getPackageName());
        context.sendBroadcast(intent);
    }

    /**
     * Register a microphone listener. Note only one listener can be registered at a time.
     *
     * @param context Application context.
     * @param listener Listener for the microphone released events.
     */
    public static void registerMicrophoneListener(Context context, final OnMicReleased listener) {
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ACTION_RELEASE_MIC)) {
                        for (OnMicReleased listener : micReleasedListeners) {
                            listener.onReleased();
                        }

                        // Indicate that the mic has been released.
                        Intent releasedIntent = new Intent();
                        releasedIntent.setPackage(PACKAGE_NAME);
                        releasedIntent.setAction(ACTION_MIC_RELEASED);
                        releasedIntent.putExtra(EXTRA_SOURCE_PACKAGE, context.getPackageName());
                        context.sendBroadcast(releasedIntent);
                    }
                }
            };

            context.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_RELEASE_MIC));
        }

        micReleasedListeners.add(listener);
    }

    /**
     * Unregister a microphone listener.
     *
     * @param context Application context.
     * @param listener
     */
    public static void unregisterMicrophoneListener(Context context, final OnMicReleased listener) {
        micReleasedListeners.remove(listener);
    }

    public interface OnMicReleased {
        void onReleased();
    }
}
