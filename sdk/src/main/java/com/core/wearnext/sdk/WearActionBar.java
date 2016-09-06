package com.core.wearnext.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.wearnext.sdk.views.HiddenControls;

/**
 * Created by Fin on 23/08/2016.
 */
public class WearActionBar extends RelativeLayout implements View.OnClickListener {
    private TextView mMyControls, mShowHelp;
    private HiddenControls mHiddenCommandsView;

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

        mShowHelp = (TextView)findViewById(R.id.show_help);
        mMyControls = (TextView)findViewById(R.id.my_controls);
        mMyControls.setOnClickListener(this);
        mShowHelp.setOnClickListener(this);
        mHiddenCommandsView = (HiddenControls)findViewById(R.id.hiddenControl);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mMyControls)){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings"));
            getContext().startActivity(intent);
        }else if (view.equals(mShowHelp)){
            if(mHiddenCommandsView.getVisibility() == View.INVISIBLE)
                mHiddenCommandsView.showCommands();
            else{
                mHiddenCommandsView.hideCommands();
            }
        }
    }

    public void setHiddenLayout(int resourceId){
        mHiddenCommandsView.setResourceId(resourceId);
    }

}
