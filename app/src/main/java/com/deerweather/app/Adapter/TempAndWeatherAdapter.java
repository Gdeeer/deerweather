package com.deerweather.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deerweather.app.R;
import com.deerweather.app.model.TempAndWeather;

import org.w3c.dom.Text;

import java.util.List;

public class TempAndWeatherAdapter extends ArrayAdapter<TempAndWeather> {

    final int TYPE_1 = 0 ;
    final int TYPE_2 = 1 ;
    final int TYPE_3 = 2 ;
    final int TYPE_4 = 3 ;

    public TempAndWeatherAdapter(Context context, int resId, List<TempAndWeather> objects) {
        super(context, resId, objects);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_1;
        else if (position == 1)
            return TYPE_2;
        else if (position == 2)
            return TYPE_3;
        else
            return TYPE_4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TempAndWeather tempAndWeather = getItem(position);
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        ViewHolder3 viewHolder3 = null;
        ViewHolder4 viewHolder4 = null;
        int type = getItemViewType(position);
        if(convertView == null || convertView.getTag()==null) {
            switch (type) {
                case TYPE_1:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item1, null);
                    viewHolder1 = new ViewHolder1();
                    viewHolder1.publishText = (TextView) convertView.findViewById(R.id.publish_text);
                    viewHolder1.nowTempText = (TextView) convertView.findViewById(R.id.now_temp);
                    viewHolder1.minTempText = (TextView) convertView.findViewById(R.id.min_temp);
                    viewHolder1.maxTempText = (TextView) convertView.findViewById(R.id.max_temp);
                    convertView.setTag(viewHolder1);
                    break;
                case TYPE_2:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item2, null);
                    viewHolder2 = new ViewHolder2();
                    viewHolder2.weatherImage = (ImageView) convertView.findViewById(R.id.image);
                    viewHolder2.weatherNowText = (TextView) convertView.findViewById(R.id.weather_now);
                    convertView.setTag(viewHolder2);
                    break;
                case TYPE_3:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item3, null);
                    viewHolder3 = new ViewHolder3();
                    viewHolder3.qltyText = (TextView) convertView.findViewById(R.id.qlty);
                    viewHolder3.aqiText = (TextView) convertView.findViewById(R.id.aqi);
                    viewHolder3.pm25Text = (TextView) convertView.findViewById(R.id.pm25);
                    convertView.setTag(viewHolder3);
                    break;
                case TYPE_4:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item4, null);
                    viewHolder4 = new ViewHolder4();
                    viewHolder4.minTempFutureText = (TextView) convertView.findViewById(R.id.min_temp_future);
                    viewHolder4.maxTempFutureText = (TextView) convertView.findViewById(R.id.max_temp_future);
                    viewHolder4.weatherFutureText = (TextView) convertView.findViewById(R.id.weather_future);
                    viewHolder4.dayFutureText = (TextView) convertView.findViewById(R.id.day_future);
                    convertView.setTag(viewHolder4);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                    break;
                case TYPE_3:
                    viewHolder3 = (ViewHolder3) convertView.getTag();
                    break;
                case TYPE_4:
                    viewHolder4 = (ViewHolder4) convertView.getTag();
                    break;
            }
        }

        switch(type) {
            case TYPE_1:
                viewHolder1.publishText.setText(tempAndWeather.getPublishTime());
                viewHolder1.nowTempText.setText(tempAndWeather.getNowTemp());
                viewHolder1.minTempText.setText(tempAndWeather.getMinTemp());
                viewHolder1.maxTempText.setText(tempAndWeather.getMaxTemp());
                break;
            case TYPE_2:
                //viewHolder2.weatherImage.setImageResource(tempAndWeather.getWeatherCode());
                viewHolder2.weatherNowText.setText(tempAndWeather.getWeatherNow());
                break;
            case TYPE_3:
                viewHolder3.qltyText.setText(tempAndWeather.getQlty());
                viewHolder3.aqiText.setText(tempAndWeather.getAqi());
                viewHolder3.pm25Text.setText(tempAndWeather.getPm25());
                break;
            case TYPE_4:
                viewHolder4.minTempFutureText.setText(tempAndWeather.getMinTempFuture());
                viewHolder4.maxTempFutureText.setText(tempAndWeather.getMaxTempFuture());
                viewHolder4.weatherFutureText.setText(tempAndWeather.getWeatherFuture());
                viewHolder4.dayFutureText.setText(tempAndWeather.getDayFuture());
                break;
        }

        return convertView;
    }

    class ViewHolder1 {
        TextView publishText;
        TextView nowTempText;
        TextView minTempText;
        TextView maxTempText;
    }
    class ViewHolder2 {
        ImageView weatherImage;
        TextView weatherNowText;
    }
    class ViewHolder3 {
        TextView qltyText;
        TextView aqiText;
        TextView pm25Text;
    }
    class ViewHolder4 {
        TextView minTempFutureText;
        TextView maxTempFutureText;
        TextView weatherFutureText;
        TextView dayFutureText;
    }
}
