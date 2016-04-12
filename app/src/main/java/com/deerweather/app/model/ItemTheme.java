package com.deerweather.app.model;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Gdeer on 2016/4/2.
 */
public class ItemTheme {
    private Integer mId;
    private ArrayList<Integer> mColors;
    private ArrayList<String> mPictureUrls;
    private boolean mSelected = false;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public ArrayList<Integer> getColors() {
        return mColors;
    }

    public void setColors(ArrayList<Integer> colors) {
        mColors = colors;
    }

    public ArrayList<String> getPictureUrls() {
        return mPictureUrls;
    }

    public void setPictureUrls(ArrayList<String> pictureUrls) {
        mPictureUrls = pictureUrls;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
}
