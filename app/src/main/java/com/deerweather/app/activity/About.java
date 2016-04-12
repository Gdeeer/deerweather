package com.deerweather.app.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.deerweather.app.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class About extends AppCompatActivity implements View.OnClickListener {

    private PopupWindow mPopFeedback;
    private PopupWindow mPopContribute;

    private LinearLayout mLlGithub;
    private LinearLayout mLlSina;
    private LinearLayout mLlQQ;
    private LinearLayout mLlContribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mLlGithub = (LinearLayout) findViewById(R.id.ll_github);
        mLlSina = (LinearLayout) findViewById(R.id.ll_sina);
        mLlQQ = (LinearLayout) findViewById(R.id.ll_qq);
        mLlContribute = (LinearLayout) findViewById(R.id.ll_contribute);
        mLlGithub.setOnClickListener(this);
        mLlSina.setOnClickListener(this);
        mLlQQ.setOnClickListener(this);
        mLlContribute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_github:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Gdeeer/deerweather"));
                startActivity(intent);
                break;
            case R.id.ll_sina:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("http://weibo.com/u/2681148603"));
                startActivity(intent2);
                break;
            case R.id.ll_qq:
                joinQQGroup("rky9modNWZkxefT1naU5xI7nlijqKH27");
                break;
            case R.id.ll_contribute:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("comment", "1533691934@qq.com");
                cm.setPrimaryClip(clipData);
//                Snackbar.make(v, "已保存至剪贴板", Snackbar.LENGTH_SHORT).setAction("确定", null).show();


                Snackbar snackbar = Snackbar.make(v, "已保存至剪贴板（支付宝账号）", Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snackbar.getView();
                group.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar));
                snackbar.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action));
                snackbar.show();
                break;
            default:
                break;
        }
    }

    /****************
     *
     * 发起添加群流程。群号：deerweather(244037411) 的 key 为： rky9modNWZkxefT1naU5xI7nlijqKH27
     * 调用 joinQQGroup(rky9modNWZkxefT1naU5xI7nlijqKH27) 即可发起手Q客户端申请加群 deerweather(244037411)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
