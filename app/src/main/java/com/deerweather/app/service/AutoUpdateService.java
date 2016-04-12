package com.deerweather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class AutoUpdateService extends Service {

    public static final String CHANGE_ACTION = "com.android.CHANGE";
    public static final String CHANGE_TIME_ACTION = "com.android.CHANGE_TIME";
    private static final String TAG = "time_send";

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 发时钟广播
        int anMinute = 60 * 1000;
        long triggerAtTime2 = SystemClock.elapsedRealtime() + anMinute;
        Intent intent2 = new Intent(CHANGE_TIME_ACTION);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime2, pendingIntent2);
        Log.d(TAG, "onStartCommand: " + "send");

        // 发天气广播
        int anHour = 60 * 60 * 1000;
        long triggerAtTime3 = SystemClock.elapsedRealtime() + anHour;
        Intent intent3 = new Intent(CHANGE_ACTION);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 0, intent3, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime3, pendingIntent3);

        return super.onStartCommand(intent, flags, startId);
    }
}