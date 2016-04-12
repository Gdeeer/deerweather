package com.deerweather.app.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deerweather.app.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * The configuration screen for the {@link Widget2 Widget2} AppWidget.
 */
public class Widget2ConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.deerweather.app.widget.Widget2";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String TAG = "id_widget_2";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private int mColor;
    private RelativeLayout mRlLeft;
    private RelativeLayout mRlRight;
    private TextView mTvDay0;
    private TextView mTvDay1;
    private TextView mTvDay2;
    private TextView mTvDay3;
    private TextView mTvDay4;
    private TextView mTvTemp0;
    private TextView mTvTemp1;
    private TextView mTvTemp2;
    private TextView mTvTemp3;
    private TextView mTvTemp4;
    private TextView mTvWeather0;
    private TextView mTvWeather1;
    private TextView mTvWeather2;
    private TextView mTvWeather3;
    private TextView mTvWeather4;
    private ArrayList<TextView> mAlTextView = new ArrayList<>();

    public Widget2ConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget2_configure);

        findViewById(R.id.tv_no_bg).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_done_widget_2_cfg).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_change).setOnClickListener(mOnClickListener);

        mRlLeft = (RelativeLayout) findViewById(R.id.rl_widget2_left_cfg);
        mRlRight = (RelativeLayout) findViewById(R.id.rl_widget2_right_cfg);

        mTvDay0 = (TextView) findViewById(R.id.tv_day_0_cfg);
        mTvDay1 = (TextView) findViewById(R.id.tv_day_1_cfg);
        mTvDay2 = (TextView) findViewById(R.id.tv_day_2_cfg);
        mTvDay3 = (TextView) findViewById(R.id.tv_day_3_cfg);
        mTvDay4 = (TextView) findViewById(R.id.tv_day_4_cfg);
        mTvTemp0 = (TextView) findViewById(R.id.tv_temp_day_0_cfg);
        mTvTemp1 = (TextView) findViewById(R.id.tv_temp_day_1_cfg);
        mTvTemp2 = (TextView) findViewById(R.id.tv_temp_day_2_cfg);
        mTvTemp3 = (TextView) findViewById(R.id.tv_temp_day_3_cfg);
        mTvTemp4 = (TextView) findViewById(R.id.tv_temp_day_4_cfg);

        mTvWeather0 = (TextView) findViewById(R.id.tv_weather_day_0_cfg);
        mTvWeather1 = (TextView) findViewById(R.id.tv_weather_day_1_cfg);
        mTvWeather2 = (TextView) findViewById(R.id.tv_weather_day_2_cfg);
        mTvWeather3 = (TextView) findViewById(R.id.tv_weather_day_3_cfg);
        mTvWeather4 = (TextView) findViewById(R.id.tv_weather_day_4_cfg);

        mAlTextView.add(mTvDay0);
        mAlTextView.add(mTvDay1);
        mAlTextView.add(mTvDay2);
        mAlTextView.add(mTvDay3);
        mAlTextView.add(mTvDay4);
        mAlTextView.add(mTvTemp0);
        mAlTextView.add(mTvTemp1);
        mAlTextView.add(mTvTemp2);
        mAlTextView.add(mTvTemp3);
        mAlTextView.add(mTvTemp4);
        mAlTextView.add(mTvWeather0);
        mAlTextView.add(mTvWeather1);
        mAlTextView.add(mTvWeather2);
        mAlTextView.add(mTvWeather3);
        mAlTextView.add(mTvWeather4);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d(TAG, "onCreate: " + mAppWidgetId);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    static int loadColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
    }

    static void saveColorPref(Context context, int appWidgetId, int color) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, color);
        prefs.apply();
    }

    static void deleteColorPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    static boolean loadFlagDefalut(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean("default" + appWidgetId, true);
    }

    static void saveFlagDefalut(Context context, int appWidgetId, boolean flag) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean("default" + appWidgetId, flag);
        prefs.apply();
    }

    static void deleteFlagDefault(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove("default" + appWidgetId);
        prefs.apply();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = Widget2ConfigureActivity.this;
            switch (v.getId()) {
                case R.id.tv_done_widget_2_cfg:
                    saveColorPref(context, mAppWidgetId, mColor);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    Widget2.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();
                    break;
                case R.id.tv_change:
                    saveFlagDefalut(context, mAppWidgetId, false);
                    Random rnd = new Random();
                    mColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    mRlLeft.setBackgroundColor(mColor);
                    mRlRight.setBackgroundColor(Color.WHITE);
                    for (int i = 0; i < mAlTextView.size(); i++) {
                        mAlTextView.get(i).setTextColor(mColor);
                    }
                    break;
                case R.id.tv_no_bg:
                    saveFlagDefalut(context, mAppWidgetId, false);
                    mColor = 0;
                    mRlLeft.setBackgroundColor(mColor);
                    mRlRight.setBackgroundColor(mColor);
                    for (int i = 0; i < mAlTextView.size(); i++) {
                        mAlTextView.get(i).setTextColor(Color.WHITE);
                    }
                    break;
            }
        }
    };
}