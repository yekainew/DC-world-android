package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ExchangeLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeBean1;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.List;

import butterknife.BindView;

/**
 * 兑换记录
 * item_exchange_record
 */
public class ExchangeRecordActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange_record;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_exchange_record));
        toolbarImageLeft.setVisibility(View.VISIBLE);
        log();
    }

    private void log() {
        MyApp.requestSend.sendData("exchange.method.log").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<ExchangeBean1> exchangeBean1s = JSONArray.parseArray(message.getDate().toString(), ExchangeBean1.class);
                ExchangeLogAdapter exchangeLogAdapter = new ExchangeLogAdapter(ExchangeRecordActivity.this, exchangeBean1s);
                recyclerView.setAdapter(exchangeLogAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ExchangeRecordActivity.this));
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

}
