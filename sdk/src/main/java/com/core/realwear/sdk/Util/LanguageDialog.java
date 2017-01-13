package com.core.realwear.sdk.Util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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
    private boolean canGoNext = true;
    private boolean hasPosted = true;
    private boolean mShowing = false;

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
        //clone

        if(!canGoNext)
            dismiss();

        final View newView = createLangView((WearLanguage) mPlaceHolder.getChildAt(0).getTag());
        mPlaceHolder.addView(newView);


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlaceHolder.removeViewAt(0);
                mSelectedLang = (WearLanguage) (mPlaceHolder.getChildAt(1)).getTag();
            }
        }, 50);



        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasPosted = true;
                if(canGoNext)
                    gotoNextLanguage();
                else
                    dismiss();
            }
        }, 2000);

    }

    public WearLanguage getSelectedLanguage(){
        return mSelectedLang;
    }

    @Override
    public void show() {
        super.show();
        if(!mShowing) {
            setCurrentLanguage();
            mShowing = true;
            canGoNext = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNextLanguage();
                }
            }, 2000);
        }

    }

    private void setCurrentLanguage() {
        mPlaceHolder.removeAllViews();
        mCurrentLanguages = WearLanguage.getCurrentLanguages();

        for(WearLanguage lang : mCurrentLanguages){
            mPlaceHolder.addView(createLangView(lang));
        }
    }

    @Override
    public void hide() {
        super.hide();
        mShowing = false;
        mHandler.removeCallbacks(null);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        mShowing = false;
        mHandler.removeCallbacks(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == 500){
            canGoNext = false;
            mHandler.removeCallbacks(null);
            dismiss();

        }
        return super.onKeyUp(keyCode, event);
    }

    public void canGoNext(boolean value){
        canGoNext = value;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
//        mScrollview = (HorizontalScrollView) findViewById(R.id.scrollView);
        mPlaceHolder = (LinearLayout) findViewById(R.id.scrollViewplaceholder);
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
