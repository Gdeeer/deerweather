package com.deerweather.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.deerweather.app.R;
import com.deerweather.app.activity.WeatherActivity;
import com.deerweather.app.util.Utility;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    public static final String TAG = "widdd";
    public static final String CHANGE_ACTION = "com.android.CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CHANGE_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            Log.d(TAG, "onReceive: from_activity");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String id = prefs.getString("now_weather_code", "100");
            String weather = prefs.getString("now_weather", "晴");
            String temp = prefs.getString("now_temp", "9");
            String county = prefs.getString("county_name", "没找到");
            remoteViews.setImageViewResource(R.id.widget_image, Utility.map.get(id));
            remoteViews.setTextViewText(R.id.widget_county, county);
            remoteViews.setTextViewText(R.id.widget_temp, temp);
            remoteViews.setTextViewText(R.id.widget_weather, weather);
            appWidgetManager.updateAppWidget(new ComponentName(context, WeatherWidget.class), remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String id = prefs.getString("now_weather_code", "100");
        String weather = prefs.getString("now_weather", "晴");
        String temp = prefs.getString("now_temp", "9");
        String county = prefs.getString("county_name", "上海");
//            views.setImageViewUri(R.id.widget_image, Uri.parse("android.resource://"+ packageName +"/"+ R.drawable.id));
        remoteViews.setImageViewResource(R.id.widget_image, Utility.map.get(id));
        remoteViews.setTextViewText(R.id.widget_county, county);
        remoteViews.setTextViewText(R.id.widget_temp, temp);
        remoteViews.setTextViewText(R.id.widget_weather, weather);
        Intent intentClick = new Intent(context, WeatherActivity.class);
        intentClick.putExtra("click", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_image, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_county, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_temp, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_weather, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_temp_du, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
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