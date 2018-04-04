/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk;

import android.content.Context;

/**
 * Created by Luke on 23/01/2017.
 */
public interface IVoiceAdapter {
    String getVoiceCommand(int index);
    void selectItem(Context context, int index);
}
