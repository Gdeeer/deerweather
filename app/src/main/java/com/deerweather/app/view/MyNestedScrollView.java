package com.deerweather.app.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Gdeer on 2016/4/1.
 */
public class MyNestedScrollView extends NestedScrollView {

    private static final String TAG = "touch";
    //    // 分别记录上次滑动的坐标
//    private int mLastX = 0;
//    private int mLastY = 0;
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;


    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;

                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        mLastXIntercept = x;
        mLastYIntercept = y;
        return super.onInterceptTouchEvent(event);
    }
}
