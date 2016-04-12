package com.deerweather.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.deerweather.app.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gdeer on 2016/3/31.
 */
public class ViewUtil {
    public static Map<String, Integer> map = new HashMap<String, Integer>() {
        {
            put("cloud_night", R.drawable.cloud_night);
            put("100", R.drawable.sun);
            put("100_n", R.drawable.moon);
            put("101", R.drawable.cloud_day);
            put("101_n", R.drawable.cloud_night);
            put("102", R.drawable.cloud_day);
            put("103", R.drawable.cloud_day);
            put("104", R.drawable.glum);
            put("200", R.drawable.wind);
            put("201", R.drawable.d201);
            put("202", R.drawable.wind);
            put("203", R.drawable.wind);
            put("204", R.drawable.wind);
            put("205", R.drawable.d205);
            put("206", R.drawable.d205);
            put("207", R.drawable.d205);
            put("208", R.drawable.d208);
            put("209", R.drawable.d208);
            put("210", R.drawable.d208);
            put("211", R.drawable.d208);
            put("212", R.drawable.d208);
            put("213", R.drawable.d208);
            put("300", R.drawable.rain_shower);
            put("301", R.drawable.rain_big_shower);
            put("302", R.drawable.thunder);
            put("303", R.drawable.d303);
            put("304", R.drawable.d304);
            put("305", R.drawable.rain);
            put("306", R.drawable.rain_mid);
            put("307", R.drawable.rain_big);
            put("308", R.drawable.d308);
            put("309", R.drawable.d309);
            put("310", R.drawable.d310);
            put("311", R.drawable.d311);
            put("312", R.drawable.d312);
            put("313", R.drawable.d313);
            put("400", R.drawable.d400);
            put("401", R.drawable.d401);
            put("402", R.drawable.d402);
            put("403", R.drawable.d403);
            put("404", R.drawable.d404);
            put("405", R.drawable.d405);
            put("406", R.drawable.d406);
            put("407", R.drawable.d407);
            put("501", R.drawable.fog);
            put("502", R.drawable.mai);
            put("503", R.drawable.d503);
            put("504", R.drawable.d504);
            put("507", R.drawable.d507);
            put("508", R.drawable.d508);
            put("900", R.drawable.d900);
            put("901", R.drawable.d901);
            put("999", R.drawable.d999);
        }
    };

    public static Integer getImageResource(String key) {
        return map.get(key);
    }

    public static Map<String, String> mapHourly = new HashMap<String, String>() {
        {
            put("CLEAR_DAY", "100");
            put("CLEAR_NIGHT", "100_n");
            put("PARTLY_CLOUDY_DAY", "101");
            put("PARTLY_CLOUDY_NIGHT", "101_n");
            put("CLOUDY", "104");
            put("WIND", "203");
            put("RAIN", "305");
            put("SLEET", "313");
            put("SNOW", "400");
            put("FOG", "501");
            put("HAZE", "502");
        }
    };

    public static String getHourlyWeatherCode(String key) {
        return mapHourly.get(key);
    }

    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return (int) (dpValue * dm.density + 0.5f);
    }

    public static float dip2pxfloat(Context context, float dpValue) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dpValue * dm.density;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap getBitmapByView(NestedScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
//            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }
}
