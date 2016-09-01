package com.core.wearnext.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Fin on 23/08/2016.
 */
public class WearActionBar extends RelativeLayout implements View.OnClickListener {
    private TextView mMyControls, mShowHelp;

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
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mMyControls)){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings"));
            getContext().startActivity(intent);
        }
    }

    public void addCommands(View view){
        addView(view);
    }

}
