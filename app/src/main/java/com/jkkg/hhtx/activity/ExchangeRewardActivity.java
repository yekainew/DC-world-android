package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ExchangeRewardAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeBean1;
import com.jkkg.hhtx.net.bean.ExchangeRewardBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 星际演替-奖金
 * item  item_exchange_reward
 */
public class ExchangeRewardActivity extends BaseActivity {

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
    @BindView(R.id.reward_num)
    TextView rewardNum;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    int page = 1;
    int pagenum = 200;



    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange_reward;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("奖金");
        setData();
    }
    private BigDecimal add=BigDecimal.ZERO;
    private void setData() {
        Map<String, String> map = new ArrayMap<>();
        map.put("pageNum", page + "");
        map.put("pageNum", pagenum + "");

        MyApp.requestSend.sendData("exchange.method.rewardLog",map).main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                List<ExchangeRewardBean> exchangeBean1s = JSONArray.parseArray(message.getDate().toString(), ExchangeRewardBean.class);

                for (ExchangeRewardBean exchangeBean1 : exchangeBean1s) {
                    BigDecimal reward_num = exchangeBean1.getReward_num();
                    add =add.add(reward_num);
                }

                if (add!=null) {
                    rewardNum.setText(add.stripTrailingZeros().toPlainString()+" USDT");
                }
                ExchangeRewardAdapter exchangeRewardAdapter = new ExchangeRewardAdapter(getContext(), exchangeBean1s);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(exchangeRewardAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
