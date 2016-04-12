package com.deerweather.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.deerweather.app.R;
import com.deerweather.app.model.City;
import com.deerweather.app.model.County;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.Province;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.JSONUtility;
import com.deerweather.app.view.ColorView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DeerWeatherDB deerWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    private ColorView mCvToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mCvToolbar = (ColorView) findViewById(R.id.cv_toolbar_choose);

        setWallpaper();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_choose);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        listView = (ListView) findViewById(R.id.lv_choose);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        deerWeatherDB = DeerWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String countyCode = countyList.get(position).getCountyCode();
                    Intent intent = new Intent();
                    intent.putExtra("county_code_part", countyCode);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        queryProvinces();
    }

    public void setWallpaper() {
        String picturePath = getIntent().getStringExtra("picture");
        int color = getIntent().getIntExtra("color", 0);
        if (picturePath != null && !picturePath.equals("")) {
            Uri pictureUri = Uri.parse(picturePath);
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(pictureUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputStream, pictureUri.toString());
//            mLlToolbar.setBackground(drawable);
            mCvToolbar.setBackground(drawable);
        } else {
//            mLlToolbar.setBackgroundColor(color);
            mCvToolbar.setBackgroundColor(color);
        }
    }

    private void queryProvinces() {
        provinceList = deerWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("中国");
            }
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServerCity(null, "province");
        }
    }

    private void queryCities() {
        cityList = deerWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(selectedProvince.getProvinceName());
            }
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServerCity(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCounties() {
        countyList = deerWeatherDB.loadCounties(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(selectedCity.getCityName());
            }
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServerCity(selectedCity.getCityCode(), "county");
        }
    }

    private void queryFromServerCity(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = JSONUtility.handleProvincesResponse(deerWeatherDB, response);
                } else if ("city".equals(type)) {
                    result = JSONUtility.handleCitiesResponse(deerWeatherDB, response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = JSONUtility.handleCountiesResponse(deerWeatherDB, response, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(),
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    /**
     * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            super.onBackPressed();
        }
    }
}
