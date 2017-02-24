package com.core.realwear.sdk.iAdapter;

import android.content.Context;
import android.view.View;

/**
 * Created by Luke on 23/01/2017.
 */
public interface iVoiceAdapter {
    String getVoiceCommand(int index);
    String getSecondaryCommand(Context context, int index);
    void clickView(Context context, int index);
}
