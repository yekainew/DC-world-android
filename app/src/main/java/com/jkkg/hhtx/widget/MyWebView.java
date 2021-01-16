package com.jkkg.hhtx.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jkkg.hhtx.R;

public class MyWebView extends WebView {
    private ProgressBar mProgressBar;

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化一个进度条控件
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        //设置params
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        //设置进度条颜色（可以根据自己的需求自定义）
        Drawable drawable = context.getResources().getDrawable(R.drawable.progressbar_webview);
        mProgressBar.setProgressDrawable(drawable);
        //将ProgressBar控件添加到MyWebView上
        addView(mProgressBar);

        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebClient());
        //设置WebSettings
        setting();

    }
    /*****设置WebSettings ****/
    private void setting() {
        WebSettings settings = getSettings();
        //支持javascript
        settings.setJavaScriptEnabled(true);
        //是否可以缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        //增加WebView对localStorage的支持
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        //不使用缓存 LOAD_NO_CACHE   优先加载缓存LOAD_CACHE_ELSE_NETWORK
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //解决图片不显示
        settings.setBlockNetworkImage(false);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //判断进度是否到了100，如果到了就要隐藏ProgressBar，如果没有就更新进度状态。
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE) {
                    mProgressBar.setVisibility(VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public class WebClient extends android.webkit.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //app内加载，防止跳转到外部的浏览器。
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //有时候需求是要获取网页中的标题，故通过接口监听来实现。
            if (titleListener != null) {
                titleListener.onTitle(view.getTitle());
            }
            super.onPageFinished(view, url);

        }

    }
    /**************** 网页的标题监听 ****************/
    TitleListener titleListener;
    public void setTitleListener(TitleListener titleListener) {
        this.titleListener = titleListener;
    }
    public interface TitleListener{
        void onTitle(String title);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //更新ProgressBar的params
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
