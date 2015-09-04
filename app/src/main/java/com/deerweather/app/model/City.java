package com.deerweather.app.model;

/**
 * Created by DELL-PC on 2015/6/22.
 */
public class City {
    private int id;
    private String cityName;
    private String provinceName;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
