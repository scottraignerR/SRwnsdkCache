package com.core.realwear.sdk.Util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fin on 18/09/2016.
 */
public class SpeechCommands {
    public static final String ACTION_UPDATE_HELP_COMMANDS = "com.realwear.wearhf.intent.action.UPDATE_HELP_COMMANDS";
    public static final String EXTRA_HELP_COMMANDS = "com.realwear.wearhf.intent.extra.HELP_COMMANDS";
    public static final String EXTRA_SOURCE_PACKAGE = "com.realwear.wearhf.intent.extra.SOURCE_PACKAGE";

    @Deprecated
    public static void UpdateHelp(Context context, String commands) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(commands.split(",")));
        updateHelp(context, list);
    }

    @Deprecated
    public static void UpdateHelp(Context context, ArrayList<String> commands){
        updateHelp(context, commands);
    }

    public static void updateHelp(Context context, ArrayList<String> commands) {
        final Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_HELP_COMMANDS);
        intent.putStringArrayListExtra(EXTRA_HELP_COMMANDS, commands);
        intent.putExtra(EXTRA_SOURCE_PACKAGE, context.getPackageName());
        context.sendBroadcast(intent);
    }
}
