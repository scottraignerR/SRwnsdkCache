package com.core.realwear.sdk.Util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fin on 18/09/2016.
 */
public class SpeechCommands {
    public static final String WEARHF_PACKAGE = "com.realwear.wearhf";
    public static final String ACTION_UPDATE_HELP_COMMANDS = "com.realwear.wearhf.intent.action.UPDATE_HELP_COMMANDS";
    public static final String EXTRA_HELP_COMMANDS = "com.realwear.wearhf.intent.extra.HELP_COMMANDS";
    public static final String EXTRA_SOURCE_PACKAGE = "com.realwear.wearhf.intent.extra.SOURCE_PACKAGE";
    public static final String ACTION_UPDATE_REFRESH_UI = "com.realwear.wearhf.intent.action.REFRESH_UI";

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
        intent.setPackage(WEARHF_PACKAGE);
        intent.setAction(ACTION_UPDATE_HELP_COMMANDS);
        intent.putStringArrayListExtra(EXTRA_HELP_COMMANDS, commands);
        intent.putExtra(EXTRA_SOURCE_PACKAGE, context.getPackageName());
        context.sendBroadcast(intent);
    }

    public static void refreshUI(Context context) {
        final Intent intent = new Intent();
        intent.setPackage(WEARHF_PACKAGE);
        intent.setAction(ACTION_UPDATE_REFRESH_UI);
        intent.putExtra(EXTRA_SOURCE_PACKAGE, context.getPackageName());
        context.sendBroadcast(intent);
    }
}
