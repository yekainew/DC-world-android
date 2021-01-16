package com.jkkg.hhtx.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetUtil {

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    return netInfo != null && netInfo.isConnectedOrConnecting();
                } else {
                    Network network = cm.getActiveNetwork();
                    if (network != null) {
                        NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                        if (nc != null) {
                            if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    NetworkInfo mWiFiNetworkInfo = cm.getActiveNetworkInfo();
                    if (mWiFiNetworkInfo != null) {
                        if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {//WIFI
                            return true;
                        } else if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//移动数据
                            return false;
                        }
                    }
                } else {
                    Network network = cm.getActiveNetwork();
                    if (network != null) {
                        NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                        if (nc != null) {
                            if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {//WIFI
                                return true;
                            } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {//移动数据
                                return false;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}
