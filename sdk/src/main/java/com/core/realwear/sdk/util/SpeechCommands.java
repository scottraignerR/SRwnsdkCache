package com.core.realwear.sdk.util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        updateHelp(context, Arrays.asList(commands.split(",")));
    }

    @Deprecated
    public static void UpdateHelp(Context context, List<String> commands){
        updateHelp(context, commands);
    }

    public static void updateHelp(Context context, List<String> commands) {
        final Intent intent = new Intent();
        intent.setPackage(WEARHF_PACKAGE);
        intent.setAction(ACTION_UPDATE_HELP_COMMANDS);
        if (commands instanceof ArrayList) {
            intent.putStringArrayListExtra(EXTRA_HELP_COMMANDS, (ArrayList<String>) commands);
        } else {
            intent.putStringArrayListExtra(EXTRA_HELP_COMMANDS, new ArrayList<>(commands));
        }
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
