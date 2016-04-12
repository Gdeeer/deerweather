package com.deerweather.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deerweather.app.R;
import com.deerweather.app.model.ItemWeekly;
import com.deerweather.app.util.OtherUtil;
import com.deerweather.app.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by Gdeer on 2016/3/30.
 */
public class RcvWeeklyAdapter extends BaseRcvAdapter {

    private ArrayList<ItemWeekly> mDataList = new ArrayList<>();

    public RcvWeeklyAdapter(ArrayList<ItemWeekly> dataList) {
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_rcv_weekly, null);
        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final WeeklyViewHolder viewHolder = (WeeklyViewHolder) holder;
        if (position == 0) {
            viewHolder.mTvDayWeekly.setText("今天");
        } else if (position == 1) {
            viewHolder.mTvDayWeekly.setText("明天");
        } else if (position == 2) {
            viewHolder.mTvDayWeekly.setText("后天");
        } else {
            viewHolder.mTvDayWeekly.setText(OtherUtil.getWeekOther(position));
        }

        viewHolder.mTvWeatherWeekly.setText(mDataList.get(position).getWeatherWeekly());
        if (mDataList.get(position).getWeatherCodeWeekly() != null && !mDataList.get(position).getWeatherCodeWeekly().equals("")) {
            viewHolder.mIvWeatherWeekly.setImageResource(ViewUtil.getImageResource(mDataList.get(position).getWeatherCodeWeekly()));
        }
        if (position == 0) {
            viewHolder.mTvAqiWeekly.setText("AQI " + mDataList.get(position).getAqiWeekly());
        } else {
            viewHolder.mTvAqiWeekly.setText(mDataList.get(position).getAqiWeekly());
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class WeeklyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvDayWeekly;
        private ImageView mIvWeatherWeekly;
        private TextView mTvWeatherWeekly;
        private TextView mTvAqiWeekly;

        public WeeklyViewHolder(View itemView) {
            super(itemView);
            mTvDayWeekly = (TextView) itemView.findViewById(R.id.tv_day_weekly);
            mIvWeatherWeekly = (ImageView) itemView.findViewById(R.id.iv_weather_weekly);
            mTvWeatherWeekly = (TextView) itemView.findViewById(R.id.tv_weather_weekly);
            mTvAqiWeekly = (TextView) itemView.findViewById(R.id.tv_aqi_weekly);
        }
    }
}
