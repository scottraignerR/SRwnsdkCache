package com.core.realwear.sdk.Util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fin on 18/09/2016.
 */
public class SpeechCommands {

    @Deprecated
    public static void UpdateHelp(Context context, String commands) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(commands.split(",")));
        UpdateHelp(context, list);
    }

    public static void UpdateHelp(Context context, ArrayList<String> commands){
        Intent intent = new Intent();
        intent.setAction("com.realware.wearhf.intent.action.UPDATE_HELP_COMMANDS");
        intent.putStringArrayListExtra("com.realware.wearhf.intent.extra.HELP_COMMANDS", commands);
        context.sendBroadcast(intent);
    }


}
