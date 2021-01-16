package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ActivationByRecordAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.WalletBasesBeans;
import com.jkkg.hhtx.net.bean.WalletLogBeans;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购买激活次数记录
 * item_jihuo_buynum
 */
public class ActivationBuyRecordActivity extends BaseActivity {

    @BindView(R.id.buy_number)
    TextView buyNumber;
    @BindView(R.id.assets_rec)
    RecyclerView assetsRec;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_activation_buy_record;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        exchangeNumTotal();
        findwallet();
    }

    private void exchangeNumTotal() {
        MyApp.requestSend.sendData("invite.method.exchangeNumTotal").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                buyNumber.setText(message.getDate().toString());
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void findwallet() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("wallet_type", "2");
        MyApp.requestSend.sendData("app.wallet.findwallet", jsonObject).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                WalletBasesBeans walletBasesBeans = gson.fromJson(message.getDate().toString(), WalletBasesBeans.class);
                List<WalletBasesBeans.ListBean> list = walletBasesBeans.getList();


                for (WalletBasesBeans.ListBean listBean : list) {
                    if (listBean.getCurrency_name().equals("DC")) {


                        findwalletLog(listBean.getWallet_id()+"");
                    }
                }

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void findwalletLog(String walletId) {
        Map<String, String> map = new ArrayMap<>();
        map.put("log_mode","70");
        map.put("wallet_id", walletId);
        map.put("pageNum", 1 + "");
        map.put("pageSize", 2000 + "");

        MyApp.requestSend.sendData("app.wallet.findwalletLog", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                List<WalletLogBeans> o = gson.fromJson(message.getDate().toString(), new TypeToken<List<WalletLogBeans>>() {
                }.getType());
                ActivationByRecordAdapter activationByRecordAdapter = new ActivationByRecordAdapter(o, getContext());
                assetsRec.setLayoutManager(new LinearLayoutManager(getContext()));
                assetsRec.setAdapter(activationByRecordAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}