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

/**
 * Created by ${Deer} on 2015/6/23.
 */
public class JSONUtility {


    private static final String TAG = "hourlyres";

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


    public static String handleNameByLocation(Context context, String response) {
        try {
            String subResponse = response.substring(29, response.length() - 1);
            JSONObject jsonObject = new JSONObject(subResponse);
            if (jsonObject.get("status").toString().equals("0")) {
                JSONObject addressComponent = jsonObject.getJSONObject("result").getJSONObject("addressComponent");
                String address = addressComponent.getString("city");
                if (address.equals("上海市") || address.equals("北京市") || address.equals("天津市") || address.equals("重庆市"))
                    address = addressComponent.getString("district");
                address = address.substring(0, address.length() - 1);
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
            Log.d(TAG, "handleWeatherResponse: " + response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            JSONObject basic = heWeatherData.getJSONObject("basic");
            JSONArray dailyForecast = heWeatherData.getJSONArray("daily_forecast");

            JSONObject now = heWeatherData.getJSONObject("now");

            String nowWeather = now.getJSONObject("cond").getString("txt");
            String nowWeatherCode = now.getJSONObject("cond").getString("code");
            String nowTemp = now.getString("tmp");

            JSONObject todayWeather = dailyForecast.getJSONObject(0);
            String maxTempToday = todayWeather.getJSONObject("tmp").getString("max");
            String minTempToday = todayWeather.getJSONObject("tmp").getString("min");

            String countyCode = basic.getString("id");
            String countyName = basic.getString("city");
            String longitude = basic.getString("lon");
            String latitude = basic.getString("lat");
            String publishTime = basic.getJSONObject("update").getString("loc").substring(11, 16) + "  更新";

            String publishTimeHour = basic.getJSONObject("update").getString("loc").substring(11, 13);
            Log.d(TAG, "handleWeatherResponse: " + Integer.valueOf("01"));
            if (Integer.valueOf(publishTimeHour) > 18 || Integer.valueOf(publishTimeHour) < 6) {
                if (nowWeatherCode.equals("100") || nowWeatherCode.equals("101")) {
                    nowWeatherCode += "_n";
                }
            }

            saveWeatherInfo(context, countyCode, countyName, longitude, latitude, publishTime,
                    nowTemp, nowWeather, nowWeatherCode, maxTempToday, minTempToday);

            JSONObject suggestionJSON = heWeatherData.getJSONObject("suggestion");
            String comfBrf = suggestionJSON.getJSONObject("comf").getString("brf");
            String comfTxt = suggestionJSON.getJSONObject("comf").getString("txt");
            String drsgBrf = suggestionJSON.getJSONObject("drsg").getString("brf");
            String drsgTxt = suggestionJSON.getJSONObject("drsg").getString("txt");
            String fluBre = suggestionJSON.getJSONObject("flu").getString("brf");
            String fluTxt = suggestionJSON.getJSONObject("flu").getString("txt");
            String uvBrf = suggestionJSON.getJSONObject("uv").getString("brf");
            String uvTxt = suggestionJSON.getJSONObject("uv").getString("txt");
            String sportBrf = suggestionJSON.getJSONObject("sport").getString("brf");
            String sportTxt = suggestionJSON.getJSONObject("sport").getString("txt");
            String cwBrf = suggestionJSON.getJSONObject("cw").getString("brf");
            String cwTxt = suggestionJSON.getJSONObject("cw").getString("txt");

            saveSuggestion(context, comfBrf, comfTxt, drsgBrf, drsgTxt,
                    fluBre, fluTxt, uvBrf, uvTxt, sportBrf, sportTxt, cwBrf, cwTxt);

            String str = "--";
            saveAqi(context, str, str, str, str, str, str, str, str);
            if (heWeatherData.getJSONObject("aqi") != null) {
                JSONObject aqiJSON = heWeatherData.getJSONObject("aqi");
                JSONObject aqiContent = aqiJSON.getJSONObject("city");
                String aqi = aqiContent.getString("aqi");
                String qlty = aqiContent.getString("qlty");
                String pm25 = aqiContent.getString("pm25");
                String pm10 = aqiContent.getString("pm10");
                String no2 = aqiContent.getString("no2");
                String so2 = aqiContent.getString("so2");
                String co = aqiContent.getString("co");
                String o3 = aqiContent.getString("o3");
                saveAqi(context, aqi, qlty, pm25, pm10, no2, so2, co, o3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handleWeatherHourly(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject hourly = jsonObject.getJSONObject("result").getJSONObject("hourly");
            JSONObject daily = jsonObject.getJSONObject("result").getJSONObject("daily");
            JSONArray weatherInfoHourlyJSON = hourly.getJSONArray("skycon");
            JSONArray tempHourlyJSON = hourly.getJSONArray("temperature");
            JSONArray aqiHourlyJSON = hourly.getJSONArray("aqi");
            JSONArray pm25HourlyJSON = hourly.getJSONArray("pm25");
            JSONArray aqiWeeklyJSON = daily.getJSONArray("aqi");
            String description = hourly.getString("description");
            saveDescription(context, description);

            for (int i = 0; i < 24; i++) {
                String timeHourly = weatherInfoHourlyJSON.getJSONObject(i).getString("datetime").substring(11, 16);
                String weatherCodeHourlyRaw = weatherInfoHourlyJSON.getJSONObject(i).getString("value");
                String weatherCodeHourly = ViewUtil.getHourlyWeatherCode(weatherCodeHourlyRaw);
                String tempHourlyRaw = tempHourlyJSON.getJSONObject(i).getString("value");
                String tempHourly = tempHourlyRaw.substring(0, tempHourlyRaw.indexOf('.')) + "°";
                String aqiHourlyRaw = aqiHourlyJSON.getJSONObject(i).getString("value");
                String aqiHourly = aqiHourlyRaw.substring(0, aqiHourlyRaw.indexOf('.'));
                String pm25Hourly = pm25HourlyJSON.getJSONObject(i).getString("value");
                saveWeatherInfoHourly(context, timeHourly, tempHourly, weatherCodeHourly, aqiHourly, pm25Hourly, i);
            }

            for (int i = 0; i < 5; i++) {
                String aqiWeeklyRaw = aqiWeeklyJSON.getJSONObject(i).getString("avg");
                String aqiWeekly = aqiWeeklyRaw.substring(0, aqiWeeklyRaw.indexOf('.'));
                saveAqiWeekly(context, aqiWeekly, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handleWeatherWeekly(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject heWeatherData = heWeather.getJSONObject(0);
            JSONArray dailyForecast = heWeatherData.getJSONArray("daily_forecast");
            int i;
            for (i = 0; i < 7; i++) {
                JSONObject weatherFutureInfo = dailyForecast.getJSONObject(i);
                String minTempWeekly = weatherFutureInfo.getJSONObject("tmp").getString("min") + "°";
                String maxTempWeekly = weatherFutureInfo.getJSONObject("tmp").getString("max") + "°";
                String weatherWeekly = weatherFutureInfo.getJSONObject("cond").getString("txt_d");
                String weatherCodeFuture = weatherFutureInfo.getJSONObject("cond").getString("code_d");
                String dayFuture = weatherFutureInfo.getString("date").substring(5, 7) + "/"
                        + weatherFutureInfo.getString("date").substring(8, 10);
                saveWeatherInfoWeekly(context, minTempWeekly, maxTempWeekly,
                        weatherWeekly, weatherCodeFuture, dayFuture, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String countyCode, String countyName,
                                       String longitude, String latitude, String publishTime,
                                       String nowTemp, String nowWeather, String nowWeatherCode, String maxTemp, String minTemp) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("county_code", countyCode);
        editor.putString("county_name", countyName);
        editor.putString("longitude", longitude);
        editor.putString("latitude", latitude);
        editor.putString("publish_time", publishTime);
        editor.putString("temp_now", nowTemp);
        editor.putString("weather_now", nowWeather);
        editor.putString("weather_code_now", nowWeatherCode);
        editor.putString("max_temp_today", maxTemp);
        editor.putString("min_temp_today", minTemp);

        editor.apply();
    }


    public static void saveWeatherInfoHourly(Context context, String timeHourly, String tempHourly,
                                             String weatherCodeHourly, String aqiHourly, String pm25Hourly,
                                             int i) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("time_hourly" + i, timeHourly);
        editor.putString("weather_code_hourly" + i, weatherCodeHourly);
        editor.putString("temp_hourly" + i, tempHourly);
        editor.putString("aqi_hourly" + i, aqiHourly);
        editor.putString("pm25_hourly" + i, pm25Hourly);
        editor.apply();
    }

    public static void saveWeatherInfoWeekly(Context context, String minTempWeekly, String maxTempWeekly,
                                             String weatherWeekly, String weatherCodeWeekly, String dayWeekly, int i) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("min_temp_weekly" + i, minTempWeekly);
        editor.putString("max_temp_weekly" + i, maxTempWeekly);
        editor.putString("weather_code_weekly" + i, weatherCodeWeekly);
        editor.putString("weather_weekly" + i, weatherWeekly);
        editor.putString("day_weekly" + i, dayWeekly);
        editor.apply();
    }

    public static void saveDescription(Context context, String description) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("description", description);
        editor.apply();
    }

    public static void saveAqiWeekly(Context context, String aqiWeekly, int i) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("aqi_weekly" + i, aqiWeekly);
        editor.apply();
    }

    public static void saveAqi(Context context, String aqi, String qlty, String pm25,
                               String pm10, String no2, String so2, String co, String o3) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("aqi_he", aqi);
        editor.putString("qlty", qlty);
        editor.putString("pm25", pm25);
        editor.putString("pm10", pm10);
        editor.putString("no2", no2);
        editor.putString("so2", so2);
        editor.putString("co", co);
        editor.putString("o3", o3);
        editor.apply();
    }


    public static void saveSuggestion(Context context, String comfB, String comfT, String drsgB, String drsgT,
                                      String fluB, String fluT, String uvB, String uvT,
                                      String sportB, String sprotT, String cwB, String cwT) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("county_selected", true);
        editor.putString("comf_b", comfB);
        editor.putString("comf_t", comfT);
        editor.putString("drsg_b", drsgB);
        editor.putString("drsg_t", drsgT);
        editor.putString("flu_b", fluB);
        editor.putString("flu_t", fluT);
        editor.putString("uv_b", uvB);
        editor.putString("uv_t", uvT);
        editor.putString("sport_b", sportB);
        editor.putString("sport_t", sprotT);
        editor.putString("cw_b", cwB);
        editor.putString("cw_t", cwT);
        editor.apply();
    }
}