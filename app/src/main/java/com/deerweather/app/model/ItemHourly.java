package com.deerweather.app.model;

/**
 * Created by Gdeer on 2016/3/30.
 */
public class ItemHourly {
    private String mDescription;
    private String mTimeHourly;
    private String mWeatherCodeHourly;
    private String mTempHourly;
    private String mAqiHourly;
    private String mPm25Hourly;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTimeHourly() {
        return mTimeHourly;
    }

    public void setTimeHourly(String timeHourly) {
        mTimeHourly = timeHourly;
    }

    public String getWeatherCodeHourly() {
        return mWeatherCodeHourly;
    }

    public void setWeatherCodeHourly(String weatherCodeHourly) {
        mWeatherCodeHourly = weatherCodeHourly;
    }

    public String getTempHourly() {
        return mTempHourly;
    }

    public void setTempHourly(String tempHourly) {
        mTempHourly = tempHourly;
    }

    public String getAqiHourly() {
        return mAqiHourly;
    }

    public void setAqiHourly(String aqiHourly) {
        mAqiHourly = aqiHourly;
    }

    public String getPm25Hourly() {
        return mPm25Hourly;
    }

    public void setPm25Hourly(String pm25Hourly) {
        mPm25Hourly = pm25Hourly;
    }
}
