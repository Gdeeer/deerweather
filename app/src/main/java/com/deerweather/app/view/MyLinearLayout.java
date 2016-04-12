package com.deerweather.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Gdeer on 2016/4/8.
 */
public class MyLinearLayout extends LinearLayout {

    private boolean needDispatch = true;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setNeedDispatch(boolean needDispatch) {
        this.needDispatch = needDispatch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!needDispatch){
                    return false;
                }
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
