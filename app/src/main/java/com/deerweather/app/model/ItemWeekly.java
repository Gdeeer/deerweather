package com.deerweather.app.model;

/**
 * Created by Gdeer on 2016/3/30.
 */
public class ItemWeekly {

    private String mDayWeekly;
    private String mWeatherCodeWeekly;
    private String mWeatherWeekly;
    private String mAqiWeekly;
    private String mMaxTempWeekly;
    private String mMinTempWeekly;


    public String getMinTempWeekly() {
        return mMinTempWeekly;
    }

    public void setMinTempWeekly(String minTempWeekly) {
        mMinTempWeekly = minTempWeekly;
    }

    public String getMaxTempWeekly() {
        return mMaxTempWeekly;
    }

    public void setMaxTempWeekly(String maxTempWeekly) {
        mMaxTempWeekly = maxTempWeekly;
    }

    public String getWeatherCodeWeekly() {
        return mWeatherCodeWeekly;
    }

    public void setWeatherCodeWeekly(String weatherCodeWeekly) {
        mWeatherCodeWeekly = weatherCodeWeekly;
    }

    public String getWeatherWeekly() {
        return mWeatherWeekly;
    }

    public void setWeatherWeekly(String weatherWeekly) {
        mWeatherWeekly = weatherWeekly;
    }

    public String getDayWeekly() {
        return mDayWeekly;
    }

    public void setDayWeekly(String dayWeekly) {
        mDayWeekly = dayWeekly;
    }

    public String getAqiWeekly() {
        return mAqiWeekly;
    }

    public void setAqiWeekly(String aqiWeekly) {
        mAqiWeekly = aqiWeekly;
    }
}
