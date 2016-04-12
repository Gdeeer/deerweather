package com.deerweather.app.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deerweather.app.R;
import com.deerweather.app.adapter.BaseRcvAdapter;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.ItemTheme;
import com.deerweather.app.util.SharedPreferenceUtil;
import com.deerweather.app.view.MyPopWindow;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link Widget1 Widget1} AppWidget.
 */
public class Widget1ConfigureActivity extends Activity {


    private static final String PREFS_NAME = "com.deerweather.app.widget.Widget1";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String TAG = "id_widget_1";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RelativeLayout mRlWidget1Cfg;
    private TextView mTvTimeNow;
    private TextView mTvPublishTime;
    private TextView mTvCountyAndWeather;
    private TextView mTvTempNow;
    private LinearLayout mLlHourly0;
    private LinearLayout mLlHourly1;
    private LinearLayout mLlHourly2;
    private LinearLayout mLlHourly3;
    private LinearLayout mLlHourly4;
    private LinearLayout mLlHourly5;

    private DeerWeatherDB mDeerWeatherDB;
    private SharedPreferenceUtil mPrefs;
    private MyPopWindow mPopWindow;

    public Widget1ConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget1_configure);

        setResult(RESULT_CANCELED);

        mDeerWeatherDB = DeerWeatherDB.getInstance(this);
        mPrefs = new SharedPreferenceUtil(this);

        mRlWidget1Cfg = (RelativeLayout) findViewById(R.id.rl_widget1_cfg);
        mTvTimeNow = (TextView) findViewById(R.id.tv_time_now_widget_1_cfg);
        mTvTimeNow.setOnClickListener(mOnClickListener);
        mTvPublishTime = (TextView) findViewById(R.id.tv_publish_time_widget_1_cfg);
        mTvCountyAndWeather = (TextView) findViewById(R.id.tv_county_and_weather_widget_1_cfg);
        mTvTempNow = (TextView) findViewById(R.id.tv_temp_widget_1_cfg);
        mLlHourly0 = (LinearLayout) findViewById(R.id.ll_temp_hourly_0_widget_1_cfg);
        mLlHourly1 = (LinearLayout) findViewById(R.id.ll_temp_hourly_1_widget_1_cfg);
        mLlHourly2 = (LinearLayout) findViewById(R.id.ll_temp_hourly_2_widget_1_cfg);
        mLlHourly3 = (LinearLayout) findViewById(R.id.ll_temp_hourly_3_widget_1_cfg);
        mLlHourly4 = (LinearLayout) findViewById(R.id.ll_temp_hourly_4_widget_1_cfg);
        mLlHourly5 = (LinearLayout) findViewById(R.id.ll_temp_hourly_5_widget_1_cfg);

        findViewById(R.id.tv_choose_widget_1).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_done_widget_1_cfg).setOnClickListener(mOnClickListener);

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

    private void showPopWindow() {
        final ArrayList<ItemTheme> mItemThemes = (ArrayList<ItemTheme>) mDeerWeatherDB.loadMyTheme();
        mItemThemes.get(0).setSelected(true);
        mPopWindow = new MyPopWindow(this, mItemThemes, mOnClickListener,
                new BaseRcvAdapter.ItemOnClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mPrefs.setWidgetTheme(position, mAppWidgetId);
                        mLlHourly0.setBackgroundColor(mItemThemes.get(position).getColors().get(0));
                        mLlHourly1.setBackgroundColor(mItemThemes.get(position).getColors().get(1));
                        mLlHourly2.setBackgroundColor(mItemThemes.get(position).getColors().get(2));
                        mLlHourly3.setBackgroundColor(mItemThemes.get(position).getColors().get(3));
                        mLlHourly4.setBackgroundColor(mItemThemes.get(position).getColors().get(4));
                        mLlHourly5.setBackgroundColor(mItemThemes.get(position).getColors().get(5));
                        mTvTimeNow.setTextColor(mItemThemes.get(position).getColors().get(5));
                        mTvPublishTime.setTextColor(mItemThemes.get(position).getColors().get(5));
                        mTvCountyAndWeather.setTextColor(mItemThemes.get(position).getColors().get(5));
                        mTvTempNow.setTextColor(mItemThemes.get(position).getColors().get(5));
                    }
                });

        mPopWindow.showAtLocation(mRlWidget1Cfg, Gravity.BOTTOM, 0, 0);
//        mPopWindow.scrollToSelected();
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
            final Context context = Widget1ConfigureActivity.this;
            switch (v.getId()) {
                case R.id.tv_choose_widget_1:
                    if (mPopWindow == null || !mPopWindow.isShowing()) {
                        showPopWindow();
                    } else {
                        mPopWindow.showStartAnimation();
                    }
                    break;
                case R.id.tv_done_widget_1_cfg:
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    Widget1.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();
                    break;
            }
        }
    };
}
