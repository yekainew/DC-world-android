package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin6
 * pool 项目介绍
 */
public class PoolIntroductionActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.day_shifang)
    TextView dayShifang;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pool_introduction;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_tab_pool_introduce));

        initDate();
    }

    private void initDate() {
        MyApp.requestSend.sendData("holdarea.method.relaseTotal").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String s = message.getDate().toString();
                dayShifang.setText(s);

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}