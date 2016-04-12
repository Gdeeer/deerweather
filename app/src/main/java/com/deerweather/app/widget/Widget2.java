package com.deerweather.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import com.deerweather.app.R;
import com.deerweather.app.activity.WeatherActivity;
import com.deerweather.app.util.OtherUtil;
import com.deerweather.app.util.SharedPreferenceUtil;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link Widget2ConfigureActivity Widget2ConfigureActivity}
 */
public class Widget2 extends AppWidgetProvider {

    private static final String TAG = "mycolor";
    public static final String CHANGE_ACTION = "com.android.CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CHANGE_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_2);
            SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);
            setView(views, mPrefs);
            appWidgetManager.updateAppWidget(new ComponentName(context, Widget2.class), views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferenceUtil mPrefs = new SharedPreferenceUtil(context);

        int color = Widget2ConfigureActivity.loadColorPref(context, appWidgetId);
        boolean isDefault = Widget2ConfigureActivity.loadFlagDefalut(context, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_2);

        if (!isDefault) {
            views.setInt(R.id.rl_widget2_left, "setBackgroundColor", color);
            if (color == 0) {
                Log.d(TAG, "updateAppWidget: " + color + " " + Color.WHITE);
                views.setInt(R.id.rl_widget2_right, "setBackgroundColor", color);
                color = Color.WHITE;
            }
            views.setTextColor(R.id.tv_day_0, color);
            views.setTextColor(R.id.tv_day_1, color);
            views.setTextColor(R.id.tv_day_2, color);
            views.setTextColor(R.id.tv_day_3, color);
            views.setTextColor(R.id.tv_day_4, color);
            views.setTextColor(R.id.tv_temp_day_0, color);
            views.setTextColor(R.id.tv_temp_day_1, color);
            views.setTextColor(R.id.tv_temp_day_2, color);
            views.setTextColor(R.id.tv_temp_day_3, color);
            views.setTextColor(R.id.tv_temp_day_4, color);
            views.setTextColor(R.id.tv_weather_day_0, color);
            views.setTextColor(R.id.tv_weather_day_1, color);
            views.setTextColor(R.id.tv_weather_day_2, color);
            views.setTextColor(R.id.tv_weather_day_3, color);
            views.setTextColor(R.id.tv_weather_day_4, color);
        }

        setView(views, mPrefs);

        Intent intentClick = new Intent(context, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, 0);
        views.setOnClickPendingIntent(R.id.ll_widget_2, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void setView(RemoteViews views, SharedPreferenceUtil prefs) {
        views.setTextViewText(R.id.tv_temp_now_widget_2, prefs.getTempNow() + "°");
        views.setTextViewText(R.id.tv_weather_now_widget_2, prefs.getWeatherNow());
        views.setTextViewText(R.id.tv_county_widget_2, prefs.getCountyName());
        views.setTextViewText(R.id.tv_min_temp_today_widget_2, "L " + prefs.getMinTempToday() + "°");
        views.setTextViewText(R.id.tv_max_temp_today_widget_2, "H " + prefs.getMaxTempToday() + "°");
        views.setTextViewText(R.id.tv_day_0, "明天");
        views.setTextViewText(R.id.tv_day_1, "后天");
        views.setTextViewText(R.id.tv_day_2, OtherUtil.getWeekOther(3));
        views.setTextViewText(R.id.tv_day_3, OtherUtil.getWeekOther(4));
        views.setTextViewText(R.id.tv_day_4, OtherUtil.getWeekOther(5));
        views.setTextViewText(R.id.tv_weather_day_0, prefs.getWeatherWeekly(0));
        views.setTextViewText(R.id.tv_weather_day_1, prefs.getWeatherWeekly(1));
        views.setTextViewText(R.id.tv_weather_day_2, prefs.getWeatherWeekly(2));
        views.setTextViewText(R.id.tv_weather_day_3, prefs.getWeatherWeekly(3));
        views.setTextViewText(R.id.tv_weather_day_4, prefs.getWeatherWeekly(4));
        views.setTextViewText(R.id.tv_temp_day_0, prefs.getMinTempWeekly(0) +"/"+prefs.getMaxTempWeekly(0));
        views.setTextViewText(R.id.tv_temp_day_1, prefs.getMinTempWeekly(1) +"/"+prefs.getMaxTempWeekly(1));
        views.setTextViewText(R.id.tv_temp_day_2, prefs.getMinTempWeekly(2) +"/"+prefs.getMaxTempWeekly(2));
        views.setTextViewText(R.id.tv_temp_day_3, prefs.getMinTempWeekly(3) +"/"+prefs.getMaxTempWeekly(3));
        views.setTextViewText(R.id.tv_temp_day_4, prefs.getMinTempWeekly(4) +"/"+prefs.getMaxTempWeekly(4));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            Widget2ConfigureActivity.deleteColorPref(context, appWidgetId);
        }
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

