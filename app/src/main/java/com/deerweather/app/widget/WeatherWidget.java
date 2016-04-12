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
import com.deerweather.app.util.JSONUtility;
import com.deerweather.app.util.SharedPreferenceUtil;
import com.deerweather.app.util.ViewUtil;

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
            SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);
            setView(remoteViews, mPrefs);

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

        SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);

        setView(remoteViews, mPrefs);

        Intent intentClick = new Intent(context, WeatherActivity.class);
//        intentClick.putExtra("click", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_image, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_county, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_temp, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_weather, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void setView(RemoteViews views, SharedPreferenceUtil prefs) {
        String code = prefs.getWeatherCodeNow();
        String weather = prefs.getWeatherNow();
        String temp = prefs.getTempNow() + "°";
        String county = prefs.getCountyName();

        views.setImageViewResource(R.id.widget_image, ViewUtil.getImageResource(code));
        views.setTextViewText(R.id.widget_county, county);
        views.setTextViewText(R.id.widget_temp, temp);
        views.setTextViewText(R.id.widget_weather, weather);
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