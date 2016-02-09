package com.deerweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deerweather.app.service.AutoUpdateService;

/**
 * Created by Gdeer on 2016/2/9.
 * email: gdeer00@163.com
 */
public class AutoUpdateServiceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
