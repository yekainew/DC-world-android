package com.jkkg.hhtx.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.CustomVideoView;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import constant.UiType;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import listener.UpdateDownloadListener;
import me.jessyan.autosize.internal.CancelAdapt;
import model.UiConfig;
import model.UpdateConfig;
import razerdp.widget.QuickPopup;
import update.UpdateAppUtils;

/**
 * 启动页
 *
 * @author admin6
 */
public class FlashActivity extends BaseActivity implements CancelAdapt {
    @Autowired
    public Block block;
    Disposable disposable;
    @BindView(R.id.video)
    CustomVideoView video;
    private String apkUrl = "";
    private String updateTitle = "发现新版本！";
    private String updateContent = "1、修复部分bug\n2、重绘部分页面\n3、增加安全性\n4、更多功能等你探索";

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_flash;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        final String uri = "android.resource://" + getPackageName() + "/" + R.raw.start_video;

        video.setVideoURI(Uri.parse(uri));
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(false);
            }
        });


        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
        toP();

    }

    private void toP() {
        RxPermissions rxPermissions = new RxPermissions(FlashActivity.this);
        disposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            //表示用户不同意权限
                            showMessage(getString(R.string.string_failed_to_get_permission));
                            finish();
                        }
//                        toMainActivity();
                        upDate();
                    }
                });
    }

    QuickPopup quickPopup;

    private void upDate() {

        String appVersionCode = getAppVersionCode(this);
        String appVersionName = getAppVersionName(this);

        Map<String, String> map = new HashMap<String, String>() {
            {
                put("name", "安卓版本");
                put("value", appVersionCode);
            }
        };

        MyApp.requestSend.sendData("app.check.updata", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                if (message.getType() == 200) {
                    apkUrl = message.getDate().toString();

                    UpdateConfig updateConfig = new UpdateConfig();
                    updateConfig.setCheckWifi(true);
                    updateConfig.setNeedCheckMd5(true);
                    updateConfig.setForce(true);
                    updateConfig.setNeedCheckMd5(false);
                    updateConfig.setNotifyImgRes(R.mipmap.ic_launcher);

                    UiConfig uiConfig = new UiConfig();
                    uiConfig.setUiType(UiType.PLENTIFUL);

                    UpdateAppUtils.getInstance().apkUrl(apkUrl)
                            .updateTitle(updateTitle)
                            .updateContent(updateContent)
                            .uiConfig(uiConfig)
                            .updateConfig(updateConfig)
                            .setUpdateDownloadListener(new UpdateDownloadListener() {
                                @Override
                                public void onStart() {
                                    Log.d("FlashActivity", "启动");
                                }

                                @Override
                                public void onDownload(int i) {
                                    Log.d("FlashActivity", "下载中" + i);
                                }

                                @Override
                                public void onFinish() {
                                    Log.d("FlashActivity", "下载完成");
                                }

                                @Override
                                public void onError(@NotNull Throwable throwable) {
                                    Log.d("FlashActivity", "下载错误");
                                }
                            })
                            .update();

                    /*quickPopup = QuickPopupBuilder.with(getContext())
                            .contentView(R.layout.pop_update)
                            .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                                    .backpressEnable(true)
                                    .outSideTouchable(false)
                                    .outSideDismiss(false)
                                    .withShowAnimation(AnimationHelper.asAnimation()
                                            .withAlpha(AlphaConfig.IN)
                                            .toShow())
                                    .withDismissAnimation(AnimationHelper.asAnimation()
                                            .withAlpha(AlphaConfig.OUT)
                                            .toDismiss())
                                    .withClick(R.id.quit_btn, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            quickPopup.dismiss();
                                            killAppProcess();
                                        }
                                    })
                                    .withClick(R.id.start_btn, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            *//*Intent intent= new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(message.getDate().toString());
                                            intent.setData(content_url);
                                            startActivity(intent);*//*
                                        }
                                    }))
                            .show();*/
                } else {

                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                Log.d("TAG_Flash", "走到这里了2");
                toMainActivity();
                return Message.ok();
            }
        });
    }

    private void toMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean is_getWallet = SpUtil.getInstance(FlashActivity.this).getBoolean("is_getWallet", false);
                Log.d("TAG_Flash", "走到这里了2");
                if (is_getWallet) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    if (block.existWallet(SpUtil.getInstance(FlashActivity.this).getString("this_wallet_name", ""))) {
                        block.removeWallet(SpUtil.getInstance(FlashActivity.this).getString("this_wallet_name", ""));
                    }
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                }
            /*    String is_login = Constants.getUserLogin();
                if (StringUtils.isEmpty(is_login)) {
                    //是否登录
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                } else {

                }*/
                request();
            }
        }, 4000);
    }


    @Autowired
    Dao dao;

    private void request() {
        boolean b = block.existWallet(SpUtil.getInstance(this).getString("this_wallet_name", ""));
        if (!b) return;
        String is_login = Constants.getUserLogin();
        if (StringUtils.isBlank(is_login)) {
            return;
        }
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(this).getString("this_wallet_name", "")));
        Map<String, Object> requestMap1 = new ArrayMap<>();
        requestMap1.put("is_login", is_login);
        System.out.println("本地地址》》》" + this_wallet_name.getAddress());
        requestMap1.put("bind_address", this_wallet_name.getAddress());
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        MyApp.requestSend.sendData("user.login.token", GsonUtil.toJsonString(requestMap1)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                User user = User.newBean(User.class, data);
//                String bindState = user.get("bindState").toString();
                String is_login = user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();

                UserWalletBean select = dao.select(new UserWalletBean().setAddress(this_wallet_name.getAddress()));
                if (select == null) {
                    dao.save(new UserWalletBean().setAddress(this_wallet_name.getAddress()).setName(SpUtil.getInstance(FlashActivity.this).getString("this_wallet_name", "")).setToken(is_login));
                } else {
                    dao.updata(select.setAddress(this_wallet_name.getAddress()).setName(SpUtil.getInstance(FlashActivity.this).getString("this_wallet_name", "")).setToken(is_login));
                }
//                if (StringUtils.isNotEmpty(bindState) && getString(R.string.string_zhu_or_si_null).equals(bindState)) {
//                    startActivity(new Intent(getContext(), MemberActivity.class));
//                } else {
                Constants.saveUserInfo(is_login, user_invitation, user_phone, user_id, user_name, is_activate);
//                    startActivity(new Intent(getContext(), MainActivity.class));
//                }
//                finish();
//                overridePendingTransition(0, 0);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                //返回错误信息
                Constants.loginOut();

                dao.updata(this_wallet_name.setToken(""));
//                startActivity(new Intent(getContext(), LoginActivity.class));
//                finish();
//                overridePendingTransition(0, 0);
                return Message.ok();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public void killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) FlashActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != Process.myPid()) {
                Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
