package com.jkkg.hhtx.utils;

import android.view.Gravity;

import com.hjq.toast.ToastUtils;


public class ToastUtil {
    /**
     * 短时间显示Toast
     *位置：底部
     * @param message
     */
    public static void show(CharSequence message) {
        ToastUtils.setGravity(Gravity.BOTTOM,0,150);
        ToastUtils.show(message);
    }

    /**
     * 短时间显示Toast
     *位置：底部
     * @param message
     */
    public static void show(int message) {
        ToastUtils.setGravity(Gravity.BOTTOM,0,150);
        ToastUtils.show(message);
    }

    /**
     * 短时间显示Toast
     * @param message
     */
    public static void showShort( CharSequence message) {
        ToastUtils.setGravity(Gravity.CENTER,0,0);
        ToastUtils.show(message);
    }

    /**
     * 短时间显示Toast
     * @param message
     */
    public static void showShort(int message) {
        ToastUtils.setGravity(Gravity.CENTER,0,0);
        ToastUtils.show(message);
    }
}