package com.deerweather.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deerweather.app.db.DeerWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DeerWeatherDB {

    public static final String DB_NAME = "deer_weather";


    public static final int VERSION = 1;
    private static DeerWeatherDB deerWeatherDB;
    private SQLiteDatabase db;


    private DeerWeatherDB(Context context) {
        DeerWeatherOpenHelper dbHelper = new DeerWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DeerWeatherDB getInstance(Context context){
        if (deerWeatherDB == null) {
            deerWeatherDB = new DeerWeatherDB(context);
        }
        return deerWeatherDB;
    }

    public void saveProvince(Province province){
        if (province != null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            db.insert("Province", null, values);
        }
    }
    public void deleteProvinces() {
        db.delete("Province", null, null);
    }


    public List<Province> loadProvinces() {
        List<Province> list = new  ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void deleteCounties() {
        db.delete("County", null, null);
    }


    public List<City> loadCities(String provinceName) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "province_name = ?",
                new String[]{provinceName}, "city_name", null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                    city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                    city.setProvinceName(provinceName);
                    list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_code", county.getCountyCode());
            values.put("county_name", county.getCountyName());
            values.put("city_name", county.getCityName());
            values.put("province_name", county.getProvinceName());
            db.insert("County", null, values);
        }
    }

    public List<County> loadCounties(String cityName) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_name = ?",
                new String[] { String.valueOf(cityName) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCityName(cityName);
                list.add(county);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
