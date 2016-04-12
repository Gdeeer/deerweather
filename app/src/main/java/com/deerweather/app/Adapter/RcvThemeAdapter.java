package com.deerweather.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deerweather.app.R;
import com.deerweather.app.model.ItemTheme;
import com.deerweather.app.util.OtherUtil;
import com.deerweather.app.util.ViewUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Gdeer on 2016/4/2.
 */
public class RcvThemeAdapter extends BaseRcvAdapter {

    private static final String TAG = "image_";
    private ArrayList<ItemTheme> mDataList;
    private Context mContext;
    private Bitmap mBitmap;
    private int mLastSelected = 0;

    public RcvThemeAdapter(ArrayList<ItemTheme> dataList) {
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_pop, null);
        mContext = parent.getContext();
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ThemeViewHolder holder1 = (ThemeViewHolder) holder;
        if (mDataList.get(position).isSelected()) {
            holder1.mIvSelected.setVisibility(View.VISIBLE);
            mLastSelected = position;
        } else {
            holder1.mIvSelected.setVisibility(View.GONE);
        }
        SparseArray<View> array1 = new SparseArray<View>() {
            {
                append(0, holder1.mViewPopItem0);
                append(1, holder1.mViewPopItem1);
                append(2, holder1.mViewPopItem2);
                append(3, holder1.mViewPopItem3);
                append(4, holder1.mViewPopItem4);
                append(5, holder1.mViewPopItem5);
                append(6, holder1.mViewPopItem6);
            }
        };
        for (int i = 0; i < array1.size(); i++) {
            if (!OtherUtil.isEmpty(mDataList.get(position).getPictureUrls().get(i))) {
                String path = mDataList.get(position).getPictureUrls().get(i);
                Drawable drawable = new BitmapDrawable(ViewUtil.decodeSampledBitmapFromFile(path.substring(7), 35, 23));
                array1.get(i).setBackground(drawable);
            } else {
                array1.get(i).setBackgroundColor(mDataList.get(position).getColors().get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ThemeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout mRvPopItem;
        private View mViewPopItem0;
        private View mViewPopItem1;
        private View mViewPopItem2;
        private View mViewPopItem3;
        private View mViewPopItem4;
        private View mViewPopItem5;
        private View mViewPopItem6;
        private ImageView mIvSelected;

        public ThemeViewHolder(View itemView) {
            super(itemView);

            mRvPopItem = (RelativeLayout) itemView.findViewById(R.id.rv_pop_item);
            mViewPopItem0 = itemView.findViewById(R.id.view_pop_item_0);
            mViewPopItem1 = itemView.findViewById(R.id.view_pop_item_1);
            mViewPopItem2 = itemView.findViewById(R.id.view_pop_item_2);
            mViewPopItem3 = itemView.findViewById(R.id.view_pop_item_3);
            mViewPopItem4 = itemView.findViewById(R.id.view_pop_item_4);
            mViewPopItem5 = itemView.findViewById(R.id.view_pop_item_5);
            mViewPopItem6 = itemView.findViewById(R.id.view_pop_item_6);
            mIvSelected = (ImageView) itemView.findViewById(R.id.iv_selected);

            mRvPopItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).isSelected()) {
                    mDataList.get(i).setSelected(false);
                    break;
                }
            }

            mDataList.get(getLayoutPosition()).setSelected(true);
            notifyItemChanged(getLayoutPosition());
            notifyItemChanged(mLastSelected);
            mItemOnClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public int getLastSelected() {

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).isSelected()) {
                mLastSelected = i;
                break;
            }
        }
        return mLastSelected;
    }

    public void setLastSelected(int lastSelected) {
        mLastSelected = lastSelected;
    }
}
