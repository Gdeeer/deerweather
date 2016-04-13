package com.deerweather.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;

import com.deerweather.app.R;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.ItemTheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Gdeer on 2016/4/1.
 */
public class OtherUtil {

    private static final String TAG = "initit";
    private static String mWeek;
    private static String[] mAllWeek = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private static SparseIntArray arrayId2Index = new SparseIntArray() {{
        append(R.id.cv_toolbar, 0);
        append(R.id.cv_temp, 1);
        append(R.id.cv_img_weather_now, 2);
        append(R.id.cv_hourly, 3);
        append(R.id.cv_weekly_info, 4);
        append(R.id.cv_weekly_chart, 5);
        append(R.id.cv_suggestion, 6);
    }};

    public static HashMap<String, String> mMapCode2Weather = new HashMap<String, String>() {
        {
            put("100", "晴");
            put("100_n", "晴");
            put("101", "多云");
            put("101_n", "多云");
            put("104", "阴");
            put("203", "风");
            put("305", "雨");
            put("313", "冻雨");
            put("400", "雪");
            put("501", "雾");
            put("502", "霾");
        }
    };

    public static String getWeatherFromCode(String code) {
        return mMapCode2Weather.get(code);
    }

    public static int getWeekToday() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        return Integer.valueOf(mWeek) - 1;
    }

    public static String getWeekOther(int futureDay) {
        return mAllWeek[(getWeekToday() + futureDay) % 7];
    }

    public static void initDatabase(Context context) {
        Log.d(TAG, "initDatabase: ");
        DeerWeatherDB mDeerWeatherDB = DeerWeatherDB.getInstance(context);
        ArrayList<Integer> colors0 = new ArrayList<Integer>() {
            {
                add(Color.argb(255, 57, 223, 255));
                add(Color.argb(255, 72, 207, 255));
                add(Color.argb(255, 87, 191, 255));
                add(Color.argb(255, 102, 175, 255));
                add(Color.argb(255, 117, 159, 255));
                add(Color.argb(255, 132, 143, 255));
                add(Color.argb(255, 147, 127, 255));
            }
        };
        ItemTheme itemTheme0 = new ItemTheme();
        itemTheme0.setColors(colors0);
        mDeerWeatherDB.saveMyTheme(itemTheme0);

        ArrayList<Integer> colors1 = new ArrayList<Integer>() {
            {
                add(-12989793);
                add(-12989793);
                add(-12989793);
                add(-12989793);
                add(-12989793);
                add(-12989793);
                add(-12989793);
            }
        };


        ItemTheme itemTheme1 = new ItemTheme();
        itemTheme1.setColors(colors1);
        mDeerWeatherDB.saveMyTheme(itemTheme1);

        ArrayList<Integer> colors2 = new ArrayList<Integer>() {
            {
                add(-5276452);
                add(-4410691);
                add(-999487);
                add(-7166055);
                add(-4948873);
                add(-6197600);
                add(-10112077);
            }
        };
        ItemTheme itemTheme2 = new ItemTheme();
        itemTheme2.setColors(colors2);
        mDeerWeatherDB.saveMyTheme(itemTheme2);
    }

    public static int getIndexFromId(int id) {
        return arrayId2Index.get(id);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static Uri savePic(Bitmap b) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);

        File dirDeerWeather = new File(Environment.getExternalStorageDirectory() + "/deerweather");
        // 如果文件不存在，则创建一个新文件
        if (!dirDeerWeather.isDirectory()) {
            try {
                dirDeerWeather.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File dirShare = new File(Environment.getExternalStorageDirectory() + "/deerweather/share");
        // 如果文件不存在，则创建一个新文件
        if (!dirShare.isDirectory()) {
            try {
                dirShare.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String timeNow = sdf.format(new Date());
        File outfile = new File(Environment.getExternalStorageDirectory() + "/deerweather/share/" + timeNow + ".png");
        // 如果文件不存在，则创建一个新文件
        try {
            if (outfile.exists()) {
                outfile.delete();
            }
            outfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri fUri = Uri.fromFile(outfile);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outfile.getPath());
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fUri;
    }
}
