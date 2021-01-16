package com.jkkg.hhtx.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jkkg.hhtx.app.MyApp;

import java.lang.reflect.Method;

public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    private static String deviceId = null;

    private static String imei = null;

    private static String oaid = null;

    public static boolean checkAppInstalled(Context context, String packageName) {
        boolean installed = false;

        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            installed = true;
        }
        catch(PackageManager.NameNotFoundException ex) {
            //Do nothing
        }

        return installed;
    }

    public static void openApp(Context context, String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(launchIntent);
    }

    public static String getOaid(Context context) {
        if (TextUtils.isEmpty(oaid)) {
            oaid = "";
            if (MyApp.isSupportOaid()) {
                oaid = MyApp.getOaid();
            }
        }
        return oaid;
    }

    public static String getDeviceId(Context context) {
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "";
            TelephonyManager mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (mTeleManager.getDeviceId() != null) {
                    deviceId = mTeleManager.getDeviceId();
                } else {
                    //deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        return deviceId;
    }

    public static String getImei(Context context) {
        if (TextUtils.isEmpty(imei)) {
            imei = "";
            TelephonyManager mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    imei = mTeleManager.getImei();
                } else if (Build.VERSION.SDK_INT > 20 && Build.VERSION.SDK_INT < 26) {
                    try {
                        Method method = mTeleManager.getClass().getMethod("getImei");
                        imei = (String) method.invoke(mTeleManager);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        return imei;
    }
}
