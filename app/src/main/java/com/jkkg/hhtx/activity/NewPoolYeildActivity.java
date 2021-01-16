package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.PoolProfitListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.jkkg.hhtx.net.bean.UserHoldIncomeBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * new pool
 * 收益明细
 * item item_newpool_yeild
 */

public class NewPoolYeildActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.jilu)
    ImageView jilu;
    @BindView(R.id.yield_all_num)
    TextView yieldAllNum;
    @BindView(R.id.yesterday_yeild)
    TextView yesterdayYeild;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartref)
    SmartRefreshLayout smartref;
    @BindView(R.id.tixian)
    Button tixian;
    @BindView(R.id.jinrishouyi)
    TextView jinrishouyi;
    @BindView(R.id.yes_yi)
    TextView yesYi;
    @BindView(R.id.yes_ti)
    TextView yesTi;
    private PoolProfitListAdapter poolYieldListAdapter;

    int page = 1;
    private String freed_type_name;
    private PoolConfigBean poolConfigBean;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_yeild;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();


        poolConfigBean = (PoolConfigBean) intent.getSerializableExtra("poolConfigBean");
        freed_type_name = poolConfigBean.getFreed_type_name();

        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        jinrishouyi.setText("今日收益(" + freed_type_name + ")");
        poolYieldListAdapter = new PoolProfitListAdapter(new ArrayList<>(), this, freed_type_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(poolYieldListAdapter);

        incomeLog();
        nowIncome();
        myAsset();
        smartref.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                incomeLog();
                smartref.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                incomeLog();
                smartref.finishRefresh();
            }
        });

    }

    private void incomeLog() {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("pageNum", page + "");
                put("pageSize", "50");
            }
        };
        MyApp.requestSend.sendData("holdarea.method.income_log", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<UserHoldIncomeBean> o = JSONArray.parseArray(message.getDate().toString(), UserHoldIncomeBean.class);

                if (page == 1) {
                    poolYieldListAdapter.setData(o, freed_type_name);
                } else {
                    if (o.size() != 0) {
                        poolYieldListAdapter.upData(o, freed_type_name);
                    }
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
    private String s;
    public void nowIncome() {
        MyApp.requestSend.sendData("holdarea.method.nowIncome").main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                s = message.getDate().toString();
                yieldAllNum.setText(s + freed_type_name);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @OnClick({R.id.jilu, R.id.tixian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jilu:
                //记录
                startActivity(new Intent(this,NewPoolTiquJiluActivity.class));
                break;
            case R.id.tixian:
                Intent intent = new Intent(this, NewPoolTixianActivity.class);
                intent.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent);
                break;

        }
    }


    public void myAsset() {
        Map<String, String> map = new HashedMap<String, String>() {
            {
                put("currency_name", poolConfigBean.getFreed_type_name());
                put("wallet_type", "1");
            }
        };
        MyApp.requestSend.sendData("app.wallet.wallet", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                WalletDCBean walletDCBean = WalletDCBean.newBean(WalletDCBean.class, message.getDate());
                yesTi.setText("可提："+walletDCBean.getUsable()+" "+poolConfigBean.getFreed_type_name());

                if (s!=null) {
                    BigDecimal subtract = new BigDecimal(s).subtract(new BigDecimal(walletDCBean.getUsable()));
                    yesYi.setText("已提："+subtract.stripTrailingZeros().toPlainString()+" "+poolConfigBean.getFreed_type_name());
                }

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


}
