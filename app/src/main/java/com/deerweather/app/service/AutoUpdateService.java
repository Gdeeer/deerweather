package com.deerweather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.deerweather.app.activity.WeatherActivity;
import com.deerweather.app.receiver.AutoUpdateServiceReceiver;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anhour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anhour;
        Intent i = new Intent(this, AutoUpdateServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = prefs.getString("county_code", "");
        String address = "https://api.heweather.com/x3/weather?cityid=" + countyCode + "&key=13d63a6fe83c44c897d62002f4c98551";
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                    Utility.handleWeatherResponse(AutoUpdateService.this, response);
                    Utility.handleHourlyTemp(AutoUpdateService.this, response);
                    Utility.handleFutureWeather(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}