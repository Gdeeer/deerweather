package com.deerweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL-PC on 2015/6/22.
 */
public class DeerWeatherOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";

    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";

    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";

    public static final String CREATE_MY_COUNTY = "create table MyCounty ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "longitude text, "
            + "latitude text)";

    public static final String CREATE_MY_THEME = "create table MyTheme ("
            + "id integer primary key autoincrement, "
            + "color_0 integer, "
            + "color_1 integer, "
            + "color_2 integer, "
            + "color_3 integer, "
            + "color_4 integer, "
            + "color_5 integer, "
            + "color_6 integer, "
            + "color_7 integer, "
            + "color_8 integer, "
            + "color_9 integer, "
            + "picture_url_0  text, "
            + "picture_url_1  text, "
            + "picture_url_2  text, "
            + "picture_url_3  text, "
            + "picture_url_4  text, "
            + "picture_url_5  text, "
            + "picture_url_6  text, "
            + "picture_url_7  text, "
            + "picture_url_8  text, "
            + "picture_url_9  text) ";

    public DeerWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_COUNTY);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_MY_COUNTY);
        db.execSQL(CREATE_MY_THEME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL(CREATE_MY_THEME);
        }
//        onCreate(db);
    }
}
