package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.MyWebView;

import butterknife.BindView;

public class DetailHashActivity extends BaseActivity {

    @BindView(R.id.web)
    MyWebView web;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_detail_hash;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String hash = intent.getStringExtra("hash");
        https://blockchaindc.io/#/transferDetail/e749deaaec1052a701376c42af725600bc2c61be11a2c672c1e4504078b85159
        web.loadUrl(" https://blockchaindc.io/#/transferDetail/"+hash);


    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
            web.removeAllViews();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack();// activityBaseWebAddWebView.reload();
            web.removeAllViews();//删除webview中所以进程
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
