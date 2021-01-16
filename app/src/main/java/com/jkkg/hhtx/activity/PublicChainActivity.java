package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.MyWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.core.util.StrUtil;

public class PublicChainActivity extends BaseActivity {

    @BindView(R.id.web)
    MyWebView web;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_public_chain;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (StrUtil.isNotBlank(url)) {
            web.loadUrl(url);
        }else{
            web.loadUrl("https://blockchaindc.io");
        }
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
