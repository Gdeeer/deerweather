package com.deerweather.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gdeer on 2016/3/23.
 */
public abstract class BaseRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ItemOnClickListener mItemOnClickListener;

    public interface ItemOnClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemOnClickListener(ItemOnClickListener listener) {
        this.mItemOnClickListener = listener;
    }
}
