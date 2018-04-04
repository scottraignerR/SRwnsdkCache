/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Fin on 23/08/2016.
 */
public class WearActionBar extends RelativeLayout implements View.OnClickListener {
    private TextView mMyControls, mShowHelp;
    private TextView mShowCommandText;
    private RelativeLayout mInnerLayout;
    private View mHiddenCommands;

    public WearActionBar(Context context) {
        super(context);
        init();
    }

    public WearActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WearActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        View.inflate(getContext(), R.layout.wear_actionbar, this);
        mInnerLayout = (RelativeLayout)findViewById(R.id.innerCommands);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mMyControls)){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings"));
            getContext().startActivity(intent);
        }else if (view.equals(mShowHelp)){
           /* if(mHiddenCommandsView.getVisibility() == View.INVISIBLE) {
                mShowCommandText.setText(getContext().getString(R.string.hide_help));
                mHiddenCommandsView.showCommands();
            }
            else{
                mShowCommandText.setText(getContext().getString(R.string.show_help));
                mHiddenCommandsView.hideCommands();
            }*/
        }
    }

    public void setHiddenLayout(int resourceId){
        if(mHiddenCommands != null)
            mInnerLayout.removeView(mHiddenCommands);

        mHiddenCommands = View.inflate(getContext(),resourceId, null);

        //mInnerLayout.addView(mHiddenCommands, 0);
        //mHiddenCommandsView.setResourceId(resourceId);
    }

    public void addHiddenLayout(int resourceId){
        if(mHiddenCommands != null)
            mInnerLayout.removeView(mHiddenCommands);

        mHiddenCommands = View.inflate(getContext(),resourceId, null);

        mInnerLayout.addView(mHiddenCommands, 0);
        //mHiddenCommandsView.setResourceId(resourceId);
    }

    public void showCommands() {
       /* if(mHiddenCommandsView.getVisibility() == View.VISIBLE) {
            mShowCommandText.setText(getContext().getString(R.string.show_help));
            mHiddenCommandsView.hideCommands();
        }*/
    }

}
