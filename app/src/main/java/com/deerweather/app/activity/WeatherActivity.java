package com.deerweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deerweather.app.R;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends ActionBarActivity  implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;

    private TextView countyNameText;
    private TextView publishText;
    private TextView weatherText;

    private TextView dayTempText;
    private TextView nightTempText;

    private TextView currentDateText;

    private TextView wave;
    private TextView oc;

    private Button switchCity;
    private Button refreshWeather;

    private TextView tomorrowDayTempText;
    private TextView tomorrowNightTempText;
    private TextView tomorrowWeatherText;


    private TextView tomorrow2DayTempText;
    private TextView tomorrow2NightTempText;
    private TextView tomorrow2WeatherText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        countyNameText = (TextView) findViewById(R.id.county_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherText = (TextView) findViewById(R.id.weather);
        dayTempText = (TextView) findViewById(R.id.day_temp);
        nightTempText = (TextView) findViewById(R.id.night_temp);
        currentDateText = (TextView) findViewById(R.id.current_date);
        wave = (TextView) findViewById(R.id.wave);
        oc =(TextView) findViewById(R.id.oc);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        tomorrowDayTempText = (TextView) findViewById(R.id.tomorrow_day_temp);
        tomorrowNightTempText = (TextView) findViewById(R.id.tomorrow_night_temp);
        tomorrowWeatherText = (TextView) findViewById(R.id.tomorrow_weather);
        tomorrow2DayTempText = (TextView) findViewById(R.id.tomorrow2_day_temp);
        tomorrow2NightTempText = (TextView) findViewById(R.id.tomorrow2_night_temp);
        tomorrow2WeatherText = (TextView) findViewById(R.id.tomorrow2_weather);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            countyNameText.setVisibility(View.INVISIBLE);
            try {
                queryWeatherCode(countyCode);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            showWeather();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
            case R.id.refresh_weather:
                refresh();
                break;
            default:
                break;
        }
    }

    private void refresh () {
        publishText.setText("同步中...");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = prefs.getString("county_code", "");
        Log.d("Response", countyCode);
        if (!TextUtils.isEmpty(countyCode)) {
            try {
                queryWeatherCode(countyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void queryWeatherCode(String countyCode){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm", Locale.CHINA);
        String date = dateFormat.format(new Date());
        String appid = "9bd8f9f8e0d30ad3";
        String appid6 = "9bd8f9";
        String halfUrl = "http://open.weather.com.cn/data/" +
                "?areaid=" + countyCode +
                "&type=" + "forecast_v" +
                "&date=" + date;
        String public_key = halfUrl + "&appid=" + appid;
        String private_key = "cff4df_SmartWeatherAPI_4f7b2e9";
        try {
            String key = HttpUtil.computeKey(public_key, private_key);
            String fullUrl = halfUrl + "&appid=" + appid6 + "&key=" + key;
            queryFromServer(fullUrl);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        countyNameText.setText(prefs.getString("county_name", ""));
        if(TextUtils.isEmpty(prefs.getString("day_temp", ""))){
            dayTempText.setVisibility(View.GONE);
            wave.setVisibility(View.GONE);
            oc.setVisibility(View.GONE);
        } else {
            dayTempText.setVisibility(View.VISIBLE);
            wave.setVisibility(View.VISIBLE);
            oc.setVisibility(View.VISIBLE);
        }
        dayTempText.setText(prefs.getString("day_temp", ""));
        nightTempText.setText(prefs.getString("night_temp", ""));
        weatherText.setText(Utility.parseWeatherCode(prefs.getString("weather_code", "")));

        tomorrowDayTempText.setText(prefs.getString("tomorrow_day_temp", ""));
        tomorrowNightTempText.setText(prefs.getString("tomorrow_night_temp", ""));
        tomorrowWeatherText.setText(Utility.parseWeatherCode(prefs.getString("tomorrow_weather_code", "")));

        tomorrow2DayTempText.setText(prefs.getString("tomorrow2_day_temp", ""));
        tomorrow2NightTempText.setText(prefs.getString("tomorrow2_night_temp", ""));
        tomorrow2WeatherText.setText(Utility.parseWeatherCode(prefs.getString("tomorrow2_weather_code", "")));

        publishText.setText("今天" + Utility.parsePublishTime(prefs.getString("publish_time", "")) + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        countyNameText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
