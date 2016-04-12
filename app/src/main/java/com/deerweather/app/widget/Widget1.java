package com.deerweather.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.deerweather.app.R;
import com.deerweather.app.activity.WeatherActivity;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.ItemTheme;
import com.deerweather.app.service.AutoUpdateService;
import com.deerweather.app.util.OtherUtil;
import com.deerweather.app.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class Widget1 extends AppWidgetProvider {


    public static final String CHANGE_ACTION = "com.android.CHANGE";
    public static final String CHANGE_TIME_ACTION = "com.android.CHANGE_TIME";
    private static final String TAG = "time_send";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CHANGE_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_1);
            SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);
            setView(views, mPrefs);
            appWidgetManager.updateAppWidget(new ComponentName(context, Widget1.class), views);
        } else if (intent.getAction().equals(CHANGE_TIME_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_1);
            SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);
            setView(views, mPrefs);
            setTimeNow(views);
            appWidgetManager.updateAppWidget(new ComponentName(context, Widget1.class), views);
            Intent intent1 = new Intent(context, AutoUpdateService.class);
            context.startService(intent1);
            Log.d(TAG, "onReceive: " + "receive_send_again");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_1);

        DeerWeatherDB deerWeatherDB = DeerWeatherDB.getInstance(context);
        SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);
        int mTheme = mPrefs.getWidgetTheme(appWidgetId);
        final ArrayList<ItemTheme> itemThemes = (ArrayList<ItemTheme>) deerWeatherDB.loadMyTheme();

        views.setInt(R.id.ll_temp_hourly_0_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(0));
        views.setInt(R.id.ll_temp_hourly_1_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(1));
        views.setInt(R.id.ll_temp_hourly_2_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(2));
        views.setInt(R.id.ll_temp_hourly_3_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(3));
        views.setInt(R.id.ll_temp_hourly_4_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(4));
        views.setInt(R.id.ll_temp_hourly_5_widget_1, "setBackgroundColor", itemThemes.get(mTheme).getColors().get(5));
        views.setTextColor(R.id.tv_time_now_widget_1, itemThemes.get(mTheme).getColors().get(5));
        views.setTextColor(R.id.tv_publish_time_widget_1, itemThemes.get(mTheme).getColors().get(5));
        views.setTextColor(R.id.tv_county_and_weather_widget_1, itemThemes.get(mTheme).getColors().get(5));
        views.setTextColor(R.id.tv_temp_widget_1, itemThemes.get(mTheme).getColors().get(5));

        // data
        setView(views, mPrefs);

        Intent intentClick = new Intent(context, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
        views.setOnClickPendingIntent(R.id.ll_widget_1, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void setTimeNow(RemoteViews views) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        views.setTextViewText(R.id.tv_time_now_widget_1, str);
    }

    public static void setView(RemoteViews views, SharedPreferenceUtil prefs) {

        setTimeNow(views);

        views.setTextViewText(R.id.tv_publish_time_widget_1, prefs.getPublishTime());
        views.setTextViewText(R.id.tv_county_and_weather_widget_1, prefs.getCountyName() + " | " + prefs.getWeatherNow());
        views.setTextViewText(R.id.tv_temp_widget_1, prefs.getTempNow() + "°");

        views.setTextViewText(R.id.tv_time_hourly_0_widget_1, prefs.getTimeHourly(0));
        views.setTextViewText(R.id.tv_time_hourly_1_widget_1, prefs.getTimeHourly(1));
        views.setTextViewText(R.id.tv_time_hourly_2_widget_1, prefs.getTimeHourly(2));
        views.setTextViewText(R.id.tv_time_hourly_3_widget_1, prefs.getTimeHourly(3));
        views.setTextViewText(R.id.tv_time_hourly_4_widget_1, prefs.getTimeHourly(4));
        views.setTextViewText(R.id.tv_time_hourly_5_widget_1, prefs.getTimeHourly(5));

        views.setTextViewText(R.id.tv_temp_hourly_0_widget_1, prefs.getTempHourly(0));
        views.setTextViewText(R.id.tv_temp_hourly_1_widget_1, prefs.getTempHourly(1));
        views.setTextViewText(R.id.tv_temp_hourly_2_widget_1, prefs.getTempHourly(2));
        views.setTextViewText(R.id.tv_temp_hourly_3_widget_1, prefs.getTempHourly(3));
        views.setTextViewText(R.id.tv_temp_hourly_4_widget_1, prefs.getTempHourly(4));
        views.setTextViewText(R.id.tv_temp_hourly_5_widget_1, prefs.getTempHourly(5));

        views.setTextViewText(R.id.tv_weather_hourly_0_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(0)));
        views.setTextViewText(R.id.tv_weather_hourly_1_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(1)));
        views.setTextViewText(R.id.tv_weather_hourly_2_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(2)));
        views.setTextViewText(R.id.tv_weather_hourly_3_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(3)));
        views.setTextViewText(R.id.tv_weather_hourly_4_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(4)));
        views.setTextViewText(R.id.tv_weather_hourly_5_widget_1, OtherUtil.getWeatherFromCode(prefs.getWeatherCodeHourly(5)));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

