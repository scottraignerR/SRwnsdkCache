package com.core.realwear.sdk;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Luke on 26/01/2017.
 */

public class RecyclerViewMargin extends RecyclerView.ItemDecoration {
    private final int columns;
    private int margin;
    private int count;

    /**
     * constructor
     *
     * @param margin  desirable margin size in px between the views in the recyclerView
     * @param columns number of columns of the RecyclerView
     */
    public RecyclerViewMargin(@IntRange(from = 0) int margin, @IntRange(from = 0) int columns) {
        this.margin = margin;
        this.columns = columns;

    }

    public void setCount(int count){
        this.count = count;
    }


    /**
     * Set different margins for the items inside the recyclerView: no top margin for the first row
     * and no left margin for the first column.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);



        if(count <= 10)
            margin = -5;

        if(count <= 6)
            margin = 30;

        if(count <= 4)
            margin = 50;

        int dobotth = count % 2;
        if(position == 0 || position == 1){
            outRect.left = margin;
        }else if(position == count - 1 || (dobotth == 0 && position == count - 2)){
            outRect.right = margin;
        }
    }
}