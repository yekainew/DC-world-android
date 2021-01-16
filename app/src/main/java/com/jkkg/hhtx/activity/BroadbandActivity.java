package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.AccountResourecBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.liys.view.LineProView;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 宽带/能量
 */
public class BroadbandActivity extends BaseActivity {

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
    @BindView(R.id.broadband_num)
    TextView broadbandNum;
    @BindView(R.id.broadband_linpro)
    LineProView broadbandLinpro;
    @BindView(R.id.broadband_available)
    TextView broadbandAvailable;
    @BindView(R.id.broadband_oneself)
    TextView broadbandOneself;
    @BindView(R.id.broadband_trx)
    TextView broadbandTrx;
    @BindView(R.id.broadband_herself)
    TextView broadbandHerself;
    @BindView(R.id.energy_num)
    TextView energyNum;
    @BindView(R.id.energy_linpro)
    LineProView energyLinpro;
    @BindView(R.id.energy_available)
    TextView energyAvailable;
    @BindView(R.id.energy_oneself)
    TextView energyOneself;
    @BindView(R.id.energy_trx)
    TextView energyTrx;
    @BindView(R.id.energy_herself)
    TextView energyHerself;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.femle)
    RadioButton femle;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.all_trx)
    TextView allTrx;
    @BindView(R.id.oneself)
    RadioButton oneself;
    @BindView(R.id.herself)
    RadioButton herself;
    @BindView(R.id.rg2)
    RadioGroup rg2;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Autowired
    Block block;

    @Autowired
    Dao dao;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_broadband;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
        String address = select.getAddress();
        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {


                Message accountresource = block.getAccountresource(address);
                AccountResourecBean accountResourecBean = AccountResourecBean.newBean(AccountResourecBean.class, accountresource.getDate());

                BigDecimal freeNetLimit = accountResourecBean.getFreeNetLimit();//带宽总数
                BigDecimal freeNetUsed = accountResourecBean.getFreeNetUsed();//已用带宽

                BigDecimal subtract = freeNetLimit.subtract(freeNetUsed);



                BigDecimal divide = subtract.divide(new BigDecimal("1024"),4,BigDecimal.ROUND_DOWN);

                BigDecimal multiply = divide.multiply(new BigDecimal("1000"));

                Log.d("BroadbandActivitynum", multiply.stripTrailingZeros().toPlainString());

                BigDecimal divide1 = freeNetLimit.divide(new BigDecimal("1024"), 4, BigDecimal.ROUND_DOWN);
                BigDecimal multiply1 = divide1.multiply(new BigDecimal("1000"));

                broadbandLinpro.init();
                broadbandLinpro.setProgress(multiply.stripTrailingZeros().doubleValue());
                broadbandLinpro.setMaxProgress(multiply1.stripTrailingZeros().doubleValue());


                broadbandAvailable.setText(multiply.divide(new BigDecimal("1000")).setScale(2,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()
                        +"/"+multiply1.divide(new BigDecimal("1000")).setScale(2,BigDecimal.ROUND_DOWN)+"KB");







            }
        });
    }

    @OnClick(R.id.btn_ture)
    public void onViewClicked() {
    }
}
