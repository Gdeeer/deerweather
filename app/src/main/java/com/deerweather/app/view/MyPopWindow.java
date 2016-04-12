package com.deerweather.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.deerweather.app.R;
import com.deerweather.app.adapter.BaseRcvAdapter;
import com.deerweather.app.adapter.RcvThemeAdapter;
import com.deerweather.app.model.ItemTheme;

import java.util.ArrayList;

/**
 * Created by Gdeer on 2016/4/2.
 */
public class MyPopWindow extends PopupWindow {
    private RecyclerView mRcvTheme;
    private View mPopView;
    private ImageView mIvCancel;
    private ArrayList<ItemTheme> mDataList = new ArrayList<>();
    private RcvThemeAdapter mAdapter;

    public MyPopWindow(Context context, ArrayList<ItemTheme> itemThemes,
                       View.OnClickListener onClickListener, BaseRcvAdapter.ItemOnClickListener itemOnClickListener) {
        super(context);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的背景
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        this.setBackgroundDrawable(dw);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopView = inflater.inflate(R.layout.layout_pop, null);
//        mPopView.getBackground().setAlpha(230);
        this.setContentView(mPopView);
        mRcvTheme = (RecyclerView) mPopView.findViewById(R.id.rcv_theme);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvTheme.setLayoutManager(linearLayoutManager);
        mDataList = itemThemes;
        mAdapter = new RcvThemeAdapter(mDataList);
        mAdapter.setItemOnClickListener(itemOnClickListener);
        mRcvTheme.setAdapter(mAdapter);
        mRcvTheme.setItemAnimator(null);

        mIvCancel = (ImageView) mPopView.findViewById(R.id.iv_cancel);
        mIvCancel.setOnClickListener(onClickListener);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 540, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        mIvCancel.setAnimation(rotateAnimation);

//        设置点击下面就退出
//        this.setFocusable(false);
//        this.setOutsideTouchable(true);

        this.setAnimationStyle(R.style.PopAnimation);
    }

    public void showDismissAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, -540, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        mIvCancel.startAnimation(rotateAnimation);
    }


    public void showStartAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 540, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(900);
        mIvCancel.startAnimation(rotateAnimation);
    }

    public void scrollToSelected() {
        mRcvTheme.scrollToPosition(mAdapter.getLastSelected());
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
