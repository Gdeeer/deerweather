package com.deerweather.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deerweather.app.R;
import com.deerweather.app.model.ItemHourly;
import com.deerweather.app.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by Gdeer on 2016/3/29.
 */
public class RcvHourlyAdapter extends BaseRcvAdapter {

    private static final String TAG = "hourly";
    private ArrayList<ItemHourly> mDataList = new ArrayList<>();

    public RcvHourlyAdapter(ArrayList<ItemHourly> dataList) {
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View vItem = View.inflate(parent.getContext(), R.layout.item_rcv_hourly, null);
        View vItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcv_hourly, parent, false);
//        if (vItem.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
        vItem.getLayoutParams().width = (parent.getWidth() - 21) / 7;
        return new HourlyViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HourlyViewHolder viewHolder = (HourlyViewHolder) holder;
        viewHolder.mTvTimeHourly.setText(mDataList.get(position).getTimeHourly());
        viewHolder.mTempHourly.setText(mDataList.get(position).getTempHourly());
        Log.d(TAG, "onBindViewHolder: " + ViewUtil.getHourlyWeatherCode(
                mDataList.get(position).getWeatherCodeHourly()));
        if (mDataList.get(position).getWeatherCodeHourly() != null && !mDataList.get(position).getWeatherCodeHourly().equals("")) {
            viewHolder.mIvWeatherHourly.setImageResource(ViewUtil.getImageResource(mDataList.get(position).getWeatherCodeHourly()));
        }
        if (position == 0) {
            viewHolder.mTvAqiHourly.setText("AQI " + mDataList.get(position).getAqiHourly());
        } else {
            viewHolder.mTvAqiHourly.setText(mDataList.get(position).getAqiHourly());
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class HourlyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTimeHourly;
        private ImageView mIvWeatherHourly;
        private TextView mTempHourly;
        private TextView mTvAqiHourly;

        public HourlyViewHolder(View itemView) {
            super(itemView);
            mTvTimeHourly = (TextView) itemView.findViewById(R.id.tv_time_hourly);
            mIvWeatherHourly = (ImageView) itemView.findViewById(R.id.iv_weather_hourly);
            mTempHourly = (TextView) itemView.findViewById(R.id.tv_temp_hourly);
            mTvAqiHourly = (TextView) itemView.findViewById(R.id.tv_aqi_hourly);
        }
    }
}
