package com.deerweather.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.deerweather.app.adapter.TempAndWeatherAdapter;
import com.deerweather.app.R;
import com.deerweather.app.model.County;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.TempAndWeather;
import com.deerweather.app.service.AutoUpdateService;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.Utility;
import com.deerweather.app.widget.WeatherWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 100;
    private Uri mImageUri;
    private LinearLayout mLinear;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<TempAndWeather> mTempAndWeathers = new ArrayList<>();
    private TempAndWeatherAdapter mTempAndWeatherAdapter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private DeerWeatherDB mDeerWeatherDB;
    private List<County> mCounty = new ArrayList<>();
    private List<String> mSavedCounty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dawer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mLinear = (LinearLayout) findViewById(R.id.weather_all);

        SharedPreferences pref = getSharedPreferences("wallpaper_mode", MODE_PRIVATE);
        int flag = pref.getInt("mode", 1);
        if (flag == 2) {
            setWallpaper();
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("小鹿天气");
        setSupportActionBar(mToolbar);

        SharedPreferences pref2 = getSharedPreferences("first_or_not", MODE_PRIVATE);
        Boolean flag2 = pref2.getBoolean("first", true);
        if (flag2) {
            Positioning();
            SharedPreferences.Editor editor = getSharedPreferences("first_or_not", MODE_PRIVATE).edit();
            editor.putBoolean("first", false);
            editor.apply();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open, R.string.close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mNavigationView = (NavigationView) findViewById(R.id.id_navigation_view);
        onNavgationViewMenuItemSelected(mNavigationView);

        ListView listView = (ListView) findViewById(R.id.temp_weather_list_view);
        mTempAndWeatherAdapter = new TempAndWeatherAdapter(WeatherActivity.this,
                R.layout.item1, mTempAndWeathers);
        listView.setAdapter(mTempAndWeatherAdapter);

        mDeerWeatherDB = DeerWeatherDB.getInstance(this);
        String countyCodePart = getIntent().getStringExtra("county_code_part");
        if (!TextUtils.isEmpty(countyCodePart)) {
            try {
                queryCountyCode(countyCodePart);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            refresh();
            if (!flag2)
                showWeather();
        }
    }

    public void setWallpaper() {
        SharedPreferences pref = getSharedPreferences("wallpaper", MODE_PRIVATE);
        String path = pref.getString("image_path", "");
        if (!path.equals("")) {
            try {
                mImageUri = Uri.parse(path);
                InputStream inputStream = getContentResolver().openInputStream(mImageUri);
                Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                mLinear.setBackground(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                refresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 600);
    }


    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_location:
                        Positioning();
                        break;
                    case R.id.item_add:
                        mToolbar.setTitle("同步中...");
                        Intent intent = new Intent(getApplicationContext(), ChooseAreaActivity.class);
                        intent.putExtra("from_weather_activity", true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.item_delete:
                        final List<Integer> mSelectedItems = new ArrayList<>();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                        builder.setTitle("选择")
                                .setMultiChoiceItems(mSavedCounty.toArray(new CharSequence[mSavedCounty.size()]), null,
                                        new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which,
                                                                boolean isChecked) {
                                                if (isChecked) {
                                                    // If the user checked the item, add it to the selected items
                                                    mSelectedItems.add(which);
                                                } else if (mSelectedItems.contains(which)) {
                                                    // Else, if the item is already in the array, remove it
                                                    mSelectedItems.remove(Integer.valueOf(which));
                                                }
                                            }
                                        })
                                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK, so save the mSelectedItems results somewhere
                                        // or return them to the component that opened the dialog
                                        Integer n = mSelectedItems.size();
                                        for (Integer i = 0; i < n; i++) {
                                            mDeerWeatherDB.deleteMyCounties(mSavedCounty.get(mSelectedItems.get(i)));
                                        }
                                        refresh();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    case R.id.item_change:
                        final String[] wchoice = {"原装", "相册"};
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(WeatherActivity.this);
                        builder2.setTitle("选择")
                                .setItems(wchoice, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            SharedPreferences.Editor editor = getSharedPreferences("wallpaper_mode", MODE_PRIVATE).edit();
                                            editor.putInt("mode", 1);
                                            editor.apply();
                                            mLinear.setBackgroundResource(R.drawable.w);
                                        } else {
                                            SharedPreferences.Editor editor = getSharedPreferences("wallpaper_mode", MODE_PRIVATE).edit();
                                            editor.putInt("mode", 2);
                                            editor.apply();
                                            Intent intent1 = new Intent(Intent.ACTION_PICK);
                                            intent1.setType("image/*");
                                            intent1.putExtra("crop", true);
                                            intent1.putExtra("scale", true);
                                            startActivityForResult(intent1, TAKE_PHOTO);
                                        }
                                    }
                                })
                                .setCancelable(true);
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        break;
                    case R.id.item_about:
                        Intent intent2 = new Intent(WeatherActivity.this, About.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                //menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }


    public void Positioning() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST_CODE);
            return;
        }
        mToolbar.setTitle("定位中...");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*
            List<String> providerList = locationManager.getProviders(true);

            LINE ：
            什么都不开，list 含有 passive, network;
            开数据，同上；
            开wifi，同上；
            开GPS，passive, network，gps；

            COARSE ：
            什么都不开，list 含有 network;
            开数据，同上；
            开wifi，同上；
            开GPS，同上；
        */
        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            queryCountyNameBycoordinates(location);
        } else {
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
        }


        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(provider, 5000, 1000, locationListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mImageUri = data.getData();
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageUri, "image/*");
                    File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mImageUri = Uri.fromFile(outputImage);
                    intent.putExtra("scale", true);
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int w_screen = dm.widthPixels;
                    int h_screen = dm.heightPixels;
                    intent.putExtra("aspectX", w_screen);// 裁剪框比例
                    intent.putExtra("aspectY", h_screen);
                    intent.putExtra("outputX", w_screen);// 输出图片大小
                    intent.putExtra("outputY", h_screen);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                    Log.d("ppp", "ppp");
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(mImageUri);
                        Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                        mLinear.setBackground(drawable);
                        SharedPreferences.Editor editor = getSharedPreferences("wallpaper", MODE_PRIVATE).edit();
                        editor.putString("image_path", mImageUri.toString());
                        editor.apply();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(WeatherActivity.this, "获取权限失败,请手动选择。", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void refresh() {
        mToolbar.setTitle("同步中...");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = prefs.getString("county_code", "");
        if (!TextUtils.isEmpty(countyCode)) {
            try {
                queryWeatherCode(countyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void queryCountyCode(String countyCodePart) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCodePart + ".xml";
        queryFromServer(address, "countyCodePart");
    }

    private void queryCountyNameBycoordinates(Location location) {
        String address = "http://api.map.baidu.com/geocoder/v2/?ak=Xy2ajWz8vegU33RGG7U5g2ll&callback=renderReverse&location=" + location.getLatitude()
                + "," + location.getLongitude() + "&output=json&pois=1&&mcode=CA:6C:C6:A7:DE:DD:96:65:0A:E0:66:FA:66:B1:04:22:34:D7:9F:F7;com.deerweather.app";
        queryFromServer(address, "coordinates");
    }

    private void queryWeatherCode(String countyCode) {
        String httpUrl = "https://api.heweather.com/x3/weather?cityid=" + countyCode + "&key=13d63a6fe83c44c897d62002f4c98551";
        try {
            queryFromServer(httpUrl, "weather");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryWeatherCodeByname(String countyName) {
        String httpUrl = "https://api.heweather.com/x3/weather?city=" + countyName + "&key=13d63a6fe83c44c897d62002f4c98551";
        try {
            queryFromServer(httpUrl, "weather");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                if ("weather".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    Utility.handleHourlyTemp(WeatherActivity.this, response);
                    Utility.handleFutureWeather(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                } else if ("countyCodePart".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array.length == 2) {
                            String countyCode = "CN" + array[1];
                            queryWeatherCode(countyCode);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(response)) {
                        String countyName = Utility.handleNameByCoordinates(WeatherActivity.this, response);
                        try {
                            countyName = URLEncoder.encode(countyName, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        queryWeatherCodeByname(countyName);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToolbar.setTitle("同步失败");
                    }
                });
            }
        });
    }

    private void saveMyCounties() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        County county1 = new County();
        county1.setCountyName(prefs.getString("county_name", ""));
        county1.setCountyCode(prefs.getString("county_code", ""));
        if (mDeerWeatherDB.searchMyCounty(county1)) {
            Log.d("county", "executed");
            mDeerWeatherDB.saveMyCounty(county1);
        }
    }


    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mToolbar.setTitle(prefs.getString("county_name", ""));
        TempAndWeather tempAndWeather1 = new TempAndWeather();
        tempAndWeather1.setPublishTime(prefs.getString("publish_time", "") + " 更新 ");
        tempAndWeather1.setNowTemp(prefs.getString("now_temp", ""));
        tempAndWeather1.setMinTemp(prefs.getString("min_temp", ""));
        tempAndWeather1.setMaxTemp(prefs.getString("max_temp", ""));
        mTempAndWeathers.clear();
        mTempAndWeathers.add(tempAndWeather1);
        TempAndWeather tempAndWeather2 = new TempAndWeather();
        tempAndWeather2.setWeatherCode(prefs.getString("now_weather_code", ""));
        tempAndWeather2.setWeatherNow(prefs.getString("now_weather", ""));
        mTempAndWeathers.add(tempAndWeather2);
        TempAndWeather tempAndWeather3 = new TempAndWeather();
        tempAndWeather3.setQlty(prefs.getString("qlty", ""));
        tempAndWeather3.setAqi(prefs.getString("aqi", ""));
        tempAndWeather3.setPm25(prefs.getString("pm25", ""));
        mTempAndWeathers.add(tempAndWeather3);
        TempAndWeather tempAndWeather5 = new TempAndWeather();
        tempAndWeather5.setTemp4(prefs.getString("temp_4", ""));
        tempAndWeather5.setTemp7(prefs.getString("temp_7", ""));
        tempAndWeather5.setTemp10(prefs.getString("temp_10", ""));
        tempAndWeather5.setTemp13(prefs.getString("temp_13", ""));
        tempAndWeather5.setTemp16(prefs.getString("temp_16", ""));
        tempAndWeather5.setTemp19(prefs.getString("temp_19", ""));
        tempAndWeather5.setTemp22(prefs.getString("temp_22", ""));
        mTempAndWeathers.add(tempAndWeather5);
        for (int i = 1; i < 8; i++) {
            TempAndWeather tempAndWeather4 = new TempAndWeather();
            tempAndWeather4.setMinTempFuture(prefs.getString("min_temp_future" + i, ""));
            tempAndWeather4.setMaxTempFuture(prefs.getString("max_temp_future" + i, ""));
            tempAndWeather4.setWeatherFuture(prefs.getString("weather_future" + i, ""));
            tempAndWeather4.setWeatherCodeFuture(prefs.getString("weather_code_future" + i, ""));
            tempAndWeather4.setDayFuture(prefs.getString("day_future" + i, ""));
            mTempAndWeathers.add(tempAndWeather4);
        }
        mTempAndWeatherAdapter.notifyDataSetChanged();

        saveMyCounties();
        loadMyCounties();

        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);

        Intent intent1 = new Intent("com.android.CHANGE");
        sendBroadcast(intent1);
    }

    private void loadMyCounties() {
        mCounty = mDeerWeatherDB.loadMyCounties();
        final SubMenu menu = mNavigationView.getMenu().getItem(0).getSubMenu();
        menu.clear();
        mSavedCounty.clear();
        for (int i = 0; i < mCounty.size(); i++) {
            mSavedCounty.add(mCounty.get(i).getCountyName());
            menu.add(mCounty.get(i).getCountyName()).setIcon(R.drawable.round);
            MenuItem item = menu.getItem(i);
            final int finalI = i;
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mToolbar.setTitle("同步中...");
                    try {
                        queryWeatherCode(mCounty.get(finalI).getCountyCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    closeNavDrawer();
                    return true;
                }
            });
        }

        final View child = mNavigationView.getChildAt(0);
        if (child != null && child instanceof ListView) {
            final ListView menuView = (ListView) child;
            final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
            final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
            wrapped.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}