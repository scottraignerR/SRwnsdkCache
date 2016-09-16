package com.core.realwear.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.core.realwear.sdk.R;

/**
 * Created by Fin on 05/09/2016.
 */
public class HiddenControls extends RelativeLayout {

    private LinearLayout mHiddenCommandsView;
    View mHiddenCommands;

    public HiddenControls(Context context) {
        super(context);
        init();
    }

    public HiddenControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiddenControls(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
       View.inflate(getContext(), R.layout.show_help, this);
        setVisibility(View.INVISIBLE);

        mHiddenCommandsView = (LinearLayout)findViewById(R.id.commandView);
    }
    
    public void setResourceId(int resourceId){
        if(mHiddenCommands != null){
            mHiddenCommandsView.removeView(mHiddenCommands);
        }
        mHiddenCommands = View.inflate(getContext(), resourceId, null);
        mHiddenCommandsView.addView(mHiddenCommands);
    }
    

    public void showCommands(){
        setVisibility(View.VISIBLE);
    }

    public void hideCommands(){
        setVisibility(View.INVISIBLE);
    }



}
