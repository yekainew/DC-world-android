package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.PoolListBean;

import java.io.Serializable;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 矿机详情
 *
 * @author admin6
 */
public class PoolDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pool_details_hashrate)
    TextView poolDetailsHashrate;
    @BindView(R.id.pool_details_yeild)
    TextView poolDetailsYeild;
    @BindView(R.id.pool_details_algorithm)
    TextView poolDetailsAlgorithm;
    @BindView(R.id.pool_details_type)
    TextView poolDetailsType;
    @BindView(R.id.pool_details_address)
    TextView poolDetailsAddress;
    @BindView(R.id.pool_details_all_hashrate)
    TextView poolDetailsAllHashrate;
    @BindView(R.id.pool_details_pool_hashrate)
    TextView poolDetailsPoolHashrate;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pool_details;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_pool_details);
        Intent intent = getIntent();
        PoolListBean poolListBean = (PoolListBean) intent.getSerializableExtra("poolListBean");
        String hold_num = poolListBean.getHold_num();

        BigDecimal bigDecimal = new BigDecimal(hold_num);
        if (bigDecimal.compareTo(new BigDecimal("10000")) > 0) {
            String s = bigDecimal.subtract(new BigDecimal("10000")).multiply(new BigDecimal("10")).add(new BigDecimal("10000")).stripTrailingZeros().toPlainString();
            poolDetailsHashrate.setText(s + " GH/S");
        } else {
            String s = bigDecimal.multiply(new BigDecimal("10")).stripTrailingZeros().toPlainString();
            poolDetailsHashrate.setText(s + " GH/S");
        }

        poolDetailsYeild.setText(poolListBean.getIncome() + " " + poolListBean.getFreed_type_name());
        poolDetailsType.setText(poolListBean.getHold_area_name());
        poolDetailsAddress.setText(poolListBean.getInvest_address());

    }

}