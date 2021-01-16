package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.FinancesListBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 理财usdt
 * 没有买入的理财产品
 */
public class ManageFinancesUSDTActivity extends BaseActivity {

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
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.bi_name)
    TextView biName;
    @BindView(R.id.yeild_type)
    TextView yeildType;
    @BindView(R.id.qitou)
    TextView qitou;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.scrollerLayout)
    NestedScrollView scrollerLayout;
    @BindView(R.id.edit_password)
    ClearEditText editPassword;
    private AssetsBean select;
    private FinancesListBean financesListBean;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_finances_usdt;
    }

    @Autowired
    Dao dao;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_home_tab7));

        Intent intent = getIntent();
        financesListBean = (FinancesListBean) intent.getSerializableExtra("financesListBean");
        toolbarTitle.setText(financesListBean.getName());
        biName.setText(financesListBean.getGet_currency_name());
        qitou.setText(financesListBean.getMin()+ financesListBean.getPayment_currency_name());

        name.setText(financesListBean.getName());
        titleName.setText(financesListBean.getName());

        select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(financesListBean.getPayment_currency_name()));

        BigDecimal num = select.getNum();
        usdtNum.setText(getString(R.string.string_can_user)+ financesListBean.getPayment_currency_name()+"："+num.toPlainString());
        String annualized_rate = financesListBean.getAnnualized_rate();
        String s = new BigDecimal(annualized_rate).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        yelid.setText(getString(R.string.string_expected_ysild)+s+"%");
        tv_content.setText(financesListBean.getProject_description());


    }

    @Autowired
    Block block;
    @OnClick(R.id.btn_ture)
    public void onViewClicked() {
        showLoading();
        if (StrUtil.isBlank(editNum.getText().toString())) {
            showMessage(getString(R.string.string_edit_money));
            hideLoading();
            return;
        }

        BigDecimal bigDecimal = new BigDecimal(editNum.getText().toString());
        BigDecimal bigDecimal1 = new BigDecimal(financesListBean.getMin());

        if (bigDecimal.compareTo(bigDecimal1)<0) {
            showMessage(getString(R.string.string_less_than_smoney));
            hideLoading();
            return;
        }
        if (StrUtil.isBlank(editPassword.getText().toString())) {
            showMessage(getString(R.string.string_input_pwd));
            hideLoading();
            return;
        }
        CoinBean select = dao.select(new CoinBean().setSymbol(financesListBean.getPayment_currency_name()));
        String financial_address = financesListBean.getFinancial_address();
        String contract_address = select.getContract_address();

        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {
                Message tron = block.tron(financial_address, contract_address, new BigDecimal(editNum.getText().toString()), SpUtil.getInstance(ManageFinancesUSDTActivity.this).getString("this_wallet_name",""), editPassword.getText().toString());
                if (tron.getType()== Message.SUCCESS) {
                    hideLoading();
                    showMessage(getString(R.string.string_buy_success));
                    finish();
                }else{
                    hideLoading();
                    showMessage(tron.getMsg());
                }
            }
        });

    }

}
