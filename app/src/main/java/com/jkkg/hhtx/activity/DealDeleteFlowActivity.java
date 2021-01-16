package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.liys.view.LineBottomProView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易-资金池-删除流动性
 * 删除流动性 pop_deal_delete_liudong_sure
 */
public class DealDeleteFlowActivity extends BaseActivity {

    @BindView(R.id.deal_fanhui)
    ImageView dealFanhui;
    @BindView(R.id.trx_num)
    TextView trxNum;
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.deal_daibi)
    TextView dealDaibi;
    @BindView(R.id.show_daibi)
    TextView showDaibi;
    @BindView(R.id.img_zhe)
    ImageView imgZhe;
    @BindView(R.id.bottom_pro_view)
    LineBottomProView bottomProView;
    @BindView(R.id.baifenbi1)
    TextView baifenbi1;
    @BindView(R.id.baifenbi2)
    TextView baifenbi2;
    @BindView(R.id.baifenbi3)
    TextView baifenbi3;
    @BindView(R.id.baifenbi4)
    TextView baifenbi4;
    @BindView(R.id.duihuai_usdt)
    TextView duihuaiUsdt;
    @BindView(R.id.usdt_name)
    TextView usdtName;
    @BindView(R.id.duihuan_trx)
    TextView duihuanTrx;
    @BindView(R.id.trx_name)
    TextView trxName;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_deal_delete_flow;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.deal_fanhui, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.deal_fanhui:
                finish();
                break;
            case R.id.btn_ture:
                //点击移除
                break;
        }
    }
}
