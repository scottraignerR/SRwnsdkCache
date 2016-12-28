package com.core.realwear.sdk.Util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
        mScrollview.scrollBy(100,0);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoNextLanguage();
            }
        }, 2000);
    }

    public void gotoPreviousLanguage(){

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
            View langView = getLayoutInflater().inflate(R.layout.language_item, null);
            TextView langTxt = (TextView)langView.findViewById(R.id.langtxt);
            ImageView imgView = (ImageView)langView.findViewById(R.id.langimg);

            langTxt.setText(lang.Name);
            imgView.setImageResource(lang.ResourceId);
            mPlaceHolder.addView(langView);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.language_dialog;
    }
}
