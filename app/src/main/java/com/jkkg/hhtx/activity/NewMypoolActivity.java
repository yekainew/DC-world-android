package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.PowerRankListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.HoldAreaCountBean;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.jkkg.hhtx.net.bean.PowerRankListBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * new
 * 我的矿池
 * 持币排名 item_chibi_paiming
 */
public class NewMypoolActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.yeild_dc)
    TextView yeildDc;
    @BindView(R.id.yeild_cny)
    TextView yeildCny;
    @BindView(R.id.btn_yeild)
    Button btnYeild;
    @BindView(R.id.chibi)
    TextView chibi;
    @BindView(R.id.chibi_min)
    TextView chibiMin;
    @BindView(R.id.gongshi)
    TextView gongshi;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.my_shouyi)
    TextView myShouyi;
    private PowerRankListAdapter powerRankListAdapter;
    private PoolConfigBean poolConfigBean;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_mypool;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        poolConfigBean = (PoolConfigBean) intent.getSerializableExtra("poolConfigBean");

        powerRankListAdapter = new PowerRankListAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(powerRankListAdapter);
        powerRank();
        //返回
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        count();
        myAsset();
    }

    @OnClick({R.id.btn_yeild})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_yeild:
                //收益明细
                Intent intent = new Intent(this, NewPoolYeildActivity.class);
                intent.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent);
                break;
        }
    }


    /**
     * 持币区统计
     */
    private void count() {
        MyApp.requestSend.sendData("holdarea.method.count").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                JSONArray objects = JSONArray.parseArray(message.getDate().toString());
                HoldAreaCountBean holdAreaCountBean = HoldAreaCountBean.newBean(HoldAreaCountBean.class, objects.get(0));

                BigDecimal optimal_keep = holdAreaCountBean.getOptimal_keep();
                chibi.setText(CHSUtils.ghs(optimal_keep) + " GH/S");
                chibiMin.setText(holdAreaCountBean.getMin_power().stripTrailingZeros().toPlainString() + "GH/S");
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    private void powerRank() {
        MyApp.requestSend.sendData("holdarea.method.power_rank").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                List<PowerRankListBean> o = gson.fromJson(message.getDate().toString(), new TypeToken<List<PowerRankListBean>>() {
                }.getType());
                powerRankListAdapter.setData(o);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
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
                yeildDc.setText(walletDCBean.getUsable()+" "+poolConfigBean.getFreed_type_name());

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
