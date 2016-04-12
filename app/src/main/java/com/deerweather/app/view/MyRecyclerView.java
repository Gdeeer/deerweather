package com.deerweather.app.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Gdeer on 2016/4/2.
 */
public class MyRecyclerView extends RecyclerView {

    private static final String TAG = "touch";
    private boolean needDispatch = true;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isNeedDispatch() {
        return needDispatch;
    }

    public void setNeedDispatch(boolean needDispatch) {
        this.needDispatch = needDispatch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.d(TAG, "dispatchTouchEvent: ");
//        int x = (int) event.getX();
//        int y = (int) event.getY();

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
