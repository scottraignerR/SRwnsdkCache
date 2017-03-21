package com.core.realwear.sdk.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RelativeLayout;

import com.core.realwear.sdk.HFHeadtrackerListener;
import com.core.realwear.sdk.HFHeadtrackerManager;
import com.core.realwear.sdk.R;
import com.core.realwear.sdk.RecyclerViewMargin;
import com.core.realwear.sdk.IVoiceAdapter;

/**
 * Created by Fin on 31/08/2016.
 */
public class WearableListView extends RelativeLayout implements View.OnClickListener, HFHeadtrackerListener {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RelativeLayout mHiddenCommandLayout;

    private static String NO_SCROLL = "hf_scroll_none";

    private HFHeadtrackerManager mHeadTracker;
    private View mRootGroup;

    private static final String ACTION_SPEECH_EVENT = "com.realwear.wearhf.intent.action.SPEECH_EVENT";
    private RecyclerViewMargin mDecoration;
    private int mX;
    private Handler mHandler;
    private injectCommands mAdditional;


    public WearableListView(Context context) {
        super(context);
        init();
    }

    public WearableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WearableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeadTracker = new HFHeadtrackerManager(this);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_recycleview, null);

        addView(view);

        mRecycleView = (RecyclerView) view.findViewById(R.id.list2);
        mRecycleView.setContentDescription(NO_SCROLL);

        mDecoration = new RecyclerViewMargin(250, 1);
        mRecycleView.addItemDecoration(mDecoration);
        mHiddenCommandLayout = (RelativeLayout) view.findViewById(R.id.hidden_commands);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //updateScroll();
            }
        }, 5);

    }

    public void updateScroll() {
        if (mRecycleView != null)
            mRecycleView.scrollBy(mX, 0);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateScroll();
            }
        }, 5);
    }

    public void onResume() {
        if (mHeadTracker != null)
            mHeadTracker.startHeadtracker(getContext(), HFHeadtrackerManager.TRACK_HORIZONTAL_MOTION);

        getContext().registerReceiver(asrBroadcastReceiver, new IntentFilter(ACTION_SPEECH_EVENT));
    }

    public void onPause() {
        if (mHeadTracker != null) mHeadTracker.stopHeadtracker();
        getContext().unregisterReceiver(asrBroadcastReceiver);
    }

    public void setRootView(View v) {
        mRootGroup = v;
    }

    public RecyclerView getRecycleView() {
        return mRecycleView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecycleView.setAdapter(adapter);


        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setCommands();

                mDecoration.setCount(mAdapter.getItemCount());

                if (mAdapter.getItemCount() > 10)
                        mRecycleView.scrollToPosition((mAdapter.getItemCount() / 2));


            }
        });
    }

    public interface injectCommands {
        String getCommands();

        void onCommandReceived(String command);
    }

    public void setCommands(injectCommands addtionCommands) {
        mAdditional = addtionCommands;

    }

    /**
     * Called when the list view adapter has been updated. Make sure we always keeps voice commands upto date.
     */
    public void reloadCommands() {
        setCommands();
    }

    private void setCommands() {
        StringBuilder builder = new StringBuilder();
        builder.append("hf_override: ");

        if (mAdapter instanceof IVoiceAdapter) {
            IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                String voiceCommand = adapter.getVoiceCommand(i);

                builder.append(voiceCommand);
                builder.append(" # ");
            }
        }

        if (mAdditional != null)
            builder.append(mAdditional.getCommands());

        mRootGroup.setContentDescription(builder.toString());

        /*mHiddenCommandLayout.removeAllViews();

        if(mAdapter instanceof IVoiceAdapter) {
            IVoiceAdapter adapter = (IVoiceAdapter)mAdapter;

            for(int i =0; i < mAdapter.getItemCount(); i++){
                HiddenTextView txt = new HiddenTextView(getContext());
                String voiceCommand = adapter.getVoiceCommand(i);
                txt.setText(voiceCommand);
                txt.setContentDescription(voiceCommand);
                txt.setTag(i);
                txt.setOnClickListener(this);

                HiddenTextView hidden = new HiddenTextView(getContext());
                String voiceCommand2 = adapter.getSecondaryCommand(getContext(), i);
                hidden.setText(voiceCommand2);
                hidden.setContentDescription(voiceCommand2);
                hidden.setTag(i);
                hidden.setOnClickListener(this);


                //mHiddenCommandLayout.addView(txt);
               //mHiddenCommandLayout.addView(hidden);
            }
        }*/

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootGroup.requestLayout();
            }
        }, 500);

    }

    @Override
    public void onClick(View v) {
        if (v instanceof HiddenTextView) {
            int index = (Integer) v.getTag();
            if (mAdapter instanceof IVoiceAdapter) {
                IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;
                adapter.clickView(getContext(), index);
            }
        }
    }

    @Override
    public void onHeadMoved(int deltaX, int deltaY) {
        //mX += (deltaX * -1) / 2;

        if (mAdapter.getItemCount() > 8) {
            if (mRecycleView != null) {
                mRecycleView.scrollBy((deltaX * -1) / 2, 0);
            }
        }
    }

    @Override
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        return false;
    }

    @Override
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        //super.sendAccessibilityEventUnchecked(event);
    }

    /////////////////////////////////////////////////////////////////////////////
    //
    // Broadcast Receiver - Get ASR Results
    //
    /////////////////////////////////////////////////////////////////////////////
    private BroadcastReceiver asrBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(ACTION_SPEECH_EVENT)) {
                final String asrCommand = intent.getStringExtra("command").trim();

                if (mAdapter instanceof IVoiceAdapter) {
                    final IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;

                    for (int i = 0; i < mAdapter.getItemCount(); i++) {
                        final String voiceCommand = adapter.getVoiceCommand(i).trim();

                        if (asrCommand.equalsIgnoreCase(voiceCommand)) {
                            adapter.clickView(getContext(), i);
                            return;
                        }
                    }
                }

                if (mAdditional != null) {
                    mAdditional.onCommandReceived(asrCommand);
                }
            }
        }
    };

}
