package com.deerweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deerweather.app.Adapter.TempAndWeatherAdapter;
import com.deerweather.app.R;
import com.deerweather.app.model.TempAndWeather;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends ActionBarActivity  implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView countyNameText;

    private List<TempAndWeather> tempAndWeatherList = new ArrayList<>();
    private TempAndWeatherAdapter adapter;

    private ListView listView;

    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) findViewById(R.id.temp_weather_list_view);
        adapter = new TempAndWeatherAdapter(WeatherActivity.this,
                R.layout.item1, tempAndWeatherList);
        listView.setAdapter(adapter);
        countyNameText = (TextView) findViewById(R.id.county_name);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        String countyCode = "CN" + getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            countyNameText.setText("同步中...");
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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
        countyNameText.setText("同步中...");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = prefs.getString("county_code", "");
        if (!TextUtils.isEmpty(countyCode)) {
            try {
                queryWeatherCode(countyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void queryWeatherCode(String countyCode){
        String httpUrl = "https://api.heweather.com/x3/weather?cityid="+countyCode+"&key=13d63a6fe83c44c897d62002f4c98551";
        try {
            queryFromServer(httpUrl);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void queryFromServer(final String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    Utility.handleFutureWeather(WeatherActivity.this, response);
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
                        countyNameText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        countyNameText.setText(prefs.getString("county_name", ""));

        TempAndWeather tempAndWeather1 = new TempAndWeather();
        tempAndWeather1.setPublishTime(prefs.getString("publish_time", "") + " 更新");
        tempAndWeather1.setNowTemp(prefs.getString("now_temp", ""));
        tempAndWeather1.setMinTemp(prefs.getString("min_temp", ""));
        tempAndWeather1.setMaxTemp(prefs.getString("max_temp", ""));
        tempAndWeatherList.clear();
        tempAndWeatherList.add(tempAndWeather1);
        TempAndWeather tempAndWeather2 = new TempAndWeather();
        tempAndWeather2.setWeatherNow(prefs.getString("now_weather", ""));
        tempAndWeatherList.add(tempAndWeather2);
        TempAndWeather tempAndWeather3 = new TempAndWeather();
        tempAndWeather3.setQlty(prefs.getString("qlty", ""));
        tempAndWeather3.setAqi(prefs.getString("aqi", ""));
        tempAndWeather3.setPm25(prefs.getString("pm25", ""));
        tempAndWeatherList.add(tempAndWeather3);
        int i;
        for (i = 1; i < 7; i++) {
            TempAndWeather tempAndWeather4 = new TempAndWeather();
            tempAndWeather4.setMinTempFuture(prefs.getString("min_temp_future" + i, ""));
            tempAndWeather4.setMaxTempFuture(prefs.getString("max_temp_future" + i, ""));
            tempAndWeather4.setWeatherFuture(prefs.getString("weather_future" + i, ""));
            tempAndWeather4.setDayFuture(prefs.getString("day_future" + i, ""));
            Log.d("day", prefs.getString("day_future" + i, "")+i);
            tempAndWeatherList.add(tempAndWeather4);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
