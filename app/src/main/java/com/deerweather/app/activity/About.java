package com.deerweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.deerweather.app.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class About extends AppCompatActivity implements View.OnClickListener {


    private Uri mImageUri;
    private RelativeLayout mRelativeLayout;
    private Button mGithub;
    private Button mWeibo;
    private Button mFeedback;
    private Button mContribute;
    private PopupWindow mPopFeedback;
    private PopupWindow mPopContribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative);

        SharedPreferences pref = getSharedPreferences("wallpaper_mode", MODE_PRIVATE);
        int flag = pref.getInt("mode", 1);
        if (flag == 2) {
            setWallpaper();
        }

        mGithub = (Button) findViewById(R.id.address_github);
        mWeibo = (Button) findViewById(R.id.my_weibo);
        mFeedback = (Button) findViewById(R.id.feedback);
        mContribute = (Button) findViewById(R.id.contribute);

        mGithub.setOnClickListener(this);
        mWeibo.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mContribute.setOnClickListener(this);
    }

    public void setWallpaper() {
        SharedPreferences pref = getSharedPreferences("wallpaper", MODE_PRIVATE);
        String path = pref.getString("image_path", "");
        if (!path.equals("")) {
            try {
                mImageUri = Uri.parse(path);
                InputStream inputStream = getContentResolver().openInputStream(mImageUri);
                Drawable drawable = Drawable.createFromStream(inputStream, mImageUri.toString());
                mRelativeLayout.setBackground(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.address_github:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Gdeeer/deerweather"));
                startActivity(intent);
                break;
            case R.id.my_weibo:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("http://weibo.com/u/2681148603"));
                startActivity(intent2);
                break;
            case R.id.feedback:
                Toast.makeText(getApplicationContext(), "还没做好，不如去微博留个言(=￣ω￣=)", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contribute:
                showPopContribute(v);
                break;
            default:
                break;
        }
    }

    public void showPopContribute(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.pop_contribute_content, null);
        mPopContribute = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopContribute.setFocusable(true);
        mPopContribute.setOutsideTouchable(true);

        int[] location = new int[2];
        mContribute.getLocationOnScreen(location);

//        mPopContribute.showAtLocation(mContribute, Gravity.TOP|Gravity.LEFT, location[0], location[1]);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPopContribute.isShowing()) {
                    mPopContribute.dismiss();
                }
                return false;
            }
        });
        mPopContribute.showAsDropDown(mContribute, 0, -1020);
    }
}
