package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.MyFinancesListBean;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 买入的理财产品
 */
public class ManageFinancesBuyActivity extends BaseActivity {

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
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.yelid)
    TextView yelid;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.bi_name)
    TextView biName;
    @BindView(R.id.yeild_type)
    TextView yeildType;
    @BindView(R.id.qitou)
    TextView qitou;
    @BindView(R.id.qixian_time)
    TextView qixianTime;
    @BindView(R.id.scrollerLayout)
    NestedScrollView scrollerLayout;
    @BindView(R.id.cunru_num)
    TextView cunruNum;
    @BindView(R.id.status)
    TextView status;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_finances_buy;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        MyFinancesListBean myFinancesListBean = (MyFinancesListBean) intent.getSerializableExtra("myFinancesListBean");
        toolbarTitle.setText(myFinancesListBean.getName());
        titleName.setText(myFinancesListBean.getName());
        String annualized_rate = myFinancesListBean.getAnnualized_rate();
        String s = new BigDecimal(annualized_rate).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        yelid.setText(getString(R.string.string_expected_ysild) + s + "%");
        String amount = myFinancesListBean.getAmount();
        String s1 = new BigDecimal(amount).stripTrailingZeros().toPlainString();
        cunruNum.setText(s1);
        name.setText(myFinancesListBean.getName());
        biName.setText(myFinancesListBean.getPayment_currency_name());
        String max = myFinancesListBean.getMin();
        String s2 = new BigDecimal(max).stripTrailingZeros().toPlainString();
        qitou.setText(s2 + myFinancesListBean.getPayment_currency_name());


        if (myFinancesListBean.getStatus().equals("1")) {
            status.setText(getString(R.string.string_joinin));
        } else {
            status.setText(getString(R.string.string_end));
        }
    }

}
