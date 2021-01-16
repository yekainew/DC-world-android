package com.jkkg.hhtx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ExchangeListAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeConfBeans;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.utils.OverFlyingLayoutManager;
import com.liys.view.LineBottomProView;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 兑换展示页
 */
public class ExchangeFiashActivity extends BaseActivity {

    @BindView(R.id.exchange_allnum)
    TextView exchangeAllnum;
    @BindView(R.id.exchange_leijinum)
    TextView exchangeLeijinum;
    @BindView(R.id.exchange_mynum)
    TextView exchangeMynum;
    @BindView(R.id.exchange_duihuannum)
    TextView exchangeDuihuannum;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.bottom_pro_view)
    LineBottomProView bottom_pro_view;
    @BindView(R.id.one_bili)
    TextView one_bili;
    @BindView(R.id.one_dc)
    TextView one_dc;


    private ExchangeListAdapter exchangeListAdapter;
    private List<ExchangeConfBeans> exchangeConfBeans1;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange_fiash;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        /*exchangeListAdapter = new ExchangeListAdapter();
        recyclerView.setLayoutManager(new OverFlyingLayoutManager(OrientationHelper.VERTICAL,false));
        recyclerView.setAdapter(exchangeListAdapter);*/
        exchangedetail();
        changeListHierarchy();
        ImageView exchange_record = findViewById(R.id.exchange_record);
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exchange_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ExchangeFiashActivity.this, ExchangeRecordActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void changeListHierarchy() {
        MyApp.requestSend.sendData("exchange.method.changeListHierarchy").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                List<ExchangeConfBeans> exchangeConfBeans = JSONArray.parseArray(message.getDate().toString(), ExchangeConfBeans.class);
                if (exchangeConfBeans.size()>0) {
                    ExchangeConfBeans exchangeConfBeans1 = exchangeConfBeans.get(0);
                    one_bili.setText("1:"+exchangeConfBeans1.getExchange_conf_scale().stripTrailingZeros().toPlainString());
                    one_dc.setText(exchangeConfBeans1.getThe_remaining_amount().stripTrailingZeros().toPlainString());
                    BigDecimal deposit_amount_per_layer = exchangeConfBeans1.getDeposit_amount_per_layer();//本层总如今
                    BigDecimal starting_price = exchangeConfBeans1.getStarting_price();//比例
                    BigDecimal multiply = deposit_amount_per_layer.multiply(starting_price);//总数量
                    BigDecimal the_remaining_amount = exchangeConfBeans1.getThe_remaining_amount();//剩余数量
                    BigDecimal divide = the_remaining_amount.divide(multiply, 4, BigDecimal.ROUND_DOWN);
                    BigDecimal subtract = BigDecimal.ONE.subtract(divide).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN);
                    bottom_pro_view.init();
                    bottom_pro_view.setProgress(subtract.stripTrailingZeros().doubleValue());

                }

                exchangeConfBeans.remove(0);
                exchangeListAdapter = new ExchangeListAdapter(exchangeConfBeans,ExchangeFiashActivity.this);
                recyclerView.setLayoutManager(new OverFlyingLayoutManager(OrientationHelper.VERTICAL, false));
                recyclerView.setAdapter(exchangeListAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @Autowired
    Dao dao;

    private ExchangeConfBeans exchangeConfBeans;

    private void exchangedetail() {
        MyApp.requestSend.sendData("exchange.method.detail").main(new NetCall.Call() {


            @SuppressLint("SetTextI18n")
            @Override
            public Message ok(Message message) {
                exchangeConfBeans1 = JSONArray.parseArray(message.getDate().toString(), ExchangeConfBeans.class);
                exchangeConfBeans = exchangeConfBeans1.get(0);

                BigDecimal total_amount = exchangeConfBeans.getTotal_amount();//总数量
                BigDecimal the_remaining_amount = exchangeConfBeans.getThe_remaining_amount();//剩余兑换
                String exchange_conf_get = exchangeConfBeans.getExchange_conf_get();//兑换所得币种


                exchangeAllnum.setText(total_amount.setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + exchange_conf_get);
                BigDecimal subtract = total_amount.subtract(the_remaining_amount);

                exchangeLeijinum.setText(subtract.setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + exchange_conf_get);

                AssetsBean select = dao.select(new AssetsBean().setSymbol(exchangeConfBeans.getExchange_conf_spend()).setWallet_name(Constants.getWalletName()));

                if (select != null) {
                    exchangeMynum.setText(select.getNum().setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + exchangeConfBeans.getExchange_conf_spend());
                } else {
                    exchangeMynum.setText("0.00" + exchangeConfBeans.getExchange_conf_spend());
                }

                BigDecimal num = select.getNum();
                BigDecimal current_price = exchangeConfBeans.getCurrent_price();

                BigDecimal multiply = num.multiply(current_price);
                exchangeDuihuannum.setText(multiply.setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + exchange_conf_get);

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    @OnClick(R.id.btn_ture)
    public void onViewClicked() {
        if (exchangeConfBeans1.size()==0) {
            showMessage("暂未开放");
        }else{
            Intent intent = new Intent(this, ExchangeActivity.class);
            intent.putExtra("exchangeConfBeans", exchangeConfBeans);
            startActivity(intent);
            finish();
        }

    }
}
