package com.core.realwear.sdk.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.core.realwear.sdk.R;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */

public class FullScreenDialog extends Dialog {
    public FullScreenDialog(Context context) {
        super(context, R.style.TransparentDialogTheme);
        init();
    }

    public FullScreenDialog(Context context, int themeResId) {
        super(context, R.style.TransparentDialogTheme);
        init();
    }

    protected FullScreenDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Making dialog content transparent.
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        // Removing window dim normally visible when dialog are shown.
        this.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        // Setting position of content, relative to window.
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 20;
        // If user taps anywhere on the screen, dialog will be cancelled.
        this.setCancelable(false);
        setContentView( getLayout());
        setCancelable(false);
    }

    public int getLayout(){
        return -1;
    }

    private void init() {

    }
}
