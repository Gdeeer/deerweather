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

    public static final int VERSION = 2;
    private static DeerWeatherDB deerWeatherDB;
    private SQLiteDatabase db;


    private DeerWeatherDB(Context context) {
        DeerWeatherOpenHelper dbHelper = new DeerWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DeerWeatherDB getInstance(Context context) {
        if (deerWeatherDB == null) {
            deerWeatherDB = new DeerWeatherDB(context);
        }
        return deerWeatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor
                        .getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }


    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor
                        .getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor
                        .getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }


    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }


    public void saveMyCounty(County mCounty) {
        if (mCounty != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", mCounty.getCountyName());
            values.put("county_code", mCounty.getCountyCode());
            values.put("longitude", mCounty.getLongitude());
            values.put("latitude", mCounty.getLatitude());
            db.insert("MyCounty", null, values);
        }
    }

    public boolean searchMyCounty(County mCounty) {
        Cursor cursor = db.query("MyCounty", null, null, null, null, null, null);
        int flag = 0;
        if (cursor.moveToFirst()) {
            do {
                if (mCounty.getCountyName().equals(cursor.getString(cursor.getColumnIndex("county_name"))))
                    return false;
            } while (cursor.moveToNext());
        }
        return true;
    }

    public List<County> loadMyCounties() {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("MyCounty", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setLongitude(cursor.getString(cursor
                        .getColumnIndex("longitude")));
                county.setLatitude(cursor.getString(cursor
                        .getColumnIndex("latitude")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void deleteMyCounties(String mCountyName) {
        db.delete("MyCounty", "county_name == ?", new String[]{mCountyName});
    }

//    public void updateMyCounties(String mCountyName, String longitude, String latitude) {
//        ContentValues values = new ContentValues();
//        values.put("longitude", longitude);
//        values.put("latitude", latitude);
//        db.update("MyCounty", values, "county_name = ?", new String[]{mCountyName});
//    }

    public void saveMyTheme(ItemTheme itemTheme) {
        if (itemTheme != null) {
            ContentValues values = new ContentValues();
            values.put("color_0", itemTheme.getColors().get(0));
            values.put("color_1", itemTheme.getColors().get(1));
            values.put("color_2", itemTheme.getColors().get(2));
            values.put("color_3", itemTheme.getColors().get(3));
            values.put("color_4", itemTheme.getColors().get(4));
            values.put("color_5", itemTheme.getColors().get(5));
            values.put("color_6", itemTheme.getColors().get(6));

            if (itemTheme.getPictureUrls() != null) {
                values.put("picture_url_0", itemTheme.getPictureUrls().get(0));
                values.put("picture_url_1", itemTheme.getPictureUrls().get(1));
                values.put("picture_url_2", itemTheme.getPictureUrls().get(2));
                values.put("picture_url_3", itemTheme.getPictureUrls().get(3));
                values.put("picture_url_4", itemTheme.getPictureUrls().get(4));
                values.put("picture_url_5", itemTheme.getPictureUrls().get(5));
                values.put("picture_url_6", itemTheme.getPictureUrls().get(6));
            }
            db.insert("MyTheme", null, values);
        }
    }

    public List<ItemTheme> loadMyTheme() {
        List<ItemTheme> list = new ArrayList<>();
        Cursor cursor = db.query("MyTheme", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                Integer id = cursor.getInt(cursor.getColumnIndex("id"));

                Integer color0 = cursor.getInt(cursor.getColumnIndex("color_0"));
                Integer color1 = cursor.getInt(cursor.getColumnIndex("color_1"));
                Integer color2 = cursor.getInt(cursor.getColumnIndex("color_2"));
                Integer color3 = cursor.getInt(cursor.getColumnIndex("color_3"));
                Integer color4 = cursor.getInt(cursor.getColumnIndex("color_4"));
                Integer color5 = cursor.getInt(cursor.getColumnIndex("color_5"));
                Integer color6 = cursor.getInt(cursor.getColumnIndex("color_6"));
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(color0);
                colors.add(color1);
                colors.add(color2);
                colors.add(color3);
                colors.add(color4);
                colors.add(color5);
                colors.add(color6);

                String pictureUrl0 = cursor.getString(cursor.getColumnIndex("picture_url_0"));
                String pictureUrl1 = cursor.getString(cursor.getColumnIndex("picture_url_1"));
                String pictureUrl2 = cursor.getString(cursor.getColumnIndex("picture_url_2"));
                String pictureUrl3 = cursor.getString(cursor.getColumnIndex("picture_url_3"));
                String pictureUrl4 = cursor.getString(cursor.getColumnIndex("picture_url_4"));
                String pictureUrl5 = cursor.getString(cursor.getColumnIndex("picture_url_5"));
                String pictureUrl6 = cursor.getString(cursor.getColumnIndex("picture_url_6"));
                ArrayList<String> pictureUrls = new ArrayList<>();
                pictureUrls.add(pictureUrl0);
                pictureUrls.add(pictureUrl1);
                pictureUrls.add(pictureUrl2);
                pictureUrls.add(pictureUrl3);
                pictureUrls.add(pictureUrl4);
                pictureUrls.add(pictureUrl5);
                pictureUrls.add(pictureUrl6);

                ItemTheme itemTheme = new ItemTheme();
                itemTheme.setId(id);
                itemTheme.setColors(colors);
                itemTheme.setPictureUrls(pictureUrls);
                list.add(itemTheme);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void updateMyTheme(ItemTheme itemTheme) {
        ContentValues values = new ContentValues();
        values.put("color_0", itemTheme.getColors().get(0));
        values.put("color_1", itemTheme.getColors().get(1));
        values.put("color_2", itemTheme.getColors().get(2));
        values.put("color_3", itemTheme.getColors().get(3));
        values.put("color_4", itemTheme.getColors().get(4));
        values.put("color_5", itemTheme.getColors().get(5));
        values.put("color_6", itemTheme.getColors().get(6));

        if (itemTheme.getPictureUrls() != null) {
            values.put("picture_url_0", itemTheme.getPictureUrls().get(0));
            values.put("picture_url_1", itemTheme.getPictureUrls().get(1));
            values.put("picture_url_2", itemTheme.getPictureUrls().get(2));
            values.put("picture_url_3", itemTheme.getPictureUrls().get(3));
            values.put("picture_url_4", itemTheme.getPictureUrls().get(4));
            values.put("picture_url_5", itemTheme.getPictureUrls().get(5));
            values.put("picture_url_6", itemTheme.getPictureUrls().get(6));
        }
        db.update("MyTheme", values, "id = ?", new String[]{String.valueOf(itemTheme.getId())});
    }
}