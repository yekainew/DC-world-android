package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.PoolAllAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.WeekCountBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.base.Module;
import com.mugui.base.client.net.bean.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * new矿池
 * 矿池奖励
 * item item_pool_reward
 */
public class NewPoolAllActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.pool_all_num)
    TextView poolAllNum;
    @BindView(R.id.pool_reward_num1)
    TextView poolRewardNum1;
    @BindView(R.id.pool_reward_num2)
    TextView poolRewardNum2;
    @BindView(R.id.pool_reward_num3)
    TextView poolRewardNum3;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PoolAllAdapter poolAllAdapter;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_all;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        poolAllAdapter = new PoolAllAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(poolAllAdapter);
        weekCount();

        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void weekCount(){
        MyApp.requestSend.sendData("holdarea.method.weekCount").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                WeekCountBean weekCountBean = WeekCountBean.newBean(WeekCountBean.class, message.getDate());
                poolAllNum.setText(weekCountBean.getWeek_all());
                poolRewardNum1.setText(weekCountBean.getWeek_self_all());
                poolRewardNum2.setText(weekCountBean.getWeek_push_all());

                JSONArray data = weekCountBean.get().getJSONArray("data");
                List<WeekCountBean.DataBean> dataBeans = data.toJavaList(WeekCountBean.DataBean.class);

                poolAllAdapter.setDate(dataBeans);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
