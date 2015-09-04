package com.deerweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.deerweather.app.model.County;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Deer} on 2015/6/23.
 */
public class Utility {
    /**
     * 解析和处理文件中返回的省级数据
     */
    public static void handleProvincesResponse(DeerWeatherDB deerWeatherDB, String response) {
        deerWeatherDB.deleteProvinces();
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            for (String p : allProvinces) {
                Province province = new Province();
                province.setProvinceName(p);
                Log.d("handleProvincesResponse", province.getProvinceName());
                deerWeatherDB.saveProvince(province);
            }
        }
    }


    /**
     * 解析和处理文件中返回的县级数据
     */
    public synchronized static boolean handleCountiesResponse(DeerWeatherDB deerWeatherDB, String response) {
        deerWeatherDB.deleteCounties();
        if (!TextUtils.isEmpty(response)) {
            String[] allcounties = response.split(",\\.");
            if (allcounties.length > 0) {
                for (String c : allcounties) {
                    String[] array = c.split(",");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityName(array[2]);
                    county.setProvinceName(array[3]);
                    deerWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject countyInfo = jsonObject.getJSONObject("c");
            String countyCode = countyInfo.getString("c1");
            String countyName = countyInfo.getString("c3");
            JSONObject weatherInfoandTime = jsonObject.getJSONObject("f");
            String publishTime = weatherInfoandTime.getString("f0");
            JSONArray weatherInfo = weatherInfoandTime.getJSONArray("f1");
            String dayWeatherCode = weatherInfo.getJSONObject(0).getString("fa");
            String nightWeatherCode = weatherInfo.getJSONObject(0).getString("fb");
            String dayTemp = weatherInfo.getJSONObject(0).getString("fc");
            String nightTemp = weatherInfo.getJSONObject(0).getString("fd");

            String tomorrowDayWeatherCode = weatherInfo.getJSONObject(1).getString("fa");
            String tomorrowDayTemp = weatherInfo.getJSONObject(1).getString("fc");
            String tomorrowNightTemp = weatherInfo.getJSONObject(1).getString("fd");

            String tomorrow2DayWeatherCode = weatherInfo.getJSONObject(2).getString("fa");
            String tomorrow2DayTemp = weatherInfo.getJSONObject(2).getString("fc");
            String tomorrow2NightTemp = weatherInfo.getJSONObject(2).getString("fd");

            saveWeatherInfo(context, countyCode, countyName, dayWeatherCode, nightWeatherCode,
                    dayTemp, nightTemp, publishTime,
                    tomorrowDayTemp, tomorrowDayWeatherCode, tomorrowNightTemp,
                    tomorrow2DayTemp, tomorrow2DayWeatherCode, tomorrow2NightTemp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String countyCode, String countyName,
                                       String dayWeatherCode, String nightWeatherCode,
                                       String dayTemp, String nightTemp, String publishTime,
                                       String tomorrowDayTemp, String tomorrowDayWeatherCode,
                                       String tomorrowNightTemp,
                                       String tomorrow2DayTemp, String tomorrow2DayWeatherCode,
                                       String tomorrow2NightTemp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HHmm", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("county_code", countyCode);
        editor.putString("county_name", countyName);
        editor.putString("publish_time", publishTime);

        String hour = sdf2.format(new Date());
        if (hour.compareTo("1800") < 0) {
            editor.putString("weather_code", dayWeatherCode);
        } else {
            editor.putString("weather_code", nightWeatherCode);
        }
        editor.putString("day_temp", dayTemp);
        editor.putString("night_temp", nightTemp);

        editor.putString("tomorrow_day_temp", tomorrowDayTemp);
        editor.putString("tomorrow_night_temp", tomorrowNightTemp);
        editor.putString("tomorrow_weather_code", tomorrowDayWeatherCode);

        editor.putString("tomorrow2_day_temp", tomorrow2DayTemp);
        editor.putString("tomorrow2_night_temp", tomorrow2NightTemp);
        editor.putString("tomorrow2_weather_code", tomorrow2DayWeatherCode);

        editor.putString("current_date", sdf.format(new Date()));
        editor.apply();
    }

    public static String parsePublishTime(String publishTime) {
        String publishTime3;
        char[] array = publishTime.toCharArray();
        if (array[8] == '0') {
            publishTime3 = " 08:00 ";
        } else {
            if (array[9] == '1') {
                publishTime3 = " 11:00 ";
            } else {
                publishTime3 = " 18:00 ";
            }
        }
        return publishTime3;
    }

    public static String parseWeatherCode(String weatherCode) {
        String weather = "无";
        switch (weatherCode) {
            case "00":
                weather = "晴";
                break;
            case "01":
                weather = "多云";
                break;
            case "02":
                weather = "阴";
                break;
            case "03":
                weather = "阵雨";
                break;
            case "04":
                weather = "雷阵雨";
                break;
            case "05":
                weather = "雷阵雨伴有冰雹";
                break;
            case "06":
                weather = "雨夹雪";
                break;
            case "07":
                weather = "小雨";
                break;
            case "08":
                weather = "中雨";
                break;
            case "09":
                weather = "大雨";
                break;
            case "10":
                weather = "暴雨";
                break;
            case "11":
                weather = "大暴雨";
                break;
            case "12":
                weather = "特大暴雨";
                break;
            case "13":
                weather = "阵雪";
                break;
            case "14":
                weather = "小雪";
                break;
            case "15":
                weather = "中雪";
                break;
            case "16":
                weather = "大雪";
                break;
            case "17":
                weather = "暴雪";
                break;
            case "18":
                weather = "雾";
                break;
            case "19":
                weather = "冻雨";
                break;
            case "20":
                weather = "沙尘暴";
                break;
            case "21":
                weather = "小到中雨";
                break;
            case "22":
                weather = "中到大雨";
                break;
            case "23":
                weather = "大到暴雨";
                break;
            case "24":
                weather = "暴雨到大暴雨";
                break;
            case "25":
                weather = "大暴雨到特大暴雨";
                break;
            case "26":
                weather = "小到中雪";
                break;
            case "27":
                weather = "中到大雪";
                break;
            case "28":
                weather = "大到暴雪";
                break;
            case "29":
                weather = "浮尘";
                break;
            case "30":
                weather = "扬沙";
                break;
            case "31":
                weather = "强沙尘暴";
                break;
            case "53":
                weather = "霾";
                break;
        }
        return weather;
    }
}
