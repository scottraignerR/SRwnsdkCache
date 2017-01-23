package com.core.realwear.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Luke on 22/01/2017.
 */

public class HiddenTextView extends TextView {
    public HiddenTextView(Context context) {
        super(context);
        init();
    }

    public HiddenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiddenTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HiddenTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setAlpha(0.01f);
        setClickable(true);
    }
}
