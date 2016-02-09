package com.deerweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.deerweather.app.R;
import com.deerweather.app.model.City;
import com.deerweather.app.model.County;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Deer} on 2015/6/23.
 */
public class Utility {

    public static Map<String, Integer> map = new HashMap<String, Integer>(){
        {
            put("100", R.drawable.d100);
            put("101", R.drawable.d101);
            put("102", R.drawable.d102);
            put("103", R.drawable.d103);
            put("104", R.drawable.d104);
            put("200", R.drawable.d200);
            put("201", R.drawable.d201);
            put("202", R.drawable.d200);
            put("203", R.drawable.d200);
            put("204", R.drawable.d200);
            put("205", R.drawable.d205);
            put("206", R.drawable.d205);
            put("207", R.drawable.d205);
            put("208", R.drawable.d208);
            put("209", R.drawable.d208);
            put("210", R.drawable.d208);
            put("211", R.drawable.d208);
            put("212", R.drawable.d208);
            put("213", R.drawable.d208);
            put("300", R.drawable.d300);
            put("301", R.drawable.d301);
            put("302", R.drawable.d302);
            put("303", R.drawable.d303);
            put("304", R.drawable.d304);
            put("305", R.drawable.d305);
            put("306", R.drawable.d306);
            put("307", R.drawable.d307);
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
            put("501", R.drawable.d501);
            put("502", R.drawable.d502);
            put("503", R.drawable.d503);
            put("504", R.drawable.d504);
            put("507", R.drawable.d507);
            put("508", R.drawable.d508);
            put("900", R.drawable.d900);
            put("901", R.drawable.d901);
            put("999", R.drawable.d999);
        }
    };


    /**
     * 解析和处理文件中返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(DeerWeatherDB deerWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces.length > 0) {
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
            if (allCities.length > 0) {
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
            if (allCounties.length > 0) {
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


    public static String handleNameByCoordinates(Context context, String response) {
        try {
            String subResponse = response.substring(29, response.length() - 1);
            JSONObject jsonObject = new JSONObject(subResponse);
            if (jsonObject.get("status").toString().equals("0")) {
                JSONObject addressComponent = jsonObject.getJSONObject("result").getJSONObject("addressComponent");
                String address = addressComponent.getString("city");
                if (address.equals("上海市") || address.equals("北京市") || address.equals("天津市") || address.equals("重庆市"))
                    address = addressComponent.getString("district");
                int leng = address.length();
                address = address.substring(0, leng - 1);
                Log.d("address", address);
                return address;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("address3", response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            String aqiString = "";
            String qlty = "";
            String pm25 = "";
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
            String nowWeatherCode = now.getJSONObject("cond").getString("code");
            String nowTemp = now.getString("tmp");

            JSONObject todayWeather = dailyForecast.getJSONObject(0);
            String maxTempToday = todayWeather.getJSONObject("tmp").getString("max");
            String minTempToday = todayWeather.getJSONObject("tmp").getString("min");


            String countyCode = basic.getString("id");
            String countyName = basic.getString("city");
            String publishTime = "今天" + basic.getJSONObject("update").getString("loc").substring(10, 16);

            saveWeatherInfo(context, countyCode, countyName, publishTime,
                    nowTemp, nowWeather, nowWeatherCode, maxTempToday, minTempToday,
                    aqiString, qlty, pm25);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handleHourlyTemp(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            JSONArray dailyForecast = heWeatherData.getJSONArray("hourly_forecast");
            int n = dailyForecast.length() - 1;
            Log.d("temp", "hh" + n);
            String[] temp = new String[7];
            int i, j;
            for (j = 6, i = n; j >= 0; j--, i--) {
                if (i > 0)
                    temp[j] = dailyForecast.getJSONObject(i).getString("tmp") + "°";
                else
                    temp[j] = "0";
            }
            saveHourlyTemp(context, temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]);
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
                String minTempFuture = weatherFutureInfo.getJSONObject("tmp").getString("min") + "~";
                String maxTempFuture = weatherFutureInfo.getJSONObject("tmp").getString("max") + "℃";
                String weatherFuture = weatherFutureInfo.getJSONObject("cond").getString("txt_d");
                String weatherCodeFuture = weatherFutureInfo.getJSONObject("cond").getString("code_d");
                String dayFuture = weatherFutureInfo.getString("date").substring(5, 7) + "/"
                        + weatherFutureInfo.getString("date").substring(8, 10);
                saveWeatherInfofuture(context, minTempFuture, maxTempFuture,
                        weatherFuture, weatherCodeFuture, dayFuture, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String countyCode, String countyName, String publishTime,
                                       String nowTemp, String nowWeather, String nowWeatherCode, String maxTemp, String minTemp,
                                       String aqiString, String qlty, String pm25) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("county_code", countyCode);
        editor.putString("county_name", countyName);
        editor.putString("publish_time", publishTime);
        editor.putString("now_temp", nowTemp);
        editor.putString("now_weather", nowWeather);
        editor.putString("now_weather_code", nowWeatherCode);
        editor.putString("max_temp", maxTemp);
        editor.putString("min_temp", minTemp);

        editor.putString("aqi", aqiString);
        editor.putString("qlty", qlty);
        editor.putString("pm25", pm25);

        editor.apply();
    }

    public static void saveHourlyTemp(Context context, String temp4, String temp7, String temp10, String temp13, String temp16, String temp19, String temp22) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("temp_4", temp4);
        editor.putString("temp_7", temp7);
        editor.putString("temp_10", temp10);
        editor.putString("temp_13", temp13);
        editor.putString("temp_16", temp16);
        editor.putString("temp_19", temp19);
        editor.putString("temp_22", temp22);
        editor.apply();
    }

    public static void saveWeatherInfofuture(Context context, String minTempFuture, String maxTempFuture,
                                             String weatherFuture, String weatherCodeFuture, String dayFuture, int i) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("min_temp_future" + i, minTempFuture);
        editor.putString("max_temp_future" + i, maxTempFuture);
        editor.putString("weather_code_future" + i, weatherCodeFuture);
        editor.putString("weather_future" + i, weatherFuture);
        editor.putString("day_future" + i, dayFuture);
        editor.apply();
    }
}
