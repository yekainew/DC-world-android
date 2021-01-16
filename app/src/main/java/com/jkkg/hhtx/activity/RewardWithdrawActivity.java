package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.CurrencyLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.NodeEmancipationBean;
import com.jkkg.hhtx.net.bean.Wallet;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.net.bean.WalletLog;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.apache.commons.collections4.map.HashedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 社区奖励 提至余额
 */
public class RewardWithdrawActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.yield_num)
    TextView yieldNum;
    @BindView(R.id.yes_yi)
    TextView yesYi;
    @BindView(R.id.yes_ti)
    TextView yesTi;
    @BindView(R.id.ti_yue)
    Button tiYue;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;
    String type = "";

    /**
     * 认购 5  社区 4
     */
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_reward_withdraw;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        NodeEmancipationBean date = (NodeEmancipationBean) intent.getSerializableExtra("date");
        if (date != null) {
            yieldNum.setText(date.getAmount().stripTrailingZeros().toPlainString() + date.getDc_type_name());
            yesYi.setText("已提：" + date.getExtracted().stripTrailingZeros().toPlainString() + date.getDc_type_name());
            yesTi.setText("可提：" + date.getExtractable().stripTrailingZeros().toPlainString() + date.getDc_type_name());
        }


        toolbarTitle.setText("提至余额");
        type = intent.getAction();
        tiYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardWithdrawActivity.this, IncomeWithdrawalActivity.class);
                intent.putExtra("date", date.getExtractable().stripTrailingZeros().toPlainString());
                intent.putExtra("type", type);
                startActivityForResult(intent, 303);
            }
        });
        myAsset();
    }

    public void myAsset() {
        Map<String, String> map = new HashedMap<String, String>() {
            {
                put("currency_name", "DC");
                if (type.equals("5")) {
                    put("wallet_type","8");
                }else{
                    put("wallet_type","7");
                }
            }
        };
    MyApp.requestSend.sendData("app.wallet.wallet", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                WalletDCBean walletDCBean = WalletDCBean.newBean(WalletDCBean.class, message.getDate());
                int wallet_id = walletDCBean.getWallet_id();
                tibi(wallet_id);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    public void tibi(int wallet_id) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("wallet_id", wallet_id + "");
                put("pageNum", 1 + "");
                put("pageSize", 2000 + "");
            }
        };
        MyApp.requestSend.sendData("app.wallet.findwalletLog", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<WalletLog> walletLogs = JSONArray.parseArray(message.getDate().toString(), WalletLog.class);
                CurrencyLogAdapter currencyLogAdapter=new CurrencyLogAdapter(walletLogs,RewardWithdrawActivity.this);
                noticeRec.setLayoutManager(new LinearLayoutManager(RewardWithdrawActivity.this));
                noticeRec.setAdapter(currencyLogAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }
}
