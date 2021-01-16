package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.ExchangeConfBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.util.Other;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;


/**
 * Description:
 * Created by ccw on 09/11/2020 11:36
 * Email:chencw0715@163.com
 * 兑换
 */
public class MineExchangeActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cny)
    TextView cny;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.tx_excharge)
    TextView txExcharge;
    @BindView(R.id.tx_excharge_fee)
    TextView txExchargeFee;
    @BindView(R.id.tx_excharge_num)
    TextView txExchargeNum;
    @BindView(R.id.btn_excharge)
    Button btnExcharge;
    ExchangeConfBean mConfBean;

    @BindView(R.id.usdt_dc)
    TextView usdtDc;

    @BindView(R.id.edit_psd)
    ClearEditText edit_psd;
    @BindView(R.id.ycdy_ex)
    TextView ycdyEx;
    @BindView(R.id.timer_ex)
    TextView timerEx;
    @BindView(R.id.progressbar_exchange)
    ProgressBar progressbarExchange;
    @BindView(R.id.baifen_ex)
    TextView baifenEx;
    @BindView(R.id.duihuan_ex)
    TextView duihuanEx;
    @BindView(R.id.img_excharge)
    ImageView imgExcharge;
    @BindView(R.id.text_num)
    TextView textNum;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.yuji_dc)
    TextView yujiDc;
    @BindView(R.id.title_dcy)
    TextView title_dcy;

    private CountDownTimer timer;

    @Autowired
    Block block;

    @Autowired
    Dao dao;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_exchange;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_exchange));
        toolbarImageRight.setVisibility(View.VISIBLE);
        toolbarImageRight.setImageResource(R.mipmap.icon_why);
        toolbarImageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineExchangeActivity.this, MineExchangeWhyActivity.class));
            }
        });
        request();
        AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("USDT"));
        usdtNum.setText(select.getNum().stripTrailingZeros().toPlainString());
//        requestWallet("USDT");
        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mConfBean != null) {
                    if (Other.isDouble(s.toString())) {
                        BigDecimal bigDecimal = new BigDecimal(s.toString());
                        BigDecimal exchange_conf_scale = mConfBean.getExchange_conf_scale();

                        String s1 = bigDecimal.multiply(exchange_conf_scale).stripTrailingZeros().toPlainString();

                        textNum.setText(s1);
                    } else {
                        textNum.setText("0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick(R.id.btn_excharge)
    public void onViewClicked() {
        //点击兑换
        if (mConfBean != null) {
//            requestExchange();
            CoinBean usdt = dao.select(new CoinBean().setSymbol("USDT"));

            if (!Other.isDouble(editNum.getText().toString())) {
                //提示金额输入错误
                showMessage(getString(R.string.string_edit_money_fail));
                return;
            }
            BigDecimal bigDecimal = new BigDecimal(editNum.getText().toString());

            AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("USDT"));
            if (bigDecimal.compareTo(select.getNum()) > 0) {
                //提示钱包余额不足
                showMessage(getString(R.string.string_money_inadequate));
                return;
            }
            if (bigDecimal.compareTo(mConfBean.getExchange_conf_min()) < 0) {
                showMessage(getString(R.string.string_lowest_exchange) + mConfBean.getExchange_conf_min() + getString(R.string.string_ge) + mConfBean.getExchange_conf_spend());
                //提示最低兑换
                return;
            }
            if (bigDecimal.compareTo(mConfBean.getExchange_conf_max()) > 0) {
                showMessage(getString(R.string.string_highest_exchange) + mConfBean.getExchange_conf_max() + getString(R.string.string_ge) + mConfBean.getExchange_conf_spend());
                //提示最多兑换
                return;
            }
            if (StrUtil.isBlank(edit_psd.getText().toString())) {
                showMessage(getString(R.string.string_input_pwd));
                return;
            }
            //弹出钱包密码输入框
            showLoading();
            ThreadUtil.execAsync(new Runnable() {
                @Override
                public void run() {
                    Message tron = block.tron(mConfBean.getExchange_address(), usdt.getContract_address(), bigDecimal, SpUtil.getInstance(DataSave.app).getString("this_wallet_name",""),edit_psd.getText().toString());
                    hideLoading();
                    if (tron.getType() == Message.SUCCESS) {
                        //成功
                        showMessage(getString(R.string.string_exchange_success));
                        finish();
                    } else {
                        //失败
                        showMessage(tron.getMsg());
                    }

                }
            });

        }


    }

    /**
     * 得到兑换配置信息
     */
    private void request() {
        showLoading();
        MyApp.requestSend.sendData("exchange.method.detail").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data = message.getDate().toString();
                JSONArray objects = JSONArray.parseArray(data);
                Iterator<Object> iterator = objects.iterator();
                while (iterator.hasNext()) {
                    ExchangeConfBean confBean = ExchangeConfBean.newBean(ExchangeConfBean.class, iterator.next());
                    if (confBean.getExchange_conf_spend().equals("USDT")) {
                        mConfBean = confBean;

                        String s1 = confBean.getExchange_conf_scale().stripTrailingZeros().toPlainString();
                        cny.setText("1" + confBean.getExchange_conf_spend() + "=" + s1 + confBean.getExchange_conf_get());

                        String s = confBean.getExchange_conf_fee().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                        txExchargeFee.setText(s + "%");
                        String s2 = confBean.getExchange_conf_min().stripTrailingZeros().toPlainString();
                        txExchargeNum.setText(s2);
                        String s3 = confBean.getExchange_conf_max().stripTrailingZeros().toPlainString();
                        txExcharge.setText(s3);
                        usdtDc.setText(confBean.getExchange_conf_spend());
                        yujiDc.setText(confBean.getExchange_conf_get());
                        ycdyEx.setText(confBean.getName());
                        BigDecimal total_amount = confBean.getTotal_amount();//总数量
                        BigDecimal the_remaining_amount = confBean.getThe_remaining_amount();//剩余数量
                        BigDecimal s4 = total_amount.subtract(the_remaining_amount).stripTrailingZeros();
                        title_dcy.setText(confBean.getExchange_conf_get());

                        progressbarExchange.setMax(confBean.getTotal_amount().intValue());
                        progressbarExchange.setProgress(s4.intValue());

                        BigDecimal divide = s4.divide(total_amount,2,BigDecimal.ROUND_UP);
                        BigDecimal multiply = divide.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
                        baifenEx.setText(multiply.stripTrailingZeros().toPlainString() + "%");

                        duihuanEx.setText(getString(R.string.string_exchange_yi) + s4.toPlainString() + "/" + total_amount.stripTrailingZeros().toPlainString());
                        Date date = new Date();
                        long time = date.getTime();
                        Date end_time = confBean.getEnd_time();
                        long time1 = end_time.getTime();
                        long remainingTime = time1 - time;


                        if (remainingTime <= 0) {
                            timerEx.setText(getString(R.string.string_end));
                            btnExcharge.setEnabled(false);
                            btnExcharge.setBackgroundResource(R.drawable.block_shape);
                        } else {
                            countDown(remainingTime);
                            timerEx.setText(" "+DateFormatUtils.format(new Date(remainingTime), "dd天HH时mm分"));
                        }


                        if (the_remaining_amount.compareTo(new BigDecimal("0")) <= 0) {
                            timerEx.setText(getString(R.string.string_end));
                            btnExcharge.setEnabled(false);
                            btnExcharge.setBackgroundResource(R.drawable.block_shape);
                        }
                    }
                }


                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    /**
     * 倒计时显示
     */
    private void countDown(long remainingTime) {

        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String date = DateFormatUtils.format(new Date(millisUntilFinished), "dd日 HH时mm分ss秒");
                timerEx.setText(date);
            }

            @Override
            public void onFinish() {


            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

}
