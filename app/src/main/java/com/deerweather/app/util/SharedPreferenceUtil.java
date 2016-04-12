package com.deerweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Gdeer on 2016/3/30.
 */
public class SharedPreferenceUtil {
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public SharedPreferenceUtil(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void apply() {
        mEditor.apply();
    }

    public void setTempNow(String tempNow) {
        mEditor.putString("temp_now", tempNow);
    }

    public String getTempNow() {
        return mPrefs.getString("temp_now", "--");
    }

    public void setPublishTime(String publishTime) {
        mEditor.putString("publish_time", publishTime);
    }

    public String getPublishTime() {
        return mPrefs.getString("publish_time", "--");
    }

    public void setWeatherCodeNow(String weatherCodeNow) {
        mEditor.putString("weather_code_now", weatherCodeNow);
    }

    public String getWeatherCodeNow() {
        return mPrefs.getString("weather_code_now", "100");
    }

    public void setWeatherNow(String weatherNow) {
        mEditor.putString("weather_now", weatherNow);
    }

    public String getWeatherNow() {
        return mPrefs.getString("weather_now", "--");
    }

    public String getCountyName() {
        return mPrefs.getString("county_name", "--");
    }

    public String getCountyCode() {
        return mPrefs.getString("county_code", "--");
    }

    public String getLongitude() {
        return mPrefs.getString("longitude", "");
    }

    public String getLatitude() {
        return mPrefs.getString("latitude", "");
    }

    public String getMinTempWeekly(int i) {
        return mPrefs.getString("min_temp_weekly" + i, "");
    }

    public String getMaxTempWeekly(int i) {
        return mPrefs.getString("max_temp_weekly" + i, "");
    }

    public String getWeatherWeekly(int i) {
        return mPrefs.getString("weather_weekly" + i, "--");
    }

    public String getWeatherCodeWeekly(int i) {
        return mPrefs.getString("weather_code_weekly" + i, "100");
    }

    public String getMaxTempToday() {
        return mPrefs.getString("max_temp_today", "--");
    }

    public String getMinTempToday() {
        return mPrefs.getString("min_temp_today", "--");
    }

    public String getTimeHourly(int i) {
        return mPrefs.getString("time_hourly" + i, "--");
    }

    public String getWeatherCodeHourly(int i) {
        return mPrefs.getString("weather_code_hourly" + i, "100");
    }

    public String getTempHourly(int i) {
        return mPrefs.getString("temp_hourly" + i, "--");
    }

    public String getAqiHourly(int i) {
        return mPrefs.getString("aqi_hourly" + i, "--");
    }

    public String getPm25Hourly(int i) {
        return mPrefs.getString("pm25_hourly" + i, "--");
    }

    public String getAqiWeekly(int i) {
        return mPrefs.getString("aqi_weekly" + i, "--");
    }

    public void setThemeNow(int themeNow) {
        mEditor.putInt("theme_now", themeNow);
        mEditor.apply();
    }

    public int getThemeNow() {
        return mPrefs.getInt("theme_now", 0);
    }

    public void setWidgetTheme(int themeNow, int appId) {
        mEditor.putInt("widget_theme_" + appId, themeNow);
        mEditor.apply();
    }

    public int getWidgetTheme(int appId) {
        return mPrefs.getInt("widget_theme_" + appId, 0);
    }

    public void setPictureUrl(String url, int i) {
        mEditor.putString("picture" + i, url);
        mEditor.apply();
    }

    public String getPictureUrl(int i) {
        return mPrefs.getString("picture" + i, "");
    }

    public String getAqiNow() {
        if (!mPrefs.getString("aqi_hourly" + 0, "--").equals("--")) {
            return "AQI " + mPrefs.getString("aqi_hourly" + 0, "--") + " >";
        } else {
            return mPrefs.getString("aqi_he" + 0, "--");
        }
    }


    public String getAqiDialog() {
        if (!mPrefs.getString("aqi_hourly" + 0, "--").equals("--")) {
            return mPrefs.getString("aqi_hourly" + 0, "--");
        } else {
            return mPrefs.getString("aqi_he" + 0, "--");
        }
    }


    public String getQlty() {
        return mPrefs.getString("qlty", "--");
    }

    public String getPm25() {
        return mPrefs.getString("pm25", "--");
    }

    public String getPm10() {
        return mPrefs.getString("pm10", "--");
    }

    public String getNo2() {
        return mPrefs.getString("no2", "--");
    }

    public String getSo2() {
        return mPrefs.getString("so2", "--");
    }

    public String getCo() {
        return mPrefs.getString("co", "--");
    }

    public String getO3() {
        return mPrefs.getString("o3", "--");
    }

    public String getComfB() {
        return mPrefs.getString("comf_b", "--");
    }

    public String getComfT() {
        return mPrefs.getString("comf_t", "--");
    }

    public String getDrsgB() {
        return mPrefs.getString("drsg_b", "--");
    }

    public String getDrsgT() {
        return mPrefs.getString("drsg_t", "--");
    }

    public String getFluB() {
        return mPrefs.getString("flu_b", "--");
    }

    public String getfluT() {
        return mPrefs.getString("flu_t", "--");
    }

    public String getUvB() {
        return mPrefs.getString("uv_b", "--");
    }

    public String getUvT() {
        return mPrefs.getString("uv_t", "--");
    }

    public String getSportB() {
        return mPrefs.getString("sport_b", "--");
    }

    public String getSportT() {
        return mPrefs.getString("sport_t", "--");
    }

    public String getCwB() {
        return mPrefs.getString("cw_b", "--");
    }

    public String getCwT() {
        return mPrefs.getString("cw_t", "--");
    }
}
