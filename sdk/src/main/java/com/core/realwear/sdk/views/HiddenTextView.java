/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Luke on 22/01/2017.
 */

public class HiddenTextView extends AppCompatTextView {
    public HiddenTextView(Context context) {
        this(context, null);
    }

    public HiddenTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiddenTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAlpha(0.01f);
        setClickable(true);
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }
}
