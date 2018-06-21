/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */

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
 * Scrollable list view for viewing items on the HMT-1
 */
public class WearableListView extends RelativeLayout implements View.OnClickListener, HFHeadtrackerListener {
    private static final String NO_SCROLL = "hf_scroll_none";

    private static final String ACTION_SPEECH_EVENT = "com.realwear.wearhf.intent.action.SPEECH_EVENT";
    private static final String EXTRA_INDEX = "com.realwear.wearhf.intent.extra.INDEX";

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;

    private HFHeadtrackerManager mHeadTracker;
    private View mRootGroup;

    private RecyclerViewMargin mDecoration;
    private Handler mHandler;
    private InjectCommands mAdditional;

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
        View view = inflater.inflate(R.layout.custom_recycleview, this);

        mRecycleView = (RecyclerView) view.findViewById(R.id.list2);
        mRecycleView.setContentDescription(NO_SCROLL);

        mDecoration = new RecyclerViewMargin(250, 1);
        mRecycleView.addItemDecoration(mDecoration);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //updateScroll();
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
            int lastCount = -1;

            @Override
            public void onChanged() {
                super.onChanged();
                setCommands();

                mDecoration.setCount(mAdapter.getItemCount());

                if (lastCount != mAdapter.getItemCount()) {
                    lastCount = mAdapter.getItemCount();

                    if (mAdapter.getItemCount() > 6) {
                        mRecycleView.scrollToPosition((mAdapter.getItemCount() / 2) - 3);
                        mRecycleView.post(new Runnable() {
                            @Override
                            public void run() {
                                View initial = mRecycleView.getChildAt(0);
                                if (initial != null) {
                                    mRecycleView.scrollBy(initial.getWidth() / 2, 0);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void setCommands(InjectCommands additionalCommands) {
        mAdditional = additionalCommands;
    }

    /**
     * Called when the list view adapter has been updated. Make sure we always keeps voice commands upto date.
     */
    public void reloadCommands() {
        setCommands();
    }

    /**
     * Set up all the speech commands for the list view
     */
    private void setCommands() {
        StringBuilder builder = new StringBuilder();
        builder.append("hf_override:");

        if (mAdapter instanceof IVoiceAdapter) {
            IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                final String voiceCommand = adapter.getVoiceCommand(i);
                if (voiceCommand == null || voiceCommand.isEmpty()) {
                    continue;
                }

                // The # indicates that the command has an associated 'Select Item <n>' command.
                builder.append('#');
                builder.append(voiceCommand);
                builder.append("|");
            }
        }

        if (mAdditional != null) {
            builder.append(mAdditional.getCommands());
        }

        mRootGroup.setContentDescription(builder.toString());

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootGroup.requestLayout();
            }
        }, 500);
    }

    @Override
    public void onClick(View view) {
        if (!(view instanceof HiddenTextView)) {
            return;
        }

        final int index = (Integer) view.getTag();

        if (mAdapter instanceof IVoiceAdapter) {
            IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;
            adapter.selectItem(getContext(), index);
        }
    }

    @Override
    public void onHeadMoved(float deltaX, float deltaY) {
        //mX += (deltaX * -1) / 2;

        if (mAdapter.getItemCount() > 6) {
            if (mRecycleView != null) {
                mRecycleView.scrollBy((int) ((deltaX * -1) / 2), 0);
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

    /**
     * Receives broadcasts for ASR.
     */
    private BroadcastReceiver asrBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(ACTION_SPEECH_EVENT)) {
                return;
            }

            final String asrCommand = intent.getStringExtra("command").trim();

            if (mAdapter instanceof IVoiceAdapter) {
                final IVoiceAdapter adapter = (IVoiceAdapter) mAdapter;

                int index = intent.getIntExtra(EXTRA_INDEX, -1);
                if (index >= 0 && index < mAdapter.getItemCount()) {
                    adapter.selectItem(getContext(), index);
                } else {
                    for (int i = 0; i < mAdapter.getItemCount(); i++) {
                        final String voiceCommand = adapter.getVoiceCommand(i);

                        if (voiceCommand != null &&
                                asrCommand.equalsIgnoreCase(voiceCommand.trim())) {
                            adapter.selectItem(getContext(), i);
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

    public interface InjectCommands {
        String getCommands();

        void onCommandReceived(String command);
    }
}
