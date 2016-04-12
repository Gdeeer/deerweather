package com.deerweather.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.deerweather.app.R;

import java.util.ArrayList;

public class LineGraphicView extends View {
    /**
     * 公共部分
     */
    private static final String TAG = "vvv";

    private static enum Linestyle {
        Line, Curve
    }

    private static enum LineType {
        High, Low
    }

    private Context mContext;
    private Paint mPaint;
    private Resources res;
    private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Line;

    private int canvasHeight;
    private int canvasWidth;

    private int mChartHeight = 0;
    private int mStartX;
    private int mEndX;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int maxValue;
    private int minValue;

    private int marginTop = 85;
    private int marginBottom = 73;

    /**
     * 曲线上总点数
     */
    private Point[] mPointsHigh;
    private Point[] mPointsLow;
    /**
     * 纵坐标值
     */
    private ArrayList<Integer> yRawDataHigh = new ArrayList<>();
    private ArrayList<Integer> yRawDataLow = new ArrayList<>();
    /**
     * 横坐标值
     */
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值

    private int mDotTextColor = Color.argb(255, 255, 255, 255);

    public int getDotTextColor() {
        return mDotTextColor;
    }

    public void setDotTextColor(int dotTextColor) {
        mDotTextColor = dotTextColor;
    }

    public LineGraphicView(Context context) {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public LineGraphicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (isMeasure) {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (mChartHeight == 0)
                mChartHeight = canvasHeight - marginTop - marginBottom;
            mStartX = dip2px(28);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        // 点的操作设置
        setPoints();

        mPaint.setStrokeWidth(dip2px(1.2f));
        mPaint.setStyle(Paint.Style.STROKE);
        if (mStyle == Linestyle.Curve) {
            drawScrollLine(canvas, mPointsHigh);
            drawScrollLine(canvas, mPointsLow);
        } else {
            drawLine(canvas, mPointsHigh);
            drawLine(canvas, mPointsLow);
        }

        drawTextOnLine(canvas);
    }

    private void drawText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(mDotTextColor);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private void setPoints() {
        mPointsHigh = new Point[yRawDataHigh.size()];
        for (int i = 0; i < yRawDataHigh.size(); i++) {
            xList.add(mStartX + (canvasWidth - mStartX * 2) / (yRawDataHigh.size() - 1) * i);
            int pointY = mChartHeight - (int) (mChartHeight * (yRawDataHigh.get(i) - minValue) / (maxValue - minValue)) + marginTop;
            mPointsHigh[i] = new Point(xList.get(i), pointY);
        }
        mPointsLow = new Point[yRawDataLow.size()];
        for (int i = 0; i < yRawDataLow.size(); i++) {
            xList.add(mStartX + (canvasWidth - mStartX * 2) / (yRawDataLow.size() - 1) * i);
            int pointY = mChartHeight - (int) (mChartHeight * (yRawDataLow.get(i) - minValue) / (maxValue - minValue)) + marginTop;
            mPointsLow[i] = new Point(xList.get(i), pointY);
        }
    }


    private void drawLine(Canvas canvas, Point[] points) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < points.length - 1; i++) {
            startp = points[i];
            endp = points[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }


    private void drawScrollLine(Canvas canvas, Point[] points) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < points.length - 1; i++) {
            startp = points[i];
            endp = points[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawTextOnLine(Canvas canvas) {
        for (int i = 0; i < mPointsHigh.length; i++) {
            drawText(
                    String.valueOf(yRawDataHigh.get(i)) + "°",
                    mPointsHigh[i].x - dip2px(8), mPointsHigh[i].y - dip2px(10),
                    canvas);
        }
        for (int i = 0; i < mPointsLow.length; i++) {
            drawText(
                    String.valueOf(yRawDataLow.get(i)) + "°",
                    mPointsLow[i].x - dip2px(8), mPointsLow[i].y + dip2px(16),
                    canvas);
        }
    }

    public void setData(ArrayList<Integer> yRawDataHigh, ArrayList<Integer> yRawDataLow, int maxValue, int minValue) {
        this.mPointsHigh = new Point[yRawDataHigh.size()];
        this.yRawDataHigh = yRawDataHigh;
        this.mPointsLow = new Point[yRawDataLow.size()];
        this.yRawDataLow = yRawDataLow;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        return (int) (dpValue * dm.density + 0.5f);
    }

}

