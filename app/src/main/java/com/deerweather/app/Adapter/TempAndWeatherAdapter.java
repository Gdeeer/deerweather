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
    final int TYPE_5 = 4 ;

    public TempAndWeatherAdapter(Context context, int resId, List<TempAndWeather> objects) {
        super(context, resId, objects);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_1;
        else if (position == 1)
            return TYPE_2;
        else if (position == 2)
            return TYPE_3;
        else if (position == 3)
            return TYPE_5;
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
        ViewHolder5 viewHolder5 = null;
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
                case TYPE_5:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item5, null);
                    viewHolder5 = new ViewHolder5();
                    viewHolder5.temp4 = (TextView) convertView.findViewById(R.id.temp_4);
                    viewHolder5.temp7 = (TextView) convertView.findViewById(R.id.temp_7);
                    viewHolder5.temp10 = (TextView) convertView.findViewById(R.id.temp_10);
                    viewHolder5.temp13 = (TextView) convertView.findViewById(R.id.temp_13);
                    viewHolder5.temp16 = (TextView) convertView.findViewById(R.id.temp_16);
                    viewHolder5.temp19 = (TextView) convertView.findViewById(R.id.temp_19);
                    viewHolder5.temp22 = (TextView) convertView.findViewById(R.id.temp_22);
                    convertView.setTag(viewHolder5);
                    break;
                case TYPE_4:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item4, null);
                    viewHolder4 = new ViewHolder4();
                    viewHolder4.minTempFutureText = (TextView) convertView.findViewById(R.id.min_temp_future);
                    viewHolder4.maxTempFutureText = (TextView) convertView.findViewById(R.id.max_temp_future);
                    viewHolder4.weatherFutureImage = (ImageView) convertView.findViewById(R.id.weather_image_future);
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
                case TYPE_5:
                    viewHolder5 = (ViewHolder5) convertView.getTag();
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
                String id = tempAndWeather.getWeatherCode();
                switch (id){
                    case "100":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d100);
                        break;
                    case "101":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d101);
                        break;
                    case "102":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d102);
                        break;
                    case "103":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d103);
                        break;
                    case "104":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d104);
                        break;
                    case "200":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d200);
                        break;
                    case "201":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d201);
                        break;
                    case "202":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d202);
                        break;
                    case "203":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d203);
                        break;
                    case "204":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d204);
                        break;
                    case "205":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d205);
                        break;
                    case "206":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d206);
                        break;
                    case "207":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d207);
                        break;
                    case "208":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d208);
                        break;
                    case "209":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d209);
                        break;
                    case "210":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d210);
                        break;
                    case "211":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d211);
                        break;
                    case "212":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d212);
                        break;
                    case "213":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d213);
                        break;
                    case "300":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d300);
                        break;
                    case "301":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d301);
                        break;
                    case "302":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d302);
                        break;
                    case "303":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d303);
                        break;
                    case "304":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d304);
                        break;
                    case "305":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d305);
                        break;
                    case "306":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d306);
                        break;
                    case "307":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d307);
                        break;
                    case "308":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d308);
                        break;
                    case "309":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d309);
                        break;
                    case "310":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d310);
                        break;
                    case "311":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d311);
                        break;
                    case "312":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d312);
                        break;
                    case "313":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d313);
                        break;
                    case "400":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d400);
                        break;
                    case "401":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d401);
                        break;
                    case "402":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d402);
                        break;
                    case "403":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d403);
                        break;
                    case "404":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d404);
                        break;
                    case "405":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d405);
                        break;
                    case "406":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d406);
                        break;
                    case "407":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d407);
                        break;
                    case "500":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d500);
                        break;
                    case "501":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d501);
                        break;
                    case "502":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d502);
                        break;
                    case "503":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d503);
                        break;
                    case "504":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d504);
                        break;
                    case "507":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d507);
                        break;
                    case "508":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d508);
                        break;
                    case "900":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d900);
                        break;
                    case "901":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d901);
                        break;
                    case "999":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d999);
                        break;
                }
                viewHolder2.weatherNowText.setText(tempAndWeather.getWeatherNow());
                break;
            case TYPE_3:
                viewHolder3.qltyText.setText(tempAndWeather.getQlty());
                viewHolder3.aqiText.setText(tempAndWeather.getAqi());
                viewHolder3.pm25Text.setText(tempAndWeather.getPm25());
                break;
            case TYPE_5:
                viewHolder5.temp4.setText(tempAndWeather.getTemp4());
                viewHolder5.temp7.setText(tempAndWeather.getTemp7());
                viewHolder5.temp10.setText(tempAndWeather.getTemp10());
                viewHolder5.temp13.setText(tempAndWeather.getTemp13());
                viewHolder5.temp16.setText(tempAndWeather.getTemp16());
                viewHolder5.temp19.setText(tempAndWeather.getTemp19());
                viewHolder5.temp22.setText(tempAndWeather.getTemp22());
                break;
            case TYPE_4:
                viewHolder4.minTempFutureText.setText(tempAndWeather.getMinTempFuture());
                viewHolder4.maxTempFutureText.setText(tempAndWeather.getMaxTempFuture());
                String idFuture = tempAndWeather.getWeatherCodeFuture();
                switch (idFuture){
                    case "100":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d100);
                        break;
                    case "101":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d101);
                        break;
                    case "102":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d102);
                        break;
                    case "103":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d103);
                        break;
                    case "104":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d104);
                        break;
                    case "200":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d200);
                        break;
                    case "201":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d201);
                        break;
                    case "202":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d202);
                        break;
                    case "203":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d203);
                        break;
                    case "204":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d204);
                        break;
                    case "205":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d205);
                        break;
                    case "206":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d206);
                        break;
                    case "207":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d207);
                        break;
                    case "208":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d208);
                        break;
                    case "209":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d209);
                        break;
                    case "210":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d210);
                        break;
                    case "211":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d211);
                        break;
                    case "212":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d212);
                        break;
                    case "213":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d213);
                        break;
                    case "300":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d300);
                        break;
                    case "301":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d301);
                        break;
                    case "302":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d302);
                        break;
                    case "303":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d303);
                        break;
                    case "304":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d304);
                        break;
                    case "305":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d305);
                        break;
                    case "306":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d306);
                        break;
                    case "307":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d307);
                        break;
                    case "308":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d308);
                        break;
                    case "309":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d309);
                        break;
                    case "310":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d310);
                        break;
                    case "311":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d311);
                        break;
                    case "312":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d312);
                        break;
                    case "313":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d313);
                        break;
                    case "400":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d400);
                        break;
                    case "401":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d401);
                        break;
                    case "402":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d402);
                        break;
                    case "403":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d403);
                        break;
                    case "404":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d404);
                        break;
                    case "405":
                        viewHolder2.weatherImage.setImageResource(R.drawable.d405);
                        break;
                    case "406":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d406);
                        break;
                    case "407":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d407);
                        break;
                    case "500":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d500);
                        break;
                    case "501":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d501);
                        break;
                    case "502":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d502);
                        break;
                    case "503":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d503);
                        break;
                    case "504":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d504);
                        break;
                    case "507":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d507);
                        break;
                    case "508":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d508);
                        break;
                    case "900":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d900);
                        break;
                    case "901":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d901);
                        break;
                    case "999":
                        viewHolder4.weatherFutureImage.setImageResource(R.drawable.d999);
                        break;
                }
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
        ImageView weatherFutureImage;
        TextView weatherFutureText;
        TextView dayFutureText;
    }
    class ViewHolder5 {
        TextView temp4;
        TextView temp7;
        TextView temp10;
        TextView temp13;
        TextView temp16;
        TextView temp19;
        TextView temp22;
    }
}
