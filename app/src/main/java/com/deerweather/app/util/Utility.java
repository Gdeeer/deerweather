package com.deerweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.deerweather.app.model.City;
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
    public synchronized static boolean handleProvincesResponse(DeerWeatherDB deerWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    deerWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(DeerWeatherDB deerWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    deerWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }


    public static boolean handleCountiesResponse(DeerWeatherDB deerWeatherDB,
                                                 String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
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
            Log.d("countyname", response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            String aqiString = "空";
            String qlty = "空";
            String pm25 = "空";
            if (heWeatherData.has("aqi")) {
                JSONObject aqi = heWeatherData.getJSONObject("aqi");
                JSONObject city = aqi.getJSONObject("city");
                aqiString = city.getString("aqi");
                qlty = city.getString("qlty");
                pm25 = city.getString("pm25");
            }
            JSONObject basic = heWeatherData.getJSONObject("basic");
            JSONArray dailyForecast = heWeatherData.getJSONArray("daily_forecast");
            //JSONArray hourlyForecast = heWeatherData.getJSONArray("hourly_forecast");
            //String status = heWeatherData.getString("status");
            JSONObject now = heWeatherData.getJSONObject("now");


            String nowWeather = now.getJSONObject("cond").getString("txt");
            String nowTemp = now.getString("tmp");

            JSONObject todayWeather = dailyForecast.getJSONObject(0);
            String maxTempToday = todayWeather.getJSONObject("tmp").getString("max");
            String minTempToday = todayWeather.getJSONObject("tmp").getString("min");


            String countyCode = basic.getString("id");
            String countyName = basic.getString("city");
            Log.d("countyname", countyName);
            String publishTime = basic.getJSONObject("update").getString("loc");

            saveWeatherInfo(context, countyCode, countyName,publishTime,
                    nowTemp, nowWeather, maxTempToday, minTempToday,
                    aqiString, qlty, pm25);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handleFutureWeather(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            JSONArray dailyForecast = heWeatherData.getJSONArray("daily_forecast");
            int i;
            for (i = 1; i < 7; i++) {
                JSONObject weatherFutureInfo = dailyForecast.getJSONObject(i);
                String minTempFuture = weatherFutureInfo.getJSONObject("tmp").getString("min")+"~";
                String maxTempFuture = weatherFutureInfo.getJSONObject("tmp").getString("max")+"℃";
                String weatherFuture = weatherFutureInfo.getJSONObject("cond").getString("txt_d");
                String dayFuture = weatherFutureInfo.getString("date").substring(5,7)+"/"
                        + weatherFutureInfo.getString("date").substring(8,10);
                saveWeatherInfofuture(context, minTempFuture, maxTempFuture,
                        weatherFuture, dayFuture, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String countyCode, String countyName, String publishTime,
                                       String nowTemp, String nowWeather, String maxTemp, String minTemp,
                                       String aqiString, String qlty, String pm25) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("county_code", countyCode);
        editor.putString("county_name", countyName);
        Log.d("countyname", countyName);
        editor.putString("publish_time", publishTime);
        editor.putString("now_temp", nowTemp);
        editor.putString("now_weather", nowWeather);
        editor.putString("max_temp", maxTemp);
        editor.putString("min_temp", minTemp);

        editor.putString("aqi", aqiString);
        editor.putString("qlty", qlty);
        editor.putString("pm25", pm25);

        editor.apply();
    }

    public static void saveWeatherInfofuture(Context context, String minTempFuture, String maxTempFuture,
                                             String weatherFuture, String dayFuture, int i) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("min_temp_future"+i, minTempFuture);
        editor.putString("max_temp_future"+i, maxTempFuture);
        editor.putString("weather_future"+i, weatherFuture);
        editor.putString("day_future"+i, dayFuture);
        editor.apply();
    }

}
