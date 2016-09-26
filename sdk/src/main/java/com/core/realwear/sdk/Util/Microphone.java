package com.core.realwear.sdk.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Fin on 26/09/2016.
 */
public class Microphone {

    //TODO MAKE CONSTANT AND TIDY UP
    public static void stopMicrophone(Context context, final onMicReleased listener){
        final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals("com.realware.wearhf.intent.action.MIC_RELEASED")) {
                    if(listener != null)
                    {
                        listener.onMicrophoneReleased();
                    }
                }
            }
        };

        IntentFilter filter1 = new IntentFilter("com.realware.wearhf.intent.action.MIC_RELEASED");
        context.registerReceiver(mBroadcastReceiver, filter1);

        Intent intent = new Intent();
        intent.setPackage("com.realware.wearhf");
        intent.setAction("com.realware.wearhf.intent.action.RELEASE_MIC");
        intent.putExtra("com.realware.wearhf.intent.extra.MUTE_TEXT", "Press PTT button to stop recording");
        context.sendBroadcast(intent);
    }



    public static void startMicrophone(Context context,onMicReleased listener){
        Intent intent = new Intent();
        intent.setPackage("com.realware.wearhf");
        intent.setAction("com.realware.wearhf.intent.action.MIC_RELEASED");
        context.sendBroadcast(intent);
    }


    public interface onMicReleased{
        void onMicrophoneReleased();
    }

}
