package com.jkkg.hhtx.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.ActivationUserActivity;
import com.jkkg.hhtx.activity.CreateSelectActivity;
import com.jkkg.hhtx.activity.HelpWebActivity;
import com.jkkg.hhtx.activity.ManageWalletActivity;
import com.jkkg.hhtx.activity.MemberActivity;
import com.jkkg.hhtx.activity.MineAboutUserActivity;
import com.jkkg.hhtx.activity.MineAddressBookActivity;
import com.jkkg.hhtx.activity.MineAssetActivity;
import com.jkkg.hhtx.activity.MineBookActivity;
import com.jkkg.hhtx.activity.MineCommunityActivity;
import com.jkkg.hhtx.activity.MineHelpActivity;
import com.jkkg.hhtx.activity.MineNewsManagementActivity;
import com.jkkg.hhtx.activity.MineSystemNoticeActivity;
import com.jkkg.hhtx.activity.MineSystemSetActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.net.bean.HelpBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.ImageUtils;
import com.jkkg.hhtx.utils.JsonUtils;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.king.zxing.util.CodeUtils;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;


/**
 * Description:
 * Created by ccw on 09/08/2020 15:23
 * 我的
 * Email:chencw0715@163.com
 */
public class MineFragment extends BaseFragment {
    /*
        @BindView(R.id.copy)
        ImageView copy;*/
    @BindView(R.id.iv_mine_head)
    ImageView ivMineHead;
    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.tv_mine_activate)
    TextView tvMineActivate;
    @BindView(R.id.mine_common_tab1)
    TextView mineCommonTab1;
    @BindView(R.id.mine_common_tab2)
    LinearLayout mineCommonTab2;
    @BindView(R.id.mine_common_tab4)
    TextView mineCommonTab4;
    @BindView(R.id.mine_common_tab5)
    TextView mineCommonTab5;
    @BindView(R.id.mine_common_tab6)
    TextView mineCommonTab6;
    @BindView(R.id.mine_common_tab7)
    TextView mineCommonTab7;
    @BindView(R.id.mine_common_tab8)
    TextView mineCommonTab8;
    @BindView(R.id.mine_common_tab9)
    TextView mineCommonTab9;
    @BindView(R.id.mine_common_tab10)
    LinearLayout mineCommonTab10;
    @BindView(R.id.mine_common_tab12)
    LinearLayout mineCommonTab12;
    @BindView(R.id.mine_guanli_wallet)
    LinearLayout mine_guanli_wallet;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.layout_mine)
    LinearLayout layoutMine;
    /*    @BindView(R.id.tv_lanauage)
        TextView tvLanauage;*/
    BasePopupWindow languagePopupWindow;
    @BindView(R.id.tv_mine_code)
    TextView tvMineCode;
    @BindView(R.id.lin_about)
    LinearLayout linAbout;
    @BindView(R.id.mine_saoma)
    ImageView mineSaoma;
    @BindView(R.id.mine_qiandao)
    ImageView mineQiandao;
    @BindView(R.id.jiedian)
    TextView jiedian;
    @BindView(R.id.mine_common_beifen_wallet)
    TextView mineCommonBeifenWallet;
    @BindView(R.id.mine_common_tab11)
    TextView mineCommonTab11;

    private Intent intent;
    boolean isThread = false;
    private boolean islogin;
    boolean isType = false;
    private Disposable disposable;
    BasePopupWindow InvitePopupWindow;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            String login = Constants.isLogin();
            if (StrUtil.isBlank(login)) {
                tvMineActivate.setText("未验证");

                tvMineActivate.setTextColor(getResources().getColor(R.color.font_color_yellow));
            } else {
                if (s.equals("")) {
                    tvMineActivate.setText(R.string.string_text_tobeactivate);
                    tvMineActivate.setTextColor(getResources().getColor(R.color.textred));
                } else {
                    if ("ToBeActivated".equals(s)) {
                        tvMineActivate.setText(R.string.string_text_tobeactivate);
                        tvMineActivate.setTextColor(getResources().getColor(R.color.textred));
                    } else {
                        tvMineActivate.setText(R.string.string_text_activate);
                        tvMineActivate.setTextColor(getResources().getColor(R.color.green));
                    }
                }
            }


        }
    };

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        tvMineName.setText(Constants.getWalletName());
     /*   if (Constants.getLanguageCode() == 0) {
            tvLanauage.setText(R.string.string_language_cn);
        } else if (Constants.getLanguageCode() == 1) {
            tvLanauage.setText(R.string.string_language_en);
        }*/
        initDate();
    }

    private void initDate() {
        String appVersionName = getAppVersionName(getContext());
        tvMineCode.setText("V" + appVersionName);
    }


    @Autowired
    Dao dao;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginOut(EventImpl.loginOut baseEvent) {
        isThread = false;
    }

    @Override
    public void onResume() {
        islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
//        tvMineUid.setText(select.getAddress());
        if (!islogin) {
            isThread = false;
            tvMineName.setText(Constants.getWalletName());

        } else {
            isThread = true;
            tvMineName.setText(Constants.getWalletName());

        }

        String login = Constants.isLogin();
        if (StrUtil.isBlank(login)) {
            tvMineActivate.setText("未验证");

            tvMineActivate.setTextColor(getResources().getColor(R.color.font_color_yellow));
        } else {
            if (Constants.getUserActivate().equals("")) {
                tvMineActivate.setText(R.string.string_text_tobeactivate);
                tvMineActivate.setTextColor(getResources().getColor(R.color.textred));
            } else {
                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                    tvMineActivate.setText(R.string.string_text_tobeactivate);
                    tvMineActivate.setTextColor(getResources().getColor(R.color.textred));
                } else {
                    tvMineActivate.setText(R.string.string_text_activate);
                    tvMineActivate.setTextColor(getResources().getColor(R.color.green));
                }
            }
        }

        if (!isType && isThread) {
            isType = true;
            ThreadUtil.execAsync(new Runnable() {
                @Override
                public void run() {
                    while (isThread && isType) {
                        findUserAccount();
                        Other.sleep(3000);
                    }
                }
            });
        }
        super.onResume();
    }

    private void findUserAccount() {
        MyApp.requestSend.sendData("app.userAccount.findUser").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String user_sub = JsonUtils.getString(message.getDate(), "user_sub");
                Constants.saveUserInvitation(user_sub);
                android.os.Message message1 = new android.os.Message();
                message1.obj = user_sub;
                handler.sendMessage(message1);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

//    private void reStartActivity() {
//        Intent intent = getIntent();
//        finish();
//        overridePendingTransition(R.anim.fade_entry, R.anim.fade_exit);
//        startActivity(intent);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThread = false;
        isType = false;
        if (languagePopupWindow != null && languagePopupWindow.isShowing()) {
            languagePopupWindow.onDestroy();
            languagePopupWindow.dismiss();
        }
        languagePopupWindow = null;

        if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
            InvitePopupWindow.onDestroy();
            InvitePopupWindow.dismiss();
        }
        InvitePopupWindow = null;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isThread = false;
        isType = false;
    }
//    R.id.tv_lanauage,
//    R.id.copy,

    @OnClick({R.id.tv_mine_activate, R.id.mine_common_beifen_wallet,
            R.id.mine_common_tab1, R.id.mine_common_tab2, R.id.mine_common_tab4,
            R.id.mine_common_tab5, R.id.mine_common_tab6, R.id.mine_common_tab7, R.id.mine_common_tab8,
            R.id.mine_common_tab9, R.id.lin_about, R.id.mine_common_tab12, R.id.mine_common_tab10, R.id.mine_guanli_wallet, R.id.mine_common_tab11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mine_activate:
                //未激活
                String login = Constants.isLogin();
                if (StrUtil.isBlank(login)) {
                    tvMineActivate.setText("未验证");
                    startActivity(new Intent(getContext(),MemberActivity.class));
                } else {
                    if (getString(R.string.string_text_tobeactivate).equals(tvMineActivate.getText().toString())) {
                        showMessage(getString(R.string.string_text_mine_activate));
                    } else {
                        showMessage(getString(R.string.string_text_activate));
                    }
                }
                break;
            case R.id.mine_common_tab1:
                //我的社区

                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                intent = new Intent(mContext, MineCommunityActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_common_beifen_wallet:
                //备份钱包
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                intent = new Intent(mContext, CreateSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_common_tab2:
                //账本明细

                intent = new Intent(mContext, MineBookActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_common_tab4:
                //激活旷工
                //账本
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                intent = new Intent(mContext, ActivationUserActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_common_tab5:
                //邀请
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                //分享海报
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                disposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    requestWebRegUrl();
                                    requestWebDownUrl();
                                    //表示用户同意权限
                                    showPop();
                                } else {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_user_refuse_authority));
                                }
                            }
                        });
                break;
            case R.id.mine_common_tab6:
                //系统公告
                startActivity(new Intent(mContext, MineSystemNoticeActivity.class));
                break;
            case R.id.mine_common_tab7:
                //资讯管理
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                intent = new Intent(mContext, MineNewsManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_common_tab8:
                //留言
                break;
            case R.id.mine_common_tab9:
                //客服
                startActivity(new Intent(getContext(), MineHelpActivity.class));
//                showMessage("暂未开放");
                break;
            case R.id.mine_common_tab10:
                //地址簿
                startActivity(new Intent(getContext(), MineAddressBookActivity.class).setAction("2"));
//                showMessage("暂未开放");
                break;
            case R.id.lin_about:
                //关于我们=
                startActivity(new Intent(getContext(), MineAboutUserActivity.class));
//                help();
//                showMessage("暂未开放");
                break;
            case R.id.mine_common_tab12:
                showMessage("暂未开放");
//                startActivity(new Intent(getContext(), MineAssetActivity.class));
                break;

            case R.id.mine_guanli_wallet:
                showMessage("暂未开放");
//                startActivity(new Intent(getContext(), ManageWalletActivity.class));
                break;

            case R.id.mine_common_tab11:
                //系统设置
//                showMessage("暂未开放");
                startActivity(new Intent(getContext(), MineSystemSetActivity.class));
                break;
            default:
                break;
        }
    }



    private void requestWebRegUrl() {
        MyApp.requestSend.sendData("app.info.webRegUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                if (InvitePopupWindow != null) {
                  /*  ImageView login_join = InvitePopupWindow.findViewById(R.id.login_join);
                    Bitmap qr = CodeUtils.createQRCode(data, ScreenSizeUtil.dp2px(getContext(), 50));
                    GlideApp.with(getContext()).load(qr).into(login_join);*/
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void requestWebDownUrl() {
        MyApp.requestSend.sendData("app.info.webDownUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                if (InvitePopupWindow != null) {
                    ImageView download_app = InvitePopupWindow.findViewById(R.id.download_app);
                    Bitmap qr = CodeUtils.createQRCode(data, ScreenSizeUtil.dp2px(getContext(), 60));
                    Glide.with(getContext()).load(qr).into(download_app);
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }


    private void showPop() {
        InvitePopupWindow = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.layout_pop_invite)
                .config(new QuickPopupConfig().gravity(Gravity.BOTTOM)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.FROM_BOTTOM)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.TO_BOTTOM)
                                .toDismiss())
                        .withClick(R.id.dialog_left_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InvitePopupWindow.dismiss();
                            }
                        })
                        .withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
                                    RelativeLayout rl_invite = InvitePopupWindow.findViewById(R.id.rl_invite);
                                    //保存这个视图就可以
                                    Bitmap bitmap = ImageUtils.screenShotView(rl_invite);
                                    ImageUtils.savePicture(getContext(), bitmap);
                                    InvitePopupWindow.dismiss();
                                }
                            }
                        })).show();

        try {
            //设置要显示的图片
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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


}
