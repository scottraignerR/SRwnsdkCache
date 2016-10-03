package com.core.realwear.sdk.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.core.realwear.sdk.R;

/**
 * Created by Luke Hopkins on 03/10/2016.
 */

public class WearToast extends Toast {
    private final TextView mView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public WearToast(Activity context) {
        super(context);

        LayoutInflater myInflator = context.getLayoutInflater();
        View layout = myInflator.inflate(R.layout.toast,null);
        mView = (TextView)layout.findViewById(R.id.textView);

        // gravity, xOffset, yOffset
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        setView(layout);//setting the view of custom toast layout
    }

    @Override
    public void setText(CharSequence s) {
        //super.setText(s);
        mView.setText(s);
    }

    public static WearToast makeWearText(Activity context, CharSequence text, int duration) {
        WearToast result = new WearToast(context);

        LayoutInflater myInflator = context.getLayoutInflater();
        View layout = myInflator.inflate(R.layout.toast,null);
        TextView view = (TextView)layout.findViewById(R.id.textView);
        view.setText(text);
        // gravity, xOffset, yOffset
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        result.setView(layout);
        result.setDuration(duration);

        return result;
    }

}
