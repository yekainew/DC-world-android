package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/12/2020 17:27
 * Email:chencw0715@163.com
 * h5 协议
 */
public class AgreementActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.fg_wallet_cb)
    CheckBox fgWalletCb;
    @BindView(R.id.fg_wallet_cb_ll)
    LinearLayout fgWalletCbLl;
    @BindView(R.id.tv_web)
    WebView flWeb;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_agreement;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.String_user_agreement);
//        getData();
    }

    private void getData() {
        MyApp.requestSend.sendData("app.info.webServiceUrl").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String s = message.getDate().toString();
                flWeb.loadUrl(s);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @OnClick({R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_true:
                //确认进入注册界面
                if (!fgWalletCb.isChecked()) {
                    showMessage(getString(R.string.string_user_agreement_text));
                } else {
                    Intent intent = new Intent(this, CreateUserActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
