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
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deerweather.app.R;
import com.deerweather.app.adapter.BaseRcvAdapter;
import com.deerweather.app.adapter.RcvHourlyAdapter;
import com.deerweather.app.adapter.RcvWeeklyAdapter;
import com.deerweather.app.model.County;
import com.deerweather.app.model.DeerWeatherDB;
import com.deerweather.app.model.ItemHourly;
import com.deerweather.app.model.ItemTheme;
import com.deerweather.app.model.ItemWeekly;
import com.deerweather.app.service.AutoUpdateService;
import com.deerweather.app.util.HttpCallBackListener;
import com.deerweather.app.util.HttpUtil;
import com.deerweather.app.util.JSONUtility;
import com.deerweather.app.util.OtherUtil;
import com.deerweather.app.util.SharedPreferenceUtil;
import com.deerweather.app.util.ViewUtil;
import com.deerweather.app.view.ColorView;
import com.deerweather.app.view.LineGraphicView;
import com.deerweather.app.view.MyLinearLayout;
import com.deerweather.app.view.MyPopWindow;
import com.deerweather.app.view.MyRecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WeatherActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener,
        View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = "touch_";
    public static final int TAKE_PHOTO = 111;
    public static final int CROP_PHOTO = 112;
    public static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 100;
    private static final int CHOOSE_COUNTY = 113;
    private Uri mImageUri;

    private NestedScrollView mNsvMain;
    private SwipeRefreshLayout mSwipeRefresh;

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private DeerWeatherDB mDeerWeatherDB;
    private List<County> mCounty = new ArrayList<>();
    private List<String> mSavedCountyName = new ArrayList<>();

    private TextView mTvNowTemp;
    private TextView mTvNowAqi;
    private TextView mTvPublishTime;
    private ImageView mIvNowWeatherImg;
    private TextView mTvNowWeather;

    private MyRecyclerView mRcvHourly;
    private ArrayList<ItemHourly> mHourlyDataList = new ArrayList<>();
    private RcvHourlyAdapter mHourlyAdapter;

    private MyRecyclerView mRcvWeekly;
    private ArrayList<ItemWeekly> mWeeklyDataList = new ArrayList<>();
    private RcvWeeklyAdapter mWeeklyAdapter;

    private LineGraphicView mLineWeekly;
    private ArrayList<Integer> mLineDataHigh = new ArrayList<>();
    private ArrayList<Integer> mLineDataLow = new ArrayList<>();

    private static final int LONG_DURATION = 1500;
    private static final int SHORT_DURATION = 200;

    private View mViewToolbarExtend;
    private ColorView mCvToolbar;
    private ColorView mCvTemp;
    private ColorView mCvImg;
    private ColorView mCvHourly;
    private ColorView mCvWeeklyInfo;
    private ColorView mCvWeeklyChart;
    private ColorView mCvSuggestion;
    private ArrayList<ColorView> mCvAll = new ArrayList<>();

    private TextView mTvComf;
    private TextView mTvDrsg;
    private TextView mTvFlu;
    private TextView mTvUv;
    private TextView mTvSport;
    private TextView mTvCw;
    private MyLinearLayout mLlComf;
    private MyLinearLayout mLlDrsg;
    private MyLinearLayout mLlFlu;
    private MyLinearLayout mLlUv;
    private MyLinearLayout mLlSport;
    private MyLinearLayout mLlCw;

    public static final int MODE_NORMAL = 1;
    public static final int MODE_UPDATE = 7;
    public static final int MODE_CREATE = 8;
    private int mModeType = MODE_NORMAL;
    public static final int MODE_CHANGE_NORMAL = 0;
    public static final int MODE_CHANGE_SINGLE = 2;
    public static final int MODE_CHANGE_ALL_DIFFERENT = 3;
    public static final int MODE_CHANGE_ALL_SAME = 4;
    public static final int MODE_CHANGE_ALL_GRADUAL = 5;
    public static final int MODE_CHANGE_PICTURE = 6;
    private int mChangeModeType = MODE_CHANGE_NORMAL;

    public static final int MENU_NORMAL = 11;
    public static final int MENU_CHANGE = 12;
    private int mMenuType = MENU_NORMAL;

    public static final int SHOW_LOCAL = 21;
    public static final int SHOW_WITH_HOURLY_QUERY = 22;
    private int mShowType = SHOW_LOCAL;

    private MyPopWindow mPopWindow;

    private boolean mFlagThemeChanged = false;

    ArrayList<ItemTheme> mItemThemes = new ArrayList<>();
    private int mThemeNow;
    private int mClickedId;
    private boolean mFlagFirst = true;

    private SharedPreferenceUtil mPrefs;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dawer);

        initView();
        initData();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("小鹿天气");
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.open, R.string.close);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();//初始化状态

        mNsvMain = (NestedScrollView) findViewById(R.id.nsv_main);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mNavigationView = (NavigationView) findViewById(R.id.id_navigation_view);
        //icon 的颜色
        mNavigationView.setItemIconTintList(null);
        onNavigationViewMenuItemSelected(mNavigationView);

        mTvNowTemp = (TextView) findViewById(R.id.tv_temp_now);
        mTvNowAqi = (TextView) findViewById(R.id.tv_aqi_now);
        mTvNowAqi.setOnClickListener(this);
        mTvPublishTime = (TextView) findViewById(R.id.tv_publish_time);
        mIvNowWeatherImg = (ImageView) findViewById(R.id.iv_weather_now);
        mTvNowWeather = (TextView) findViewById(R.id.tv_weather_now);

        mRcvHourly = (MyRecyclerView) findViewById(R.id.rcv_hourly);
        mHourlyAdapter = new RcvHourlyAdapter(mHourlyDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvHourly.setLayoutManager(linearLayoutManager);
        mRcvHourly.setAdapter(mHourlyAdapter);

        mRcvWeekly = (MyRecyclerView) findViewById(R.id.rcv_weekly);
        mWeeklyAdapter = new RcvWeeklyAdapter(mWeeklyDataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        mRcvWeekly.setLayoutManager(gridLayoutManager);
        mRcvWeekly.setAdapter(mWeeklyAdapter);

        mLineWeekly = (LineGraphicView) findViewById(R.id.line_weekly);

        mViewToolbarExtend = findViewById(R.id.view_toolbar_extend);
        mViewToolbarExtend.setOnTouchListener(this);
        mViewToolbarExtend.setOnLongClickListener(this);
        mViewToolbarExtend.setOnClickListener(this);
        mCvToolbar = (ColorView) findViewById(R.id.cv_toolbar);

        mCvTemp = (ColorView) findViewById(R.id.cv_temp);
        mCvTemp.setOnLongClickListener(this);
        mCvTemp.setOnClickListener(this);
        mCvTemp.setOnTouchListener(this);

        mCvImg = (ColorView) findViewById(R.id.cv_img_weather_now);
        mCvImg.setOnLongClickListener(this);
        mCvImg.setOnClickListener(this);
        mCvImg.setOnTouchListener(this);

        mCvHourly = (ColorView) findViewById(R.id.cv_hourly);
        mCvHourly.setOnLongClickListener(this);
        mCvHourly.setOnClickListener(this);
        mCvHourly.setOnTouchListener(this);

        mCvWeeklyInfo = (ColorView) findViewById(R.id.cv_weekly_info);
        mCvWeeklyInfo.setOnLongClickListener(this);
        mCvWeeklyInfo.setOnClickListener(this);
        mCvWeeklyInfo.setOnTouchListener(this);

        mCvWeeklyChart = (ColorView) findViewById(R.id.cv_weekly_chart);
        mCvWeeklyChart.setOnLongClickListener(this);
        mCvWeeklyChart.setOnClickListener(this);
        mCvWeeklyChart.setOnTouchListener(this);

        mCvSuggestion = (ColorView) findViewById(R.id.cv_suggestion);
        mCvSuggestion.setOnLongClickListener(this);
        mCvSuggestion.setOnClickListener(this);
        mCvSuggestion.setOnTouchListener(this);

        mCvAll = new ArrayList<>();
        mCvAll.add(mCvToolbar);
        mCvAll.add(mCvTemp);
        mCvAll.add(mCvImg);
        mCvAll.add(mCvHourly);
        mCvAll.add(mCvWeeklyInfo);
        mCvAll.add(mCvWeeklyChart);
        mCvAll.add(mCvSuggestion);

        mTvComf = (TextView) findViewById(R.id.tv_comf);
        mTvDrsg = (TextView) findViewById(R.id.tv_drsg);
        mTvFlu = (TextView) findViewById(R.id.tv_flu);
        mTvUv = (TextView) findViewById(R.id.tv_uv);
        mTvSport = (TextView) findViewById(R.id.tv_sport);
        mTvCw = (TextView) findViewById(R.id.tv_cw);

        mLlComf = (MyLinearLayout) findViewById(R.id.ll_comf);
        mLlDrsg = (MyLinearLayout) findViewById(R.id.ll_drsg);
        mLlFlu = (MyLinearLayout) findViewById(R.id.ll_flu);
        mLlUv = (MyLinearLayout) findViewById(R.id.ll_uv);
        mLlSport = (MyLinearLayout) findViewById(R.id.ll_sport);
        mLlCw = (MyLinearLayout) findViewById(R.id.ll_cw);

        mLlComf.setOnClickListener(this);
        mLlDrsg.setOnClickListener(this);
        mLlFlu.setOnClickListener(this);
        mLlUv.setOnClickListener(this);
        mLlSport.setOnClickListener(this);
        mLlCw.setOnClickListener(this);
    }

    private void initData() {
        mPrefs = new SharedPreferenceUtil(this);
        SharedPreferences pref2 = getSharedPreferences("first_or_not", MODE_PRIVATE);
        Boolean flag2 = pref2.getBoolean("first", true);
        if (flag2) {
            Positioning();
            SharedPreferences.Editor editor = getSharedPreferences("first_or_not", MODE_PRIVATE).edit();
            editor.putBoolean("first", false);
            editor.apply();
            OtherUtil.initDatabase(this);
        }

        mDeerWeatherDB = DeerWeatherDB.getInstance(this);

        refresh();
        if (!flag2) {
            showWeather(SHOW_LOCAL);
        }

        initTheme();
    }

    private void initTheme() {
        mItemThemes = (ArrayList<ItemTheme>) mDeerWeatherDB.loadMyTheme();
        mThemeNow = mPrefs.getThemeNow();
        for (int i = 0; i < mCvAll.size(); i++) {
            ColorView colorView = mCvAll.get(i);
            if (!OtherUtil.isEmpty(mItemThemes.get(mThemeNow).getPictureUrls().get(i))) {
                mImageUri = Uri.parse(mItemThemes.get(mThemeNow).getPictureUrls().get(i));
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(mImageUri);
                    Log.d(TAG, "initTheme: " + mImageUri.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                colorView.setBackground(drawable);
            } else {
                int color = mItemThemes.get(mThemeNow).getColors().get(i);
                Log.d("co_3", "initTheme: " + color);
                colorView.setBackgroundColor(color);
                colorView.setColor(color);
                colorView.setDuration(1200);
                colorView.setColorPre(color);
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 200);
    }

    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_location:
                        Positioning();
                        break;
                    case R.id.item_add:
                        Intent intent = new Intent(getApplicationContext(), ChooseAreaActivity.class);
                        intent.putExtra("picture", mItemThemes.get(mThemeNow).getPictureUrls().get(0));
                        intent.putExtra("color", mItemThemes.get(mThemeNow).getColors().get(0));
                        intent.putExtra("from_weather_activity", true);
                        startActivityForResult(intent, CHOOSE_COUNTY);
                        break;
                    case R.id.item_delete:
                        final List<Integer> mSelectedItems = new ArrayList<>();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                        builder.setTitle("选择")
                                .setMultiChoiceItems(mSavedCountyName.toArray(new CharSequence[mSavedCountyName.size()]), null,
                                        new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which,
                                                                boolean isChecked) {
                                                if (isChecked) {
                                                    // If the user checked the item, addd it to the bg_selected items
                                                    mSelectedItems.add(which);
                                                } else if (mSelectedItems.contains(which)) {
                                                    // Else, if the item is already in the array, removes it
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
                                            mDeerWeatherDB.deleteMyCounties(mSavedCountyName.get(mSelectedItems.get(i)));
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
                    case R.id.item_about:
                        Intent intent2 = new Intent(WeatherActivity.this, About.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                //menuItem.setChecked(true);
                mDrawer.closeDrawers();

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
            queryCountyNameByLocation(location);
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

                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                    String timeNow = sDateFormat.format(new Date());
//                    文件名只能有数字，字母，下划线等，不然提取不出来。
                    File outputImage = new File(Environment.getExternalStorageDirectory() + "/deerweather/", "bg_" + timeNow + ".jpg");
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
                    int h_screen = ViewUtil.dip2px(this, 190);
                    intent.putExtra("aspectX", w_screen);// 裁剪框比例
                    intent.putExtra("aspectY", h_screen);
                    intent.putExtra("outputX", w_screen);// 输出图片大小
                    intent.putExtra("outputY", h_screen);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(mImageUri);
                        Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                        Log.d(TAG, "onActivityResult: " + mImageUri.toString());

                        mPrefs.setPictureUrl(mImageUri.toString(), OtherUtil.getIndexFromId(mClickedId));
                        Log.d(TAG, "onActivityResult: " + OtherUtil.getIndexFromId(mClickedId));

                        mCvAll.get(OtherUtil.getIndexFromId(mClickedId)).setBackground(drawable);
                        mCvAll.get(OtherUtil.getIndexFromId(mClickedId)).setColor(Color.TRANSPARENT);
                        mFlagThemeChanged = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_COUNTY:
                if (resultCode == RESULT_OK) {
                    mToolbar.setTitle("同步中...");
                    String countyCodePart = data.getStringExtra("county_code_part");
                    if (countyCodePart != null) {
                        try {
                            queryCountyCode(countyCodePart);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(WeatherActivity.this, "获取权限失败,请手动选择。", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void refresh() {
        mToolbar.setTitle("同步中...");
        String countyCode = mPrefs.getCountyCode();
        if (!TextUtils.isEmpty(countyCode)) {
            try {
                queryWeatherByCode(countyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void queryCountyCode(String countyCodePart) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCodePart + ".xml";
        queryFromServer(address, "countyCodePart");
    }

    private void queryCountyNameByLocation(Location location) {
        String address = "http://api.map.baidu.com/geocoder/v2/?ak=Xy2ajWz8vegU33RGG7U5g2ll&callback=renderReverse&location=" + location.getLatitude()
                + "," + location.getLongitude() + "&output=json&pois=1&&mcode=CA:6C:C6:A7:DE:DD:96:65:0A:E0:66:FA:66:B1:04:22:34:D7:9F:F7;com.deerweather.app";
        queryFromServer(address, "coordinates");
        Log.d(TAG, "queryCountyNameByLocation: " + address);
    }

    private void queryWeatherByCode(String countyCode) {
        String httpUrl = "https://api.heweather.com/x3/weather?cityid=" + countyCode + "&key=13d63a6fe83c44c897d62002f4c98551";
        try {
            queryFromServer(httpUrl, "weather");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryWeatherByName(String countyName) {
        String httpUrl = "https://api.heweather.com/x3/weather?city=" + countyName + "&key=13d63a6fe83c44c897d62002f4c98551";
        try {
            queryFromServer(httpUrl, "weather");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryHourlyWeather(County county) {
        String httpUrl = "https://api.caiyunapp.com/v2/BjEVuqiQs2QXD0-P/" + county.getLongitude() + "," + county.getLatitude() + "/forecast.json";
        try {
            queryFromServer(httpUrl, "hourly");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                if ("weather".equals(type)) {
                    JSONUtility.handleWeatherResponse(WeatherActivity.this, response);
                    JSONUtility.handleWeatherWeekly(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefresh.setRefreshing(false);
                            showWeather(SHOW_WITH_HOURLY_QUERY);
                        }
                    });
                } else if ("countyCodePart".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array.length == 2) {
                            String countyCode = "CN" + array[1];
                            queryWeatherByCode(countyCode);
                        }
                    }
                } else if ("hourly".equals(type)) {
                    JSONUtility.handleWeatherHourly(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather(SHOW_LOCAL);
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(response)) {
                        String countyName = JSONUtility.handleNameByLocation(WeatherActivity.this, response);
                        try {
                            countyName = URLEncoder.encode(countyName, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        queryWeatherByName(countyName);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToolbar.setTitle("同步失败");
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    private void showWeather(int type) {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefresh.setRefreshing(false);
//            }
//        }, 200);

        mToolbar.setTitle(mPrefs.getCountyName());

        saveMyCounties();
        loadMyCounties();

        if (type == SHOW_WITH_HOURLY_QUERY) {
            for (int i = 0; i < mCounty.size(); i++) {
                if (mPrefs.getCountyName().equals(mCounty.get(i).getCountyName())) {
                    queryHourlyWeather(mCounty.get(i));
                    break;
                }
            }
        }

        mTvPublishTime.setText(mPrefs.getPublishTime());
        mTvNowTemp.setText((mPrefs.getTempNow()));
        mTvNowAqi.setText(mPrefs.getAqiNow());
        mIvNowWeatherImg.setImageResource(
                ViewUtil.getImageResource(
                        mPrefs.getWeatherCodeNow()));
        mTvNowWeather.setText(mPrefs.getWeatherNow());

        mHourlyDataList.clear();
        for (int i = 0; i < 24; i++) {
            ItemHourly itemHourly = new ItemHourly();
            itemHourly.setTimeHourly(mPrefs.getTimeHourly(i));
            itemHourly.setWeatherCodeHourly(mPrefs.getWeatherCodeHourly(i));
            itemHourly.setTempHourly(mPrefs.getTempHourly(i));
            itemHourly.setAqiHourly(mPrefs.getAqiHourly(i));
            itemHourly.setPm25Hourly(mPrefs.getPm25Hourly(i));

            mHourlyDataList.add(itemHourly);
        }
        mHourlyAdapter.notifyDataSetChanged();

        mWeeklyDataList.clear();
        mLineDataHigh.clear();
        mLineDataLow.clear();
        for (int i = 0; i < 7; i++) {
            ItemWeekly itemWeekly = new ItemWeekly();
            String maxTempWeekly = mPrefs.getMaxTempWeekly(i);
            itemWeekly.setMaxTempWeekly(maxTempWeekly);
            String minTempWeekly = mPrefs.getMinTempWeekly(i);
            itemWeekly.setMinTempWeekly(minTempWeekly);
            if (maxTempWeekly.length() > 0 && minTempWeekly.length() > 0) {
                mLineDataHigh.add(Integer.valueOf(maxTempWeekly.substring(0, maxTempWeekly.length() - 1)));
                mLineDataLow.add(Integer.valueOf(minTempWeekly.substring(0, minTempWeekly.length() - 1)));
            }

            itemWeekly.setWeatherWeekly(mPrefs.getWeatherWeekly(i));
            itemWeekly.setWeatherCodeWeekly(mPrefs.getWeatherCodeWeekly(i));
            itemWeekly.setAqiWeekly(mPrefs.getAqiWeekly(i));

            mWeeklyDataList.add(itemWeekly);
        }
        mWeeklyAdapter.notifyDataSetChanged();

        setLineWeekly(mLineDataHigh, mLineDataLow);

        mTvComf.setText(mPrefs.getComfB());
        mTvDrsg.setText(mPrefs.getDrsgB());
        mTvFlu.setText(mPrefs.getFluB());
        mTvUv.setText(mPrefs.getUvB());
        mTvSport.setText(mPrefs.getSportB());
        mTvCw.setText(mPrefs.getCwB());

        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);

        Intent intent1 = new Intent("com.android.CHANGE");
        sendBroadcast(intent1);

    }

    private void setLineWeekly(ArrayList<Integer> dataHigh, ArrayList<Integer> dataLow) {
        if (dataHigh.size() == 7 && dataLow.size() == 7) {
            int max = dataHigh.get(0);
            int min = dataLow.get(0);
            for (int i = 1; i < dataHigh.size(); i++) {
                if (dataHigh.get(i) > max) {
                    max = dataHigh.get(i);
                }
                if (dataLow.get(i) < min) {
                    min = dataLow.get(i);
                }
            }
            mLineWeekly.setData(dataHigh, dataLow, max, min);
            mLineWeekly.invalidate();
        }
    }

    private void saveMyCounties() {
        County county1 = new County();
        if (!mPrefs.getCountyName().equals("--") && !mPrefs.getCountyName().equals("")) {
            county1.setCountyName(mPrefs.getCountyName());
            county1.setCountyCode(mPrefs.getCountyCode());
            county1.setLongitude(mPrefs.getLongitude());
            county1.setLatitude(mPrefs.getLatitude());
            if (mDeerWeatherDB.searchMyCounty(county1)) {
                mDeerWeatherDB.saveMyCounty(county1);
            }
        }
    }

    private void loadMyCounties() {
        mCounty = mDeerWeatherDB.loadMyCounties();
        final SubMenu menu = mNavigationView.getMenu().getItem(0).getSubMenu();
        menu.clear();
        mSavedCountyName.clear();
        for (int i = 0; i < mCounty.size(); i++) {
            mSavedCountyName.add(mCounty.get(i).getCountyName());
            menu.add(mCounty.get(i).getCountyName()).setIcon(R.drawable.ic_dot);
            MenuItem item = menu.getItem(i);
            final int finalI = i;
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    closeNavDrawer();
                    mToolbar.setTitle("同步中...");
                    mToolbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                queryWeatherByCode(mCounty.get(finalI).getCountyCode());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 400);
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

    protected boolean isNavDrawerOpen() {
        return mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mChangeModeType == MODE_CHANGE_SINGLE) {
            if (v.getId() == R.id.rcv_hourly || v.getId() == R.id.rcv_weekly || v.getId() == R.id.view_toolbar_extend) {
                ColorView colorView;
                if (v.getId() == R.id.rcv_hourly) {
                    colorView = mCvHourly;
                } else if (v.getId() == R.id.rcv_weekly) {
                    colorView = mCvWeeklyInfo;
                } else {
                    colorView = mCvToolbar;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        colorView.setLongClicked(false);

                        colorView.setClickX((int) event.getX());
                        colorView.setClickY((int) event.getY());
                        if (v.getId() == R.id.view_toolbar_extend) {
                            colorView.setClickY((int) event.getY() + ViewUtil.dip2px(this, 80));
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch_hourly: up");
                        if (!colorView.isLongClicked()) {
                            changeColor(colorView, SHORT_DURATION);
                            mPrefs.setPictureUrl("", OtherUtil.getIndexFromId(colorView.getId()));
                        }
                        break;
                }
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ColorView colorView = ((ColorView) v);
                        colorView.setLongClicked(false);
                        Log.d(TAG, "onTouch: down");

                        colorView.setClickX((int) event.getX());
                        colorView.setClickY((int) event.getY());

                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch: move");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: up");
                        if (!((ColorView) v).isLongClicked()) {
                            changeColor(v, SHORT_DURATION);
                            mPrefs.setPictureUrl("", OtherUtil.getIndexFromId(v.getId()));
                        }
                        break;
                }
            }
        } else if (mChangeModeType == MODE_CHANGE_ALL_DIFFERENT) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i < mCvAll.size(); i++) {
                        ColorView colorView = mCvAll.get(i);
                        colorView.setLongClicked(false);
                        colorView.setClickX((int) event.getX());
                        colorView.setClickY((int) event.getY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    for (int i = 0; i < mCvAll.size(); i++) {
                        ColorView colorView = mCvAll.get(i);
                        if (!colorView.isLongClicked()) {
                            changeColor(colorView, SHORT_DURATION);
                        }
                    }
                    break;
            }
        } else if (mChangeModeType == MODE_CHANGE_ALL_SAME) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i < mCvAll.size(); i++) {
                        ColorView colorView = mCvAll.get(i);
                        colorView.setLongClicked(false);
                        colorView.setClickX((int) event.getX());
                        colorView.setClickY((int) event.getY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mCvAll.get(0).isLongClicked()) {
                        changeColorAllSame(mCvAll, SHORT_DURATION);
                    }
                    break;
            }
        } else if (mChangeModeType == MODE_CHANGE_ALL_GRADUAL) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i < mCvAll.size(); i++) {
                        ColorView colorView = mCvAll.get(i);
                        colorView.setLongClicked(false);
                        colorView.setClickX((int) event.getX());
                        colorView.setClickY((int) event.getY());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mCvAll.get(0).isLongClicked()) {
                        changeColorAllGradual(mCvAll, SHORT_DURATION);
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mChangeModeType == MODE_CHANGE_SINGLE) {
            ColorView colorView;
            Log.d(TAG, "onLongClick: id " + v.getId());
            if (v.getId() == R.id.rcv_hourly || v.getId() == R.id.rcv_weekly || v.getId() == R.id.view_toolbar_extend) {
                Log.d(TAG, "onLongClick: long");
                if (v.getId() == R.id.rcv_hourly) {
                    colorView = mCvHourly;
                } else if (v.getId() == R.id.rcv_weekly) {
                    colorView = mCvWeeklyInfo;
                } else {
                    colorView = mCvToolbar;
                }
                changeColor(colorView, LONG_DURATION);
                colorView.setLongClicked(true);
            } else {
                changeColor(v, LONG_DURATION);
                colorView = ((ColorView) v);
                colorView.setLongClicked(true);
            }
        } else if (mChangeModeType == MODE_CHANGE_ALL_DIFFERENT) {
            for (int i = 0; i < mCvAll.size(); i++) {
                changeColor(mCvAll.get(i), LONG_DURATION);
                mCvAll.get(i).setLongClicked(true);
            }
        } else if (mChangeModeType == MODE_CHANGE_ALL_SAME) {
            for (int i = 0; i < mCvAll.size(); i++) {
                mCvAll.get(i).setLongClicked(true);
            }

            changeColorAllSame(mCvAll, LONG_DURATION);
        } else if (mChangeModeType == MODE_CHANGE_ALL_GRADUAL) {
            for (int i = 0; i < mCvAll.size(); i++) {
                mCvAll.get(i).setLongClicked(true);
            }

            changeColorAllGradual(mCvAll, LONG_DURATION);
        }
        return true;
    }

    private void changeColor(View view, int duration) {
        mFlagThemeChanged = true;
        Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        view.setBackgroundColor(((ColorView) view).getColorPre());
        ColorView colorView = ((ColorView) view);
        colorView.setDuration(duration);
        colorView.setColor(color);
        colorView.start(colorView.getClickX(), colorView.getClickY());
        colorView.setColorPre(color);
    }

    private void changeColorAllSame(ArrayList<ColorView> colorViews, int duration) {
        mFlagThemeChanged = true;
        Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        Log.d("co_", "changeColorAllSame: " + color);
        for (int i = 0; i < colorViews.size(); i++) {
            ColorView colorView = colorViews.get(i);
            colorView.setBackgroundColor(colorViews.get(i).getColorPre());
            colorView.setDuration(duration);
            colorView.setColor(color);
            colorView.start(colorView.getClickX(), colorView.getClickY());
            colorView.setColorPre(color);
        }
    }

    private void changeColorAllGradual(ArrayList<ColorView> colorViews, int duration) {
        mFlagThemeChanged = true;
        Random rnd = new Random();
        int red = rnd.nextInt(256);
        int green = rnd.nextInt(256);
        int blue = rnd.nextInt(256);

        int count = colorViews.size();
        int[] reds = new int[count];
        int[] greens = new int[count];
        int[] blues = new int[count];

        int mode = rnd.nextInt(13);
        for (int i = 0; i < count; i++) {
            if (mode == 0) {
                reds[i] = (red / count) * (count - i);
                greens[i] = green;
                blues[i] = blue;
            } else if (mode == 1) {
                reds[i] = red;
                greens[i] = (green / count) * (count - i);
                blues[i] = blue;
            } else if (mode == 2) {
                reds[i] = red;
                greens[i] = green;
                blues[i] = (blue / count) * (count - i);
            } else if (mode == 3) {
                reds[i] = (red / count) * (count - i);
                greens[i] = (green / count) * (count - i);
                blues[i] = blue;
            } else if (mode == 4) {
                reds[i] = (red / count) * (count - i);
                greens[i] = green;
                blues[i] = (blue / count) * (count - i);
            } else if (mode == 5) {
                reds[i] = red;
                greens[i] = (green / count) * (count - i);
                blues[i] = (blue / count) * (count - i);
            } else if (mode == 6) {
                reds[i] = (red / count) * (count - i);
                greens[i] = (green / count) * (count - i);
                blues[i] = (blue / count) * (count - i);
            } else if (mode == 7) {
                reds[i] = red + (230 - red) / count * (count - i);
                greens[i] = green;
                blues[i] = blue;
            } else if (mode == 8) {
                reds[i] = red;
                greens[i] = green + (230 - green) / count * (count - i);
                blues[i] = blue;
            } else if (mode == 9) {
                reds[i] = red;
                greens[i] = green;
                blues[i] = blue + (230 - blue) / count * (count - i);
            } else if (mode == 10) {
                reds[i] = red + (230 - red) / count * (count - i);
                greens[i] = green + (230 - green) / count * (count - i);
                blues[i] = blue;
            } else if (mode == 11) {
                reds[i] = red + (230 - red) / count * (count - i);
                greens[i] = green;
                blues[i] = blue + (230 - blue) / count * (count - i);
            } else if (mode == 12) {
                reds[i] = red;
                greens[i] = green + (230 - green) / count * (count - i);
                blues[i] = blue + (230 - blue) / count * (count - i);
            }
        }


        for (int i = 0; i < count; i++) {
            ColorView colorView = colorViews.get(i);
//            colorView.set
            colorView.setBackgroundColor(colorViews.get(i).getColorPre());
            colorView.setDuration(duration);
            colorView.setColor(Color.argb(255, reds[i], greens[i], blues[i]));
            colorView.start(colorView.getClickX(), colorView.getClickY());
            colorView.setColorPre(Color.argb(255, reds[i], greens[i], blues[i]));
        }
    }

    private void changeColorAllByTheme(ArrayList<ColorView> colorViews, ItemTheme itemTheme, int duration) {
        for (int i = 0; i < colorViews.size(); i++) {
            final ColorView colorView = colorViews.get(i);
            if (!OtherUtil.isEmpty(itemTheme.getPictureUrls().get(i))) {
                Log.d(TAG, "changeColorAllByTheme: " + i + " " + itemTheme.getPictureUrls().get(i));
                InputStream inputStream = null;
                Uri uri = Uri.parse(itemTheme.getPictureUrls().get(i));
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());

                colorView.setBackground(drawable);
                colorView.setColor(Color.TRANSPARENT);
                colorView.start(0, 0);
                Log.d(TAG, "changeColorAllByTheme: done");

            } else {
                Log.d(TAG, "changeColorAllByTheme: " + i + " " + itemTheme.getColors().get(i));
                colorView.setBackgroundColor(colorViews.get(i).getColorPre());
                colorView.setDuration(duration);
                colorView.setColor(itemTheme.getColors().get(i));
                colorView.start(0, 0);
                colorView.setColorPre(itemTheme.getColors().get(i));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else if (mChangeModeType != MODE_NORMAL) {
            mViewToolbarExtend.setVisibility(View.GONE);
            mChangeModeType = MODE_NORMAL;
            mMenuType = MENU_NORMAL;
            mRcvHourly.setNeedDispatch(true);
            mRcvWeekly.setNeedDispatch(true);
            mLlComf.setNeedDispatch(true);
            mLlDrsg.setNeedDispatch(true);
            mLlFlu.setNeedDispatch(true);
            mLlUv.setNeedDispatch(true);
            mLlSport.setNeedDispatch(true);
            mLlCw.setNeedDispatch(true);
            this.invalidateOptionsMenu();
        } else if (mPopWindow != null && mPopWindow.isShowing()) {
            dismissPopWindow();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (mMenuType == MENU_NORMAL) {
            getMenuInflater().inflate(R.menu.menu_main_normal, menu);
            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);

            // Fetch and store ShareActionProvider
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        } else {
            getMenuInflater().inflate(R.menu.menu_main_change, menu);
        }
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_theme) {
            mViewToolbarExtend.setBackgroundColor(Color.TRANSPARENT);
            mViewToolbarExtend.setVisibility(View.VISIBLE);
            mModeType = MODE_CREATE;
            mChangeModeType = MODE_CHANGE_SINGLE;
            mMenuType = MENU_CHANGE;
            mRcvHourly.setNeedDispatch(false);
            mRcvWeekly.setNeedDispatch(false);
            mLlComf.setNeedDispatch(false);
            mLlDrsg.setNeedDispatch(false);
            mLlFlu.setNeedDispatch(false);
            mLlUv.setNeedDispatch(false);
            mLlSport.setNeedDispatch(false);
            mLlCw.setNeedDispatch(false);
            this.invalidateOptionsMenu();
        } else if (id == R.id.action_update_theme) {
            mViewToolbarExtend.setVisibility(View.VISIBLE);
            mModeType = MODE_UPDATE;
            mChangeModeType = MODE_CHANGE_SINGLE;
            mMenuType = MENU_CHANGE;
            mRcvHourly.setNeedDispatch(false);
            mRcvWeekly.setNeedDispatch(false);
            mLlComf.setNeedDispatch(false);
            mLlDrsg.setNeedDispatch(false);
            mLlFlu.setNeedDispatch(false);
            mLlUv.setNeedDispatch(false);
            mLlSport.setNeedDispatch(false);
            mLlCw.setNeedDispatch(false);
            this.invalidateOptionsMenu();
        } else if (id == R.id.action_all_different) {
            mChangeModeType = MODE_CHANGE_ALL_DIFFERENT;
        } else if (id == R.id.action_all_same) {
            mChangeModeType = MODE_CHANGE_ALL_SAME;
        } else if (id == R.id.action_all_gradual) {
            mChangeModeType = MODE_CHANGE_ALL_GRADUAL;
        } else if (id == R.id.action_change_single) {
            mChangeModeType = MODE_CHANGE_SINGLE;
        } else if (id == R.id.action_save_theme) {
            mViewToolbarExtend.setVisibility(View.GONE);
            mMenuType = MENU_NORMAL;
            mRcvHourly.setNeedDispatch(true);
            mRcvWeekly.setNeedDispatch(true);
            mLlComf.setNeedDispatch(true);
            mLlDrsg.setNeedDispatch(true);
            mLlFlu.setNeedDispatch(true);
            mLlUv.setNeedDispatch(true);
            mLlSport.setNeedDispatch(true);
            mLlCw.setNeedDispatch(true);
            if (mFlagThemeChanged) {
                ItemTheme itemTheme = new ItemTheme();
                ArrayList<Integer> colors = new ArrayList<>();
                ArrayList<String> pictureUrls = new ArrayList<>();
                if (mChangeModeType == MODE_CHANGE_PICTURE || mChangeModeType == MODE_CHANGE_SINGLE) {
                    for (int i = 0; i < mCvAll.size(); i++) {
                        if (!OtherUtil.isEmpty(mPrefs.getPictureUrl(i))) {
                            pictureUrls.add(i, mPrefs.getPictureUrl(i));
                            colors.add(i, mCvAll.get(i).getColorPre());
                        } else {
                            pictureUrls.add(i, "");
                            colors.add(i, mCvAll.get(i).getColorPre());
                        }
                    }
                } else {
                    for (int i = 0; i < mCvAll.size(); i++) {
                        pictureUrls.add(i, "");
                        colors.add(i, mCvAll.get(i).getColorPre());
                    }
                }

                if (mModeType == MODE_UPDATE) {
                    mModeType = MODE_NORMAL;
                    mChangeModeType = MODE_CHANGE_NORMAL;
                    itemTheme.setColors(colors);
                    itemTheme.setPictureUrls(pictureUrls);
                    itemTheme.setId(mItemThemes.get(mThemeNow).getId());
                    Log.d(TAG, "onOptionsItemSelected: " + mItemThemes.get(mThemeNow).getId());
                    mDeerWeatherDB.updateMyTheme(itemTheme);
                    mFlagThemeChanged = false;
//                    mThemeNow = mItemThemes.size();
                } else {
                    mModeType = MODE_NORMAL;
                    mChangeModeType = MODE_CHANGE_NORMAL;
                    itemTheme.setColors(colors);
                    itemTheme.setPictureUrls(pictureUrls);
                    mDeerWeatherDB.saveMyTheme(itemTheme);
                    mFlagThemeChanged = false;
                    mThemeNow = mItemThemes.size();
                }
                mPrefs.setThemeNow(mThemeNow);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "主题已存在", Toast.LENGTH_SHORT).show();
            }
            this.invalidateOptionsMenu();
        } else if (id == R.id.action_quit) {
            if (mFlagThemeChanged) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("放弃这次修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewToolbarExtend.setVisibility(View.GONE);
                                mChangeModeType = MODE_NORMAL;
                                mMenuType = MENU_NORMAL;
                                mRcvHourly.setNeedDispatch(true);
                                mRcvWeekly.setNeedDispatch(true);
                                WeatherActivity.this.invalidateOptionsMenu();
                                changeColorAllByTheme(mCvAll, mItemThemes.get(mThemeNow), SHORT_DURATION);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            } else {
                mViewToolbarExtend.setVisibility(View.GONE);
                mChangeModeType = MODE_NORMAL;
                mMenuType = MENU_NORMAL;
                mRcvHourly.setNeedDispatch(true);
                mRcvWeekly.setNeedDispatch(true);
                WeatherActivity.this.invalidateOptionsMenu();
            }
        } else if (id == R.id.action_choose_theme) {
            if (mPopWindow == null || !mPopWindow.isShowing()) {
                mItemThemes = (ArrayList<ItemTheme>) mDeerWeatherDB.loadMyTheme();
                mItemThemes.get(mThemeNow).setSelected(true);
                mPopWindow = new MyPopWindow(WeatherActivity.this, mItemThemes, this,
                        new BaseRcvAdapter.ItemOnClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                changeColorAllByTheme(mCvAll, mItemThemes.get(position), SHORT_DURATION);
                                mThemeNow = position;
                                mPrefs.setThemeNow(mThemeNow);
                                Log.d(TAG, "onItemClick: " + mThemeNow);
                            }
                        });
                mPopWindow.showAtLocation(this.findViewById(R.id.ll_weather_main), Gravity.BOTTOM, 0, 0);
                mPopWindow.scrollToSelected();
            } else {
                mPopWindow.showStartAnimation();
            }
        } else if (id == R.id.action_picture) {
            mChangeModeType = MODE_CHANGE_PICTURE;
            if (mFlagFirst) {
                Toast.makeText(this, "请点击选择背景图片", Toast.LENGTH_SHORT).show();
                mFlagFirst = false;
            } else {
                Toast.makeText(this, "缓存已清空，请重新选择", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < mCvAll.size(); i++) {
                mPrefs.setPictureUrl(mItemThemes.get(mThemeNow).getPictureUrls().get(i), i);
            }
        } else if (id == R.id.action_share) {
            share();
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        this.findViewById(R.id.tv_share).setVisibility(View.VISIBLE);
        Uri uri = OtherUtil.savePic(ViewUtil.getBitmapByView(mNsvMain));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        setShareIntent(shareIntent);
        startActivity(shareIntent);
        this.findViewById(R.id.tv_share).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_cancel) {
            dismissPopWindow();
        } else if (v.getId() == R.id.tv_aqi_now) {
            showAqiDialog();
        } else if (v.getId() == R.id.ll_comf) {
            showSuggestionDialog(mPrefs.getComfT());
        } else if (v.getId() == R.id.ll_drsg) {
            showSuggestionDialog(mPrefs.getDrsgT());
        } else if (v.getId() == R.id.ll_flu) {
            showSuggestionDialog(mPrefs.getfluT());
        } else if (v.getId() == R.id.ll_uv) {
            showSuggestionDialog(mPrefs.getUvT());
        } else if (v.getId() == R.id.ll_sport) {
            showSuggestionDialog(mPrefs.getSportT());
        } else if (v.getId() == R.id.ll_cw) {
            showSuggestionDialog(mPrefs.getCwT());
        } else {
            if (mChangeModeType == MODE_CHANGE_PICTURE) {
                mClickedId = v.getId();
                Log.d(TAG, "onClick: " + v.getId());
                if (mClickedId == R.id.view_toolbar_extend) {
                    mClickedId = R.id.cv_toolbar;
                }
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                intent1.putExtra("crop", true);
                intent1.putExtra("scale", true);
                startActivityForResult(intent1, TAKE_PHOTO);
            }
        }
    }

    private void showSuggestionDialog(String suggstion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(suggstion);
        builder.create().show();
    }

    private void showAqiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_aqi, null);
        builder.setView(view);

        TextView aqi = (TextView) view.findViewById(R.id.tv_aqi_dialog);
        TextView qlty = (TextView) view.findViewById(R.id.tv_qlty_dialog);
        TextView pm25 = (TextView) view.findViewById(R.id.tv_pm25_dialog);
        TextView pm10 = (TextView) view.findViewById(R.id.tv_pm10_dialog);
        TextView no2 = (TextView) view.findViewById(R.id.tv_no2_dialog);
        TextView so2 = (TextView) view.findViewById(R.id.tv_so2_dialog);
        TextView co = (TextView) view.findViewById(R.id.tv_co_dialog);
        TextView o3 = (TextView) view.findViewById(R.id.tv_o3_dialog);
        aqi.setText(mPrefs.getAqiDialog());
        qlty.setText(mPrefs.getQlty());
        pm25.setText(mPrefs.getPm25());
        pm10.setText(mPrefs.getPm10());
        no2.setText(mPrefs.getNo2());
        so2.setText(mPrefs.getSo2());
        co.setText(mPrefs.getCo());
        o3.setText(mPrefs.getO3());
        builder.create().show();
    }

    public void dismissPopWindow() {
        mPopWindow.showDismissAnimation();
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPopWindow.dismiss();
            }
        }, 400);
    }
}