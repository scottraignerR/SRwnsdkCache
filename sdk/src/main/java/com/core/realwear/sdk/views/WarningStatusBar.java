/*
 * RealWear Development Software, Source Code and Object Code.
 * (C) RealWear, Inc. All rights reserved.
 *
 * Contact info@realwear.com for further information about the use of this code.
 *
 * Filename: WarningStatusBar.java
 * Class: WarningStatusBar
 * Author: alexandru.ghitulescu
 *
 */

/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.core.realwear.sdk.R;

/**
 *
 */

public class WarningStatusBar {
    private static final int ANIMATION_DURATION = 700;

    private final View mContent;
    private final ViewGroup mContainer;
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mLayoutParams;
    private final TextView mTitle, mText;

    private final Handler mHandler;

    private boolean mVisible = false;

    private Runnable mOnHideRunnable;

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public WarningStatusBar(Context context, Runnable onHideRunnable) {
        mOnHideRunnable = onHideRunnable;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mContainer = new SilentFrameLayout(context);

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        LayoutInflater factory = LayoutInflater.from(context);
        mContent = factory.inflate(R.layout.warninig_status_bar, mContainer, false);

        mTitle = mContent.findViewById(R.id.warning_title);
        mText = mContent.findViewById(R.id.warning_text);

        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Shows the status bar with a slide in from top animation. It get automatically dismissed in
     * {@code timeout} milliseconds.
     *
     * @param timeout time in milliseconds from now, when it should be dismissed, if 0, or less,
     *                notification doesn't get dismissed any more
     */
    public void show(long timeout) {
        mHandler.removeCallbacks(mHideRunnable);
        show();
        if (timeout > 0) {
            mHandler.postDelayed(mHideRunnable, timeout);
        }
    }

    private void show() {
        if (mVisible) {
            return;
        }

        mVisible = true;
        mWindowManager.addView(mContainer, mLayoutParams);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Transition transition = new Slide(Gravity.TOP);
                transition.setDuration(ANIMATION_DURATION);
                transition.setInterpolator(new FastOutSlowInInterpolator());

                TransitionManager.beginDelayedTransition(mContainer, transition);
                mContainer.addView(mContent);
            }
        }, 300);
    }

    public void hide() {
        if (!mVisible) {
            return;
        }

        mVisible = false;

        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(ANIMATION_DURATION);
        transition.setInterpolator(new FastOutSlowInInterpolator());
        TransitionManager.beginDelayedTransition(mContainer, transition);
        mContainer.removeAllViews();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(mContainer);
            }
        }, ANIMATION_DURATION);

        if (mOnHideRunnable != null) {
            mOnHideRunnable.run();
        }
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public void setTitle(@StringRes int title) {
        mTitle.setText(title);
    }

    public void setText(@StringRes int text) {
        mText.setText(text);
    }

    /**
     * Silent frame layout that never sends any accessibility events.
     */
    private static class SilentFrameLayout extends FrameLayout {
        public SilentFrameLayout(Context context) {
            super(context);
        }

        @Override
        public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
            // Do not send any accessibility events.
            return false;
        }

        @Override
        public void sendAccessibilityEventUnchecked(@NonNull AccessibilityEvent event) {
            // Do not send any accessibility events.
        }
    }
}
