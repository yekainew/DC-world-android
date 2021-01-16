package com.jkkg.hhtx.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.utils.SpUtil;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.UUID;

import timber.log.Timber;

/**
 * Create by
 * on 2020/7/21
 * Email:oubaoyi@outlook.com
 */
@SuppressLint("TimberArgCount")
public class UniqueIdUtils {
    private static final String TAG = "UniqueIdUtils";
    private static String uniqueID;
    private static String uniqueKey = "unique_id";
    private static String uniqueIDFile = "unique.txt";


    public static String getUniquePsuedoID(Context context) {
        //三步读取：内存中，存储的SP表中，外部存储文件中
        if (!TextUtils.isEmpty(uniqueID)) {
            Timber.e(TAG, "getUniqueID: 内存中获取" + uniqueID);
            return uniqueID;
        }
        uniqueID = SpUtil.getInstance(MyApp.getApp()).getString(uniqueKey, "");
        if (!TextUtils.isEmpty(uniqueID)) {
            Timber.e(TAG, "getUniqueID: SP中获取" + uniqueID);
            return uniqueID;
        }

        //两步创建：硬件获取；自行生成与存储
        getOaid(context);
        getDeviceId(context);
        getAndroidID(context);
        getSNID();

        //如果硬件设备获取成功，进行md5加密
        if(!TextUtils.isEmpty(uniqueID)){
            uniqueID=getMD5(uniqueID,false);
            Timber.e(TAG, "getUniqueID: 硬件信息中获取：" + uniqueID);
        }else{
            createUUID(context);
            Timber.e(TAG, "getUniqueID: UUID中获取：" + uniqueID);
        }
        SpUtil.getInstance(MyApp.getApp()).saveString(uniqueKey, uniqueID);
        return uniqueID;
    }

    public static void getOaid(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }

        if (MyApp.isSupportOaid()) {
            uniqueID = MyApp.getOaid();
            Timber.e(TAG, "getUniqueID: Oaid获取成功" + uniqueID);
        }
    }

    private static void getDeviceId(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        TelephonyManager mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (mTeleManager.getDeviceId() != null) {
                uniqueID = mTeleManager.getDeviceId();
                Timber.e(TAG, "getUniqueID: DeviceId获取成功" + uniqueID);
                //华为MatePad上，可能会获得unknown
                if (TextUtils.isEmpty(uniqueID)||"unknown".equals(uniqueID)) {
                    uniqueID="";
                }
            }
        }catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    private static void getSNID() {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        uniqueID = Build.SERIAL;
        Timber.e(TAG, "getUniqueID: SNID获取成功" + uniqueID);
    }

    private static void getAndroidID(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        try {
            uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            Timber.e(TAG, "getUniqueID: AndroidID获取成功" + uniqueID);
            if (TextUtils.isEmpty(uniqueID) || "9774d56d682e549c".equals(uniqueID)) {
                uniqueID= "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createUUID(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        uniqueID=UUID.randomUUID().toString();
        Timber.e(TAG, "getUniqueID: UUID生成成功" + uniqueID);
        uniqueID=getMD5(uniqueID,false);
    }

    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     * @param context
     * @return
     */
    private static void getLocalMac(Context context) {
//        WifiManager wifi = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                uniqueID= "";
            }
            byte[] addr = networkInterface.getHardwareAddress();


            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            uniqueID = buf.toString();

            //获取设备的MACAddress地址 去掉中间相隔的冒号
            uniqueID = uniqueID.replace(":", "");
        } catch (SocketException e) {
            e.printStackTrace();
            uniqueID="";
        }
    }

    private static void getImei(Context context) {
        if (!TextUtils.isEmpty(uniqueID)) {
            return;
        }
        TelephonyManager mTeleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                uniqueID = mTeleManager.getImei();
            } else if (Build.VERSION.SDK_INT > 20 && Build.VERSION.SDK_INT < 26) {
                try {
                    Method method = mTeleManager.getClass().getMethod("getImei");
                    uniqueID = (String) method.invoke(mTeleManager);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }catch (SecurityException se) {
            se.printStackTrace();
        }
    }


    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    private static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    private static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }
}
