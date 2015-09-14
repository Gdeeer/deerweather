package com.deerweather.app.model;


public class TempAndWeather {
    private String publishTime;
    private String nowTemp;
    private String minTemp;
    private String maxTemp;

    private String weatherCode;

    private String weatherNow;
    private String aqi;
    private String qlty;
    private String pm25;
    private String minTempFuture;
    private String maxTempFuture;
    private String weatherFuture;
    private String dayFuture;

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    public void setNowTemp(String nowTemp) {
        this.nowTemp = nowTemp;
    }
    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }
    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }
    public void setWeatherNow(String weatherNow) {
        this.weatherNow = weatherNow;
    }
    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
    public void setQlty(String qlty) {
        this.qlty = qlty;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public void setMinTempFuture(String min) {
        this.minTempFuture = min;
    }
    public void setMaxTempFuture(String max) {
        this.maxTempFuture = max;
    }
    public void setWeatherFuture(String weather) {
        this.weatherFuture = weather;
    }
    public void setDayFuture(String day) {
        this.dayFuture = day;
    }

    public String getPublishTime() {
        return publishTime;
    }
    public String getNowTemp() {
        return nowTemp;
    }
    public String getMinTemp() {
        return minTemp;
    }
    public String getMaxTemp() {
        return maxTemp;
    }
    public String getWeatherCode() {
        return weatherCode;
    }
    public String getWeatherNow() {
        return weatherNow;
    }
    public String getAqi() {
        return aqi;
    }
    public String getQlty() {
        return qlty;
    }
    public String getPm25() {
        return pm25;
    }
    public String getMinTempFuture() {
        return minTempFuture;
    }
    public String getMaxTempFuture() {
        return maxTempFuture;
    }
    public String getWeatherFuture() {
        return weatherFuture;
    }
    public String getDayFuture() {
        return dayFuture;
    }


}
