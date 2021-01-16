package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.FinancesListAdapter;
import com.jkkg.hhtx.adapter.FinancesProfitListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.json.JSONUtil;
import jnr.ffi.annotations.In;

/**
 * 理财收益列表
 */
public class ManageFinancesYeildActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    private String all;
    private FinancesProfitListAdapter financesProfitListAdapter;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_finances_yeild;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        all = intent.getStringExtra("all");
        yieldNum.setText(all);

        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_earning_list));

        financesProfitListAdapter = new FinancesProfitListAdapter(new JSONArray(), this);
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(financesProfitListAdapter);
        initDate();
        yesti();
    }

    private void initDate() {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("pageNum", "1");
                put("pageSize", "200");
            }
        };

        MyApp.requestSend.sendData("manageFinances.app.method.financialEarningsRecord", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                JSONArray objects = JSONArray.parseArray(message.getDate().toString());
                financesProfitListAdapter.setData(objects);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    private String usable;

    private void yesti() {
        Map<String, String> map = new HashedMap<String, String>() {
            {
                put("currency_name", "USDT");
                put("wallet_type", "5");
            }
        };
        MyApp.requestSend.sendData("app.wallet.wallet", map).main(new NetCall.Call() {


            @Override
            public Message ok(Message message) {
                WalletDCBean walletDCBean = WalletDCBean.newBean(WalletDCBean.class, message.getDate());
                usable = walletDCBean.getUsable();
                yesTi.setText(getString(R.string.string_can_mention) + usable);
                String s = new BigDecimal(all).subtract(new BigDecimal(usable)).stripTrailingZeros().toPlainString();
                yesYi.setText(getString(R.string.string_mentioned) + s);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @OnClick(R.id.ti_yue)
    public void onViewClicked() {
        //提现
        Intent intent = new Intent(this, IncomeWithdrawalActivity.class);
        intent.putExtra("date", usable);
        intent.putExtra("type", "3");
        startActivityForResult(intent, 303);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 303) {
            if (resultCode == 101) {
                yesti();
            }
        }
    }
}
