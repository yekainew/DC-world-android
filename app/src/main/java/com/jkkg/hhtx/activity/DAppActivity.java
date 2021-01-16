package com.jkkg.hhtx.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.widget.MyWebView;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.classutil.DataSave;

import java.util.Locale;

import butterknife.BindView;

public class DAppActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.my_web)
    MyWebView myWeb;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_d_app;
    }

    @Autowired
    Dao dao;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText("DAPP");
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);


        Context context = DataSave.app.getApplicationContext();
        SharedPreferences walletPref = context.getSharedPreferences(Constants.getWalletName(), Context.MODE_PRIVATE);
        String privateKeyEncrypted = walletPref.getString(context.getString(com.mugui.robot.mugui_block.R.string.priv_key), "");
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
        String address = select.getAddress();
        String s="https://shop.wobus.cn/dcapp/#/?param1=" + privateKeyEncrypted + "&param2=" + address;
//        type==1 自有App内打开

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
//        myWeb.loadUrl("http://dcapp.dc-chain.cn/#/?privateKeyEncrypted=" + privateKeyEncrypted + "&address=" + address+"&type=1"+"&language="+language);
        myWeb.loadUrl("http://192.168.0.106:8848/DC/html/about_us.html?"+"language="+language);
//
//        myWeb.loadUrl("http://192.168.0.106:8848/APP-DC/html/white_paper.html?language="+language);
    }
}
