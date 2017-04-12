package com.core.realwear.sdk.util;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.util.TypedValue;
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

        final LayoutInflater inflater = context.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast, null);
        mView = (TextView)layout.findViewById(R.id.textView);

        setView(layout);//setting the view of custom toast layout
    }

    @Override
    public void setText(CharSequence s) {
        final String nbspString = s.toString().replaceAll(" ", Character.toString((char)0xA0));
        mView.setText(nbspString);
    }

    public static WearToast makeWearText(Activity context, CharSequence text, int duration) {
        final WearToast result = new WearToast(context);

        final String nbspString = text.toString().replaceAll(" ", Character.toString((char)0xA0));

        final LayoutInflater inflater = LayoutInflater.from(context);//context.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.toast, null);

        final TextView view = (TextView)layout.findViewById(R.id.textView);
        view.setText(nbspString);

        // gravity, xOffset, yOffset
        final Resources r = context.getResources();
        final int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 58, r.getDisplayMetrics());
        result.setGravity(Gravity.CENTER, 0, px);

        result.setView(layout);
        result.setDuration(duration);

        return result;
    }

}
