package com.deerweather.app.model;

/**
 * Created by DELL-PC on 2015/6/22.
 */
public class County {
    private int id;
    private String countyCode;
    private String countyName;
    private String cityName;
    private String provinceName;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }
    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
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
