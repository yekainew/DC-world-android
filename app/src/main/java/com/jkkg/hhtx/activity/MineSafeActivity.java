package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.net.bean.MethodQueryBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.kenai.jffi.Main;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/17/2020 13:57
 * Email:chencw0715@163.com
 * 安全中心
 */
public class MineSafeActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.safe_certification_type)
    TextView safeCertificationType;
    @BindView(R.id.safe_certification)
    RelativeLayout safeCertification;
    @BindView(R.id.safe_change_dlpassword)
    TextView safeChangeDlpassword;
    @BindView(R.id.safe_change_paypassword)
    TextView safeChangePaypassword;
    @BindView(R.id.safe_wallet)
    TextView safeWallet;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.safe_certification_img)
    ImageView safeCertificationImg;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_safe;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_common2));
        qureyMethod();
    }

    private void qureyMethod() {
        MyApp.requestSend.sendData("verified1.method.query", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String s = message.getDate().toString();
                MethodQueryBean methodQueryBean = GsonUtil.gsonToBean(s, MethodQueryBean.class);
                switch (methodQueryBean.getUser_account_static()) {
                    case 0:
                        safeCertificationType.setText(getString(R.string.string_swit_shenhe));
                        break;
                    case 2:
                        safeCertificationType.setText(getString(R.string.string_shenhe_success));
                        safeCertificationType.setTextColor(getResources().getColor(R.color.hint_color_text));
                        safeCertification.setEnabled(false);
                        safeCertificationImg.setVisibility(View.GONE);
                        break;
                    case 3:
                        safeCertificationType.setText(getString(R.string.string_shenhe_fail));
                        break;
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    @OnClick({R.id.safe_certification, R.id.safe_change_dlpassword, R.id.safe_change_paypassword, R.id.safe_wallet, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.safe_certification:
                //实名认证
                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                    showMessage(getString(R.string.string_tip_safe_certification));
                } else {
                    startActivity(new Intent(this, CertificationActivity.class));
                }

                break;
            case R.id.safe_change_dlpassword:
                //修改钱包密码
                startActivity(new Intent(this,ModifyWalletPasswordActivity.class));
                break;
            case R.id.safe_change_paypassword:
                //修改支付密码
                showMessage(getString(R.string.string_text_mine_null));
                break;
            case R.id.safe_wallet:
                //备份钱包
                startActivity(new Intent(this,CreateSelectActivity.class));
                break;
            case R.id.btn_back:
                //退出登录
                EventBus.getDefault().post(new EventImpl.loginOut());
//                Constants.loginOut();
             /*   AppManager.getAppManager().killAll(MineSafeActivity.class);

                startActivity(new Intent(this, MainActivity.class));
                finish();*/
                break;
            default:
                break;
        }
    }

}
