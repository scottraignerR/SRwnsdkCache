package com.core.realwear.sdk.Util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.realwear.sdk.R;

import java.util.List;

/**
 * Created by Luke Hopkins on 28/12/2016.
 */

public class LanguageDialog extends FullScreenDialog {
    private WearLanguage mSelectedLang;
    private List<WearLanguage> mCurrentLanguages;
    HorizontalScrollView mScrollview;
    private Handler mHandler;
    private LinearLayout mPlaceHolder;

    public LanguageDialog(Context context) {
        super(context);
    }

    public LanguageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LanguageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setLanguages(List<WearLanguage> lanagues){
        mCurrentLanguages = lanagues;
    }

    public void gotoNextLanguage(){
        //mScrollview.scrollBy(100,0);
        //clone
        final View newView = createLangView((WearLanguage) mPlaceHolder.getChildAt(0).getTag());
        mPlaceHolder.addView(newView);
        mPlaceHolder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mPlaceHolder.removeOnLayoutChangeListener(this);
                newView.animate().translationX(-newView.getWidth()).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mPlaceHolder.removeView(mPlaceHolder.getChildAt(0));

                        for(int i = 0; i < mPlaceHolder.getChildCount(); i++) {
                            View v = mPlaceHolder.getChildAt(i);
                            v.setTranslationX(0);
                        }
                    }
                }).start();
            }
        });

        for(int i = 0; i < mPlaceHolder.getChildCount(); i++){
            View v = mPlaceHolder.getChildAt(i);
            final View rV = v;
            if(i == 0){
                v.animate().translationX(-v.getWidth()).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //mPlaceHolder.removeView(rV);
                    }
                }).start();
            }else if(i == mPlaceHolder.getChildCount() - 1){
            }
            else{
                v.animate().translationX(-v.getWidth()).setDuration(500).start();
            }

            if(i == 1) {
                mSelectedLang = (WearLanguage) v.getTag();
            }

        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoNextLanguage();
            }
        }, 2000);

    }

    public Animation.AnimationListener mNewView = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

            for(int i = 0; i < mPlaceHolder.getChildCount(); i++){
                View v = mPlaceHolder.getChildAt(i);
                final View rV = v;
                if(i == 0){
                    v.animate().translationX(-v.getWidth()).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mPlaceHolder.removeView(rV);
                        }
                    }).start();
                }else if(i == mPlaceHolder.getChildCount() - 1){
                    v.animate().translationX(0).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < mPlaceHolder.getChildCount() -1; i++) {
                                View v = mPlaceHolder.getChildAt(i);
                                v.setTranslationX(0);
                            }
                        }
                    }).start();
                }
                else{
                    v.animate().translationX(-v.getWidth()).setDuration(500).start();
                }

                if(i == 1) {
                    mSelectedLang = (WearLanguage) v.getTag();
                }

            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNextLanguage();
                }
            }, 2000);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void gotoPreviousLanguage(){

    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        return inFromRight;
    }

    public WearLanguage getSelectedLanguage(){
        return mSelectedLang;
    }

    @Override
    public void show() {
        super.show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoNextLanguage();
            }
        }, 2000);
    }

    @Override
    public void hide() {
        super.hide();

        mHandler.removeCallbacks(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        mScrollview = (HorizontalScrollView) findViewById(R.id.scrollView);
        mPlaceHolder = (LinearLayout) findViewById(R.id.scrollViewplaceholder);
        mCurrentLanguages = WearLanguage.getCurrentLanguages();

        for(WearLanguage lang : mCurrentLanguages){
            mPlaceHolder.addView(createLangView(lang));
        }
    }

    private View createLangView(WearLanguage lang){
        View langView = getLayoutInflater().inflate(R.layout.language_item, null);
        TextView langTxt = (TextView)langView.findViewById(R.id.langtxt);
        ImageView imgView = (ImageView)langView.findViewById(R.id.langimg);
        langView.setTag(lang);
        langTxt.setText(lang.Name);
        imgView.setImageResource(lang.ResourceId);
        return langView;
    }

    @Override
    public int getLayout() {
        return R.layout.language_dialog;
    }
}
