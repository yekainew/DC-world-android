package com.jkkg.hhtx.app;

import android.content.Context;

import com.jkkg.hhtx.BuildConfig;
import com.jkkg.hhtx.utils.SpUtil;
import com.mugui.base.client.net.classutil.DataSave;


public class Constants {
    /**
     * 当前语言
     **/
    public static final String KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE";

    /**
     * 是否同意协议
     **/
    public static final String PRE_START_APP_ISFIRST = "PRE_START_APP_ISFIRST";

    /**
     * 当前用户TOKEN
     **/
    public static final String KEY_CURRENT_IS_LOGIN = "KEY_CURRENT_IS_LOGIN";

    /**
     * 当前用户手机号码
     **/
    public static final String KEY_CURRENT_PHONE = "KEY_CURRENT_PHONE";

    /**
     * 当前用户ID
     **/
    public static final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";

    /**
     * 当前用户用户名
     **/
    public static final String KEY_CURRENT_USER_NAME = "KEY_CURRENT_USER_NAME";

    /**
     * 当前用户是否激活
     **/
    public static final String KEY_CURRENT_USER_ACTIVATE = "KEY_CURRENT_USER_ACTIVATE";

    /**
     * 当前用户邀请码
     **/
    public static final String KEY_CURRENT_INVITATION = "KEY_CURRENT_INVITATION";

    /**
     * 位置
     **/
    public static final String KEY_CURRENT_LOCATION = "KEY_CURRENT_LOCATION";

    /**
     * 加载默认提示
     */
    public static final String HIDE_LOADING_STATUS_MSG = "hide_loading_status_msg";

    /**
     * 二维码是否连续扫描
     **/
    public static final String KEY_IS_CONTINUOUS = "KEY_IS_CONTINUOUS";

    public static final String FilePath = MyApp.getApp().getPackageName();

    public static final String user_agreement = "/api/article?param=user";//用户协议

    public static final String privacy_agreement = "/api/article?param=privacy";//隐私协议

    public static final String IS_LOGIN = "islogin";//是否登录


    public static final String TIME = "yyyy-MM-dd HH:mm:ss";



    public static final String PUBLIC_COIN="this_coin_wallet";
    public static String getDomain() {
        if (BuildConfig.TEST) {
            return "http://www.nd666.top"; //测试服
        } else {
            return "http://www.nd666.top";//正式服
        }
    }

    /**
     * 是否同意协议
     *
     * @return
     */
    public static boolean isAgreementState(Context context) {
        return SpUtil.getInstance(context).getBoolean(PRE_START_APP_ISFIRST, false);
    }

    public static void dealAgreement(Context context, boolean code) {
        SpUtil.getInstance(context).saveBoolean(PRE_START_APP_ISFIRST, code);
    }

    /**
     * 获取语言偏好
     * 0 简体中文，1 英文
     *
     * @return
     */
    public static int getLanguageCode() {
        return SpUtil.getInstance(MyApp.getApp()).getInt(KEY_APP_LANGUAGE, 0);
    }

    public static void saveLanguageCode(int code) {
        SpUtil.getInstance(MyApp.getApp()).saveInt(KEY_APP_LANGUAGE, code);
    }

    /**
     * 获取当前用户is_login
     *
     * @return
     */
    public static String getUserLogin() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_IS_LOGIN, "");
    }

    public static void saveUserLogin(String is_login) {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_IS_LOGIN, is_login);
    }

    /**
     * 保存用户信息
     *
     * @param is_login     是否登录
     * @param invitation   邀请码
     * @param phone        手机号
     * @param userId       用户id
     * @param userName     用户名
     * @param userActivate 用户激活状态
     */
    public static void saveUserInfo(String is_login, String invitation, String phone, String userId, String userName, String userActivate) {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_IS_LOGIN, is_login);
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_PHONE, phone);
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_INVITATION, invitation);
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_ID, userId);
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_NAME, userName);
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_ACTIVATE, userActivate);
        SpUtil.getInstance(MyApp.getApp()).saveBoolean(IS_LOGIN, true);

    }

    /**
     * 得到当前钱包名称
     *
     * @return
     */
    public static String getWalletName() {
        String this_wallet_name = SpUtil.getInstance(MyApp.getApp()).getString("this_wallet_name", "");
        return this_wallet_name;
    }

    public static void saveWalletName(String wallet_name){
        SpUtil.getInstance(MyApp.getApp()).saveString("this_wallet_name",wallet_name);
    }

    public static void saveUsetLogin(String is_login) {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_IS_LOGIN, is_login);
    }

    /**
     * 获取当前用户激活状态
     *
     * @return
     */
    public static String getUserActivate() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_USER_ACTIVATE, "");
    }

    public static String isLogin() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_IS_LOGIN, "");
    }

    /**
     * 获取当前用户名
     *
     * @return
     */
    public static String getUserName() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_USER_NAME, "");
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    public static String getUserId() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_USER_ID, "");
    }

    /**
     * 获取当前用户手机号
     *
     * @return
     */
    public static String getUserPhone() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_PHONE, "");
    }

    /**
     * 获取当前邀请码
     *
     * @return
     */
    public static String getUserInvitation() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_INVITATION, "");
    }

    public static void saveUserInvitation(String invitation) {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_INVITATION, invitation);
    }

    public static void loginOut() {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_IS_LOGIN, "");
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_PHONE, "");
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_INVITATION, "");
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_ID, "");
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_NAME, "");
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_USER_ACTIVATE, "");
        SpUtil.getInstance(MyApp.getApp()).saveBoolean(IS_LOGIN, false);
    }

    /**
     * 获取当前定位信息
     *
     * @return
     */
    public static String getLocation() {
        return SpUtil.getInstance(MyApp.getApp()).getString(KEY_CURRENT_LOCATION, "");
    }

    public static void saveLocation(String token) {
        SpUtil.getInstance(MyApp.getApp()).saveString(KEY_CURRENT_LOCATION, token);
    }


    //获取当前所在公链
    public static  void savePublicCoin(String coin){
        com.mugui.block.util.SpUtil.getInstance(DataSave.app).saveString(PUBLIC_COIN, coin);
    }

    //得到当前所在公链
    public static String getPublicCoin(){
        return com.mugui.block.util.SpUtil.getInstance(DataSave.app).getString(PUBLIC_COIN,"");
    }

}
