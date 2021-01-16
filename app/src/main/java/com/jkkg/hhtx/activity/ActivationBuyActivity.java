package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ActivationNumAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.ActivationNumConfBean;
import com.jkkg.hhtx.net.bean.ExchangeConfBeanss;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 购买激活次数
 * 购买记录 ActivationBuyRecordActivity
 * item_jihuonum_buy
 */
public class ActivationBuyActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.buy_jilu)
    TextView buyJilu;
    @BindView(R.id.assets_rec)
    RecyclerView assetsRec;
    @BindView(R.id.text_price)
    TextView textPrice;
    @BindView(R.id.xiaohao_price)
    TextView xiaohaoPrice;
    @BindView(R.id.invite_psw)
    ClearEditText invitePsw;
    @BindView(R.id.send_code1)
    TextView sendCode1;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.num_dc)
    TextView numDc;
    @BindView(R.id.tip_text)
    TextView tipText;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_activation_buy;
    }
    ActivationNumConfBean bean;
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        buyJilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ActivationBuyRecordActivity.class));
            }
        });
        exchangeConf();
        List<ActivationNumConfBean> list = new ArrayList<>();
        list.add(new ActivationNumConfBean().setCheckd(true).setNum("10"));
        list.add(new ActivationNumConfBean().setCheckd(false).setNum("20"));
        list.add(new ActivationNumConfBean().setCheckd(false).setNum("30"));

        ActivationNumAdapter activationNumAdapter = new ActivationNumAdapter(this, list);

        assetsRec.setLayoutManager(new GridLayoutManager(this, 3));
        assetsRec.setAdapter(activationNumAdapter);


        activationNumAdapter.setOnClickListener(new ActivationNumAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int p, ActivationNumConfBean bean) {
                ActivationBuyActivity.this.bean=bean;
                BigDecimal invite_exchange_scale = exchangeConfBeanss.getInvite_exchange_scale();

                BigDecimal divide = invite_exchange_scale.divide(new BigDecimal("100"));

                String num = bean.getNum();

                BigDecimal multiply = divide.multiply(new BigDecimal(num));

                xiaohaoPrice.setText("预计消耗:" + multiply.stripTrailingZeros().toPlainString() + " " + exchangeConfBeanss.getInvite_exchange_curreny());
            }
        });


    }

    @Autowired
    Block block;

    @Autowired
    Dao dao;
    private ExchangeConfBeanss exchangeConfBeanss;

    private void exchangeConf() {
        MyApp.requestSend.sendData("invite.method.exchangeConf").main(new NetCall.Call() {


            @Override
            public Message ok(Message message) {
                exchangeConfBeanss = ExchangeConfBeanss.newBean(ExchangeConfBeanss.class, message.getDate());
                BigDecimal invite_exchange_scale = exchangeConfBeanss.getInvite_exchange_scale();

                BigDecimal divide = invite_exchange_scale.divide(new BigDecimal("100"));


                textPrice.setText("价格：1个激活次数=" + divide.stripTrailingZeros().toPlainString() + exchangeConfBeanss.getInvite_exchange_curreny());
                String num = "10";

                BigDecimal multiply = divide.multiply(new BigDecimal(num));
                xiaohaoPrice.setText("预计消耗:" + multiply.stripTrailingZeros().toPlainString() + " " + exchangeConfBeanss.getInvite_exchange_curreny());

                AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(exchangeConfBeanss.getInvite_exchange_curreny()));

                if (select != null) {
                    numDc.setText(select.getNum().stripTrailingZeros().toPlainString() + exchangeConfBeanss.getInvite_exchange_curreny());
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    public void tron(String psd) {

        BigDecimal invite_exchange_scale = exchangeConfBeanss.getInvite_exchange_scale();

        BigDecimal divide = invite_exchange_scale.divide(new BigDecimal("100"));
        String num;
        if (bean==null) {
            num="10";
        }else{
            num = bean.getNum();
        }

        BigDecimal multiply = divide.multiply(new BigDecimal(num));
        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {
                Message tron = block.tron(exchangeConfBeanss.getInvite_exchange_address(),exchangeConfBeanss.getContract_address(),multiply,Constants.getWalletName(),psd);
                if (tron.getType() == Message.SUCCESS) {
                    String s4 = tron.getDate().toString();
                    showMessage("成功");
                    finish();
                } else {

                    showMessage(tron.getMsg());
                }
                hideLoading();
            }
        });

    }


    @OnClick(R.id.btn_join)
    public void onViewClicked() {
        String s = invitePsw.getText().toString();
        showLoading();
        if (StrUtil.isBlank(s)) {
            hideLoading();
            showMessage("请输入钱包密码");
            return;
        }
        tron(s);

    }
}