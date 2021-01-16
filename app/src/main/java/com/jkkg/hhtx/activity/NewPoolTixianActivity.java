package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * new pool
 * 提余额
 */
public class NewPoolTixianActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.all_dc)
    TextView allDc;
    @BindView(R.id.keti_dc)
    TextView ketiDc;
    @BindView(R.id.choose_type)
    LinearLayout chooseType;
    @BindView(R.id.yue_usdt)
    TextView yueUsdt;
    @BindView(R.id.get_dc1)
    TextView getDc1;
    @BindView(R.id.get_dc2)
    TextView getDc2;
    @BindView(R.id.get_dc3)
    TextView getDc3;
    @BindView(R.id.edit_psw)
    ClearEditText editPsw;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.type_name)
    TextView typeName;
    private PoolConfigBean poolConfigBean;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_tixian;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        poolConfigBean = (PoolConfigBean) intent.getSerializableExtra("poolConfigBean");
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myAsset();
        editNum.setHint("请输入" + poolConfigBean.getFreed_type_name() + "数量");
        typeName.setText(poolConfigBean.getFreed_type_name());
        AssetsBean usdt = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("USDT"));
        yueUsdt.setText(usdt.getSymbol()+"余额："+usdt.getNum().stripTrailingZeros().toPlainString());
    }

    @Autowired
    Dao dao;

    @OnClick({R.id.all_dc, R.id.choose_type, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_dc:
                //全部dc
                break;
            case R.id.choose_type:
                //选择
                break;
            case R.id.btn_ture:
                //确认
                break;
        }
    }

    /**
     * 我的资产
     */
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
                ketiDc.setText("可提数量  " + walletDCBean.getUsable() + " " + poolConfigBean.getFreed_type_name());

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