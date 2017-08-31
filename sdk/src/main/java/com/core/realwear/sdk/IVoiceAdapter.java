package com.core.realwear.sdk;

import android.content.Context;

/**
 * Created by Luke on 23/01/2017.
 */
public interface IVoiceAdapter {
    String getVoiceCommand(int index);
    void selectItem(Context context, int index);
}
