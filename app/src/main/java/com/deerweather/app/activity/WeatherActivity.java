package com.deerweather.app.activity;

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

public class WeatherActivity extends ActionBarActivity {

    private LinearLayout weatherInfoLayout;

    private TextView countyNameText;
    private TextView publishText;
    private TextView weatherText;

    private TextView dayTempText;
    private TextView nightTempText;

    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 更新天气按钮
     */
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        countyNameText = (TextView) findViewById(R.id.county_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherText = (TextView) findViewById(R.id.weather);
        dayTempText = (TextView) findViewById(R.id.day_temp);
        nightTempText = (TextView) findViewById(R.id.night_temp);
        currentDateText = (TextView) findViewById(R.id.current_date);
        //switchCity = (Button) findViewById(R.id.switch_city);
        //refreshWeather = (Button) findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra("county_id");
        if (!TextUtils.isEmpty(countyCode)) {
        // 有县级代号时就去查询天气
            publishText.setText("syncing...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            countyNameText.setVisibility(View.INVISIBLE);
            try {
                queryWeatherCode(countyCode);
                Log.d("queryWeatherCode", "yes");
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
        // 没有县级代号时就直接显示本地天气
            showWeather();
            Log.d("show", "show");
        }
        //switchCity.setOnClickListener(this);
        //refreshWeather.setOnClickListener(this);
    }

    private void queryWeatherCode(String countyCode){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm", Locale.CHINA);
        String date = dateFormat.format(new Date());
        String appid = "9bd8f9f8e0d30ad3";
        String appid6 = "9bd8f9";
        String halfUrl = "http://open.weather.com.cn/data/" +
                "?areaid=" + countyCode +
                "&type=" + "forecast_f" +
                "&date=" + date;
        String public_key = halfUrl + "&appid=" + appid;
        String private_key = "cff4df_SmartWeatherAPI_4f7b2e9";
        try {
            String key = HttpUtil.computeKey(public_key, private_key);
            Log.d("key", key);
            String fullUrl = halfUrl + "&key=" + key;
            queryFromServer(fullUrl, "countyCode");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                    // 处理服务器返回的天气信息
                    Log.d("response", response);
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
        dayTempText.setText(prefs.getString("day_temp", ""));
        nightTempText.setText(prefs.getString("night_temp", ""));
        //weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        countyNameText.setVisibility(View.VISIBLE);
    }
}
