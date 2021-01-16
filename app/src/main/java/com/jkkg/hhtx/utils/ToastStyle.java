package com.jkkg.hhtx.utils;

import android.view.Gravity;

import com.hjq.toast.IToastStyle;
import com.jkkg.hhtx.app.MyApp;

/**
 * Create by
 * on 2020/8/14
 * Email:oubaoyi@outlook.com
 */
public class ToastStyle implements IToastStyle {
    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),60);
    }

    @Override
    public int getZ() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),10);
    }

    @Override
    public int getCornerRadius() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),40);
    }

    @Override
    public int getBackgroundColor() {
        return 0Xaa000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return ScreenSizeUtil.sp2px(MyApp.getApp(),14);
    }

    @Override
    public int getMaxLines() {
        return 0;
    }

    @Override
    public int getPaddingStart() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),20);
    }

    @Override
    public int getPaddingTop() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),10);
    }

    @Override
    public int getPaddingEnd() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),20);
    }

    @Override
    public int getPaddingBottom() {
        return ScreenSizeUtil.dp2px(MyApp.getApp(),10);
    }
}
