package com.core.realwear.sdk.Util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Fin on 18/09/2016.
 */
public class SpeechCommands {
        public static void UpdateHelp(Context context, String commands){

            Intent intent = new Intent();
            intent.setAction("com.realware.wearhf.intent.action.UPDATE_HELP_COMMANDS");
            intent.putExtra("com.realware.wearhf.intent.extra.HELP_COMMANDS", commands);
            context.sendBroadcast(intent);
        }

}
