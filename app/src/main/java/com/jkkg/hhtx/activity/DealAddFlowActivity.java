package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易-资金池-添加流动性
 *  增加流动性确认    pop_deal_add_liudong_sure
 *  等待中      pop_deal_pool_wait
 *  失败       pop_deal_pool_wait_fail
 *  成功       pop_deal_pool_wait_success
 */
public class DealAddFlowActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.jine)
    TextView jine;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.choose_bi)
    TextView chooseBi;
    @BindView(R.id.gaoji_set)
    TextView gaojiSet;
    @BindView(R.id.daibi_jine)
    TextView daibiJine;
    @BindView(R.id.edit_num2)
    ClearEditText editNum2;
    @BindView(R.id.choose_daibi)
    TextView chooseDaibi;
    @BindView(R.id.show_daibi)
    TextView showDaibi;
    @BindView(R.id.img_zhe)
    ImageView imgZhe;
    @BindView(R.id.exchange_trx)
    TextView exchangeTrx;
    @BindView(R.id.exchange_usdt)
    TextView exchangeUsdt;
    @BindView(R.id.deal_fene)
    TextView dealFene;
    @BindView(R.id.trx_num)
    TextView trxNum;
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.deal_daibi)
    TextView dealDaibi;
    @BindView(R.id.btn_shuru_num)
    Button btnShuruNum;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_deal_add_flow;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //string_add_liudong添加流动性
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_add_liudong);
        toolbarImageLeft.setVisibility(View.VISIBLE);

    }


    @OnClick({R.id.choose_bi, R.id.gaoji_set, R.id.choose_daibi, R.id.show_daibi, R.id.btn_shuru_num, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_bi:
                break;
            case R.id.gaoji_set:
                break;
            case R.id.choose_daibi:
                break;
            case R.id.show_daibi:
                break;
            case R.id.btn_shuru_num:
                break;
            case R.id.btn_ture:
                break;
            default:
                break;
        }
    }
}
