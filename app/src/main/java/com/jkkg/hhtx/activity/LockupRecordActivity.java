package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONScanner;
import com.google.gson.JsonObject;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.LockUpRecordLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.LockCoinToEarnInterestLogBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 锁仓记录
 * 收益明细 LockupTixianActivity
 * 子布局 item_lockup_record
 */
public class LockupRecordActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_tv_left)
    TextView toolbarTvLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_lockup_record;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_lockup_recoed));
        toolbarTvRight.setText(getString(R.string.string_yield_mingxi));
        lockcoinlog();
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LockupRecordActivity.this,LockupTixianActivity.class));
            }
        });

    }

    private void lockcoinlog() {
        MyApp.requestSend.sendData("lockcoin.app.method.log").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                List<LockCoinToEarnInterestLogBean> lockCoinToEarnInterestLogBeans = JSONArray.parseArray(message.getDate().toString(), LockCoinToEarnInterestLogBean.class);
                LockUpRecordLogAdapter lockUpRecordLogAdapter = new LockUpRecordLogAdapter(LockupRecordActivity.this, lockCoinToEarnInterestLogBeans);
                noticeRec.setLayoutManager(new LinearLayoutManager(LockupRecordActivity.this));
                noticeRec.setAdapter(lockUpRecordLogAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
