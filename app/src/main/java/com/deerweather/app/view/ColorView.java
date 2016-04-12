package com.deerweather.app.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("NewApi")
public class ColorView extends View {
    private ObjectAnimator ainm1;
    private float progress;
    private int mClickX;
    private int mClickY;
    private int x1;
    private int y1;
    private int director;
    private int mColor;
    private int mColorPre;
    private Paint mPaint;
    private int mDuration = 200;
    private boolean mLongClicked = false;

    private String mPictureUrl;

    public ColorView(Context context) {
        super(context);
    }

    //实现在布局文件中使用
    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setLongClicked(boolean longClicked) {
        this.mLongClicked = longClicked;
    }

    public boolean isLongClicked() {
        return mLongClicked;
    }

    public int getColorPre() {
        return mColorPre;
    }

    public void setColorPre(int colorPre) {
        mColorPre = colorPre;
    }

    public int getClickY() {
        return mClickY;
    }

    public void setClickY(int clickY) {
        mClickY = clickY;
    }

    public int getClickX() {
        return mClickX;
    }

    public void setClickX(int clickX) {
        mClickX = clickX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);

        if (mClickY < getHeight() / 2 && mClickX > getWidth() / 2) {
            y1 = getHeight() - mClickY;
            director = (int) Math.sqrt(mClickX * mClickX + y1 * y1);
        }
        if (mClickX < getWidth() / 2 && mClickY > getHeight() / 2) {
            x1 = getWidth() - mClickX;
            director = (int) Math.sqrt(x1 * x1 + mClickY * mClickY);
        }
        if (mClickX < getWidth() / 2 && mClickY < getHeight() / 2) {
            y1 = getHeight() - mClickY;
            x1 = getWidth() - mClickX;
            director = (int) Math.sqrt(x1 * x1 + y1 * y1);
        }
        if (mClickX > getWidth() / 2 && mClickY > getHeight() / 2) {
            director = (int) Math.sqrt(mClickX * mClickX + mClickY * mClickY);
        }
        float r = progress * director;
        //画圆

        canvas.drawCircle(mClickX, mClickY, r, mPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void start(int width, int height) {
        mClickX = width;
        mClickY = height;
        //属性动画
        ainm1 = ObjectAnimator.ofFloat(this, "progress", 0, 1).setDuration(mDuration);
        ainm1.start();
    }
}