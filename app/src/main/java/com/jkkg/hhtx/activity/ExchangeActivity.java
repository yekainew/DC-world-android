package com.jkkg.hhtx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ExchangRecTimeAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.ExchangeConfBeans;
import com.jkkg.hhtx.net.bean.ExchangeGroupBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.CircularProgressView;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.PsdDialog;
import com.jkkg.hhtx.widget.UsdtDialog;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

/**
 * new 兑换
 * 确认弹窗 pop_exchange1
 * 安全认证 pop_exchange2
 */
public class ExchangeActivity extends BaseActivity {
    @BindView(R.id.exchange_record)
    ImageView exchangeRecord;
    @BindView(R.id.xuanzhuan_img)
    ImageView xuanzhuan_img;
    @BindView(R.id.zong_dc)
    TextView zongDc;
    @BindView(R.id.shengyu_dc)
    TextView shengyuDc;
    @BindView(R.id.huilv)
    TextView huilv;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.yue)
    TextView yue;
    @BindView(R.id.get_dc_num)
    TextView getDcNum;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.exchange_min)
    TextView exchangeMin;
    @BindView(R.id.exchange_max)
    TextView exchangeMex;
    @BindView(R.id.lun_num)
    TextView lunNum;
    @BindView(R.id.leiji_dc)
    TextView leijiDc;
    @BindView(R.id.usdt_price)
    TextView usdtPrice;
    @BindView(R.id.dc_price)
    TextView dcPrice;
    @BindView(R.id.usdt_price_xia)
    TextView usdtPriceXia;
    @BindView(R.id.dc_price_xia)
    TextView dcPriceXia;
    @BindView(R.id.progress_bar)
    CircularProgressView progressBar;
    @BindView(R.id.shengyu_dc1)
    TextView shengyuDc1;
    @BindView(R.id.all_dc)
    TextView allDc;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.exchange_main)
    LinearLayout exchangeMain;
    @BindView(R.id.bc_name)
    TextView bcName;
    @BindView(R.id.exchange_rec)
    RecyclerView exchangeRec;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.choose_biz)
    TextView chooseBiz;
    @BindView(R.id.asset)
    TextView asset;
    @BindView(R.id.jiangjin)
    TextView jiangjin;

    private CircularProgressView progress_bar;
    private ExchangeConfBeans exchangeConfBeans;
    private AssetsBean select;
    private BigDecimal multiply3;
    private TextView usdt_name;
    private ExchangRecTimeAdapter exchangRecTimeAdapter;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange;
    }

    int position = 0;
    @Autowired
    Dao dao;
    private List<ExchangeConfBeans> exchangeConfBeans1;

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

                select = dao.select(new AssetsBean().setSymbol(exchangeConfBeans.getExchange_conf_spend()).setWallet_name(Constants.getWalletName()));

                BigDecimal deposit_amount_per_layer = exchangeConfBeans.getDeposit_amount_per_layer();//每层需要入多少金
                BigDecimal current_price = exchangeConfBeans.getCurrent_price();//当前价格
                BigDecimal current_deposit = exchangeConfBeans.getCurrent_deposit();//当前入金
                BigDecimal exchange_conf_fee = exchangeConfBeans.getExchange_conf_fee();//手续费
                BigDecimal multiply = current_price.multiply(deposit_amount_per_layer);//本层可兑换数量
                BigDecimal multiply1 = current_deposit.multiply(current_price);//本层已兑换DC
                BigDecimal subtract = multiply.subtract(multiply1);//本层兑换剩余DC
                exchangeMex.setText("最高兑换" + exchangeConfBeans.getExchange_conf_max().stripTrailingZeros().toPlainString());
                exchangeMin.setText("最低兑换" + exchangeConfBeans.getExchange_conf_min().stripTrailingZeros().toPlainString());

                huilv.setText(1 + exchangeConfBeans.getExchange_conf_spend() + "≈" + current_price.stripTrailingZeros().toPlainString() + exchange_conf_get);
                fee.setText(exchange_conf_fee.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString() + "%");

                if (select != null) {
                    yue.setText(select.getNum().stripTrailingZeros().toPlainString());
                } else {
                    yue.setText("余额" + "0.00" + exchangeConfBeans.getExchange_conf_spend());
                }

                BigDecimal subtract1 = total_amount.subtract(the_remaining_amount);
                leijiDc.setText(subtract1.setScale(4, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                bcName.setText(exchange_conf_get);

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        exchangedetail();
        changeListHierarchy();
        openTime();

        startRec();
   /*     progress_bar = (CircularProgressView) findViewById(R.id.progress_bar);
        progress_bar.setProgress(0);
        progress_bar.setText(progress_bar.getProgress() + "%");*/
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Other.isDouble(s.toString())) {
                    multiply3 = new BigDecimal(s.toString()).multiply(exchangeConfBeans.getCurrent_price());
                    getDcNum.setText("可获得" + multiply3.stripTrailingZeros().toPlainString() + exchangeConfBeans.getExchange_conf_get());
                } else {
                    getDcNum.setText("可获得" + "0.00" + exchangeConfBeans.getExchange_conf_get());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void startRec() {
        exchangeRec.setLayoutManager(new GridLayoutManager(getContext(), 3));
        exchangRecTimeAdapter = new ExchangRecTimeAdapter(getContext(), new ArrayList<>(), position);
        exchangeRec.setAdapter(exchangRecTimeAdapter);
        exchangRecTimeAdapter.setOnClickListener(new ExchangRecTimeAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, ExchangeGroupBean bean) {
                ExchangeActivity.this.position = position;
                Integer group_status = bean.getGroup_status();
                if (timer != null) {
                    timer.cancel();
                }
                if (group_status == ExchangeGroupBean.group_status_0) {
                    // TODO: 2020/12/26 未开始
                    if (bean.getStart_time() != null) {
                        longstartData(bean.getStart_time());
                        type = 1;
                    } else {
                        time.setText("未开始");
                        showMessage("未开始");
                        type = 1;
                    }
                } else if (group_status == ExchangeGroupBean.group_status_1) {
                    // TODO: 2020/12/26 进行中
                    if (bean.getEnd_time() != null) {
                        longstartData(bean.getEnd_time());
                        type = 0;
                    } else {
                        time.setText("进行中");
                        type = 0;
                    }
                } else {
                    // TODO: 2020/12/26 已结束
                    time.setText("已结束");
                    type = 2;
                }

                exchangedetail();
                changeListHierarchy();
                if (timer != null) {
                    timer.cancel();
                }
                openTime();

            }
        });
    }

    int type = 0;
    int p = 1;

    private void openTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        MyApp.requestSend.sendData("exchange.method.openTime").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                List<ExchangeGroupBean> exchangeGroupBeans = JSONArray.parseArray(message.getDate().toString(), ExchangeGroupBean.class);

                for (int i = 0; i < exchangeGroupBeans.size(); i++) {
                    if (i == 0) {
                        exchangeGroupBeans.get(i).setCheck(true);
                    } else {
                        exchangeGroupBeans.get(i).setCheck(false);
                    }

                    if (p <= 2) {
                        /*if (exchangeGroupBeans.get(i).getGroup_status()== ExchangeGroupBean.group_status_1) {
                            position=i;
                        }*/
                        if (exchangeGroupBeans.get(i).getGroup_status() == ExchangeGroupBean.group_status_2) {
                            position = i + 1;
                        }
                        p++;
                    }

                }
                ExchangeGroupBean exchangeGroupBean = exchangeGroupBeans.get(position);
                Integer group_status = exchangeGroupBean.getGroup_status();
                if (group_status == ExchangeGroupBean.group_status_0) {
                    // TODO: 2020/12/26 未开始
                    if (exchangeGroupBean.getStart_time() != null) {
                        longendData(exchangeGroupBean.getStart_time());
                        type = 1;
                    } else {
                        time.setText("未开始");
                        type = 1;
                    }
                } else if (group_status == ExchangeGroupBean.group_status_1) {
                    // TODO: 2020/12/26 进行中
                    if (exchangeGroupBean.getEnd_time() != null) {
                        longstartData(exchangeGroupBean.getEnd_time());
                        type = 0;
                    } else {
                        time.setText("进行中");
                        type = 0;
                    }
                } else {
                    // TODO: 2020/12/26 已结束
                    time.setText("已结束");
                    type = 2;
                }
                exchangRecTimeAdapter.setData(exchangeGroupBeans, position);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    public void longstartData(Date startTime) {
        Date date = new Date();
        long time = date.getTime();

        long time1 = startTime.getTime();

        long remaining = time1 - time;

        countDown(remaining);
    }

    public void longendData(Date shouTime) {
        Date date = new Date();
        long time = date.getTime();
        long time1 = shouTime.getTime();


        long remainingTime = time1 - time;

        countDown(remainingTime);
    }

    private CountDownTimer timer;

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    boolean isInit = true;
    /**
     * 倒计时显示
     */
    private void countDown(long remainingTime) {

        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long day = millisUntilFinished / (24 * 60 * 60 * 1000);

                long hour = (millisUntilFinished / (60 * 60 * 1000) - day * 24);

                long min = ((millisUntilFinished / (60 * 1000)) - day * 24 * 60 - hour * 60);

                long s = (millisUntilFinished / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (type == 0) {
                    time.setText(hour + "时" + min + "分" + s + "秒" + "后结束");
                } else if (type == 1) {
                    time.setText(hour + "时" + min + "分" + s + "秒" + "后开放");
                }

                /*if (millisUntilFinished == 0) {
                    if (type==0) {
                        time.setText("已结束");
                        type=1;
                    }else{
                        time.setText("已开放");
                        type=0;
                    }
                }*/
            }

            @Override
            public void onFinish() {
                if (isInit) {
                    openTime();
                    isInit=false;
                }
            }
        }.start();
    }

    @Autowired
    Block block;

    @OnClick({R.id.btn_ture, R.id.choose_biz, R.id.asset, R.id.jiangjin, R.id.all_dc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ture:
//                startarea1Pop("0","0");
                check();
                break;
            case R.id.choose_biz:
                UsdtDialog usdtDialog = new UsdtDialog(getContext());
                usdtDialog.show();
                break;
            case R.id.asset:
                //资产
                startActivity(new Intent(this, ExchangeAssetActivity.class));
                break;
            case R.id.jiangjin:
                //奖金
                startActivity(new Intent(this, ExchangeRewardActivity.class));
                break;
            case R.id.all_dc:
                //奖金
                String s = yue.getText().toString();
                if (StrUtil.isNotBlank(s)) {
                    editNum.setText(s);
                }
                break;
            default:
                break;
        }
    }

    private void check() {
        Map<String, String> map = new ArrayMap<>();
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
        String address = select.getAddress();

        map.put("address", address);

        MyApp.requestSend.sendData("exchange.method.check", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                if (multiply3 != null) {
                    startarea1Pop(editNum.getText().toString(), multiply3.stripTrailingZeros().toPlainString());
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void changeListHierarchy() {
        MyApp.requestSend.sendData("exchange.method.changeListHierarchy").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                List<ExchangeConfBeans> exchangeConfBeans = JSONArray.parseArray(message.getDate().toString(), ExchangeConfBeans.class);
                if (exchangeConfBeans.size() > 0) {
                    ExchangeConfBeans exchangeConfBeans1 = exchangeConfBeans.get(0);
                    usdtPrice.setText("1");
                    dcPrice.setText(exchangeConfBeans1.getExchange_conf_scale().stripTrailingZeros().toPlainString());

                    lunNum.setText("第" + exchangeConfBeans1.getCurrent_level() + "轮");

                    BigDecimal current_deposit = exchangeConfBeans1.getCurrent_deposit();
                    BigDecimal current_price = exchangeConfBeans1.getCurrent_price();
                    BigDecimal output_curreny = exchangeConfBeans1.getOutput_curreny();


                    BigDecimal multiply = current_deposit.multiply(current_price);


                    BigDecimal subtract1 = output_curreny.subtract(multiply);

//                    BigDecimal subtract = total_amount.subtract(the_remaining_amount);
                    shengyuDc.setText(subtract1.stripTrailingZeros().toPlainString());

                    editNum.setHint("请输入USDT数量" + exchangeConfBeans1.getExchange_conf_min().stripTrailingZeros().toPlainString() + "-" + exchangeConfBeans1.getExchange_conf_max().stripTrailingZeros().toPlainString());


                    if (exchangeConfBeans.size() > 2) {
                        ExchangeConfBeans exchangeConfBeans2 = exchangeConfBeans.get(1);
                        usdtPriceXia.setText("1");
                        dcPriceXia.setText(exchangeConfBeans2.getExchange_conf_scale().stripTrailingZeros().toPlainString());
                    }
                }

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void checkMessage(String psd) {
        if (multiply3 == null) {
            showMessage("请输入数量");
            return;
        }
        Integer number_of_exchanges = exchangeConfBeans.getNumber_of_exchanges();//可兑换次数
        Integer numberOfPurchases = exchangeConfBeans.getNumberOfPurchases();//已兑换次数
        if (numberOfPurchases == null) {
            numberOfPurchases = 0;
        }
        if (number_of_exchanges <= numberOfPurchases) {
            showMessage("演替次数已超上限");
            return;
        }

        if (new BigDecimal(editNum.getText().toString()).compareTo(select.getNum()) > 0) {
            showMessage("您的可用余额不足");
            return;
        }

        if (new BigDecimal(editNum.getText().toString()).compareTo(exchangeConfBeans.getExchange_conf_min()) < 0) {
            showMessage("最小可演替数量为" + exchangeConfBeans.getExchange_conf_min());
            return;
        }

        if (new BigDecimal(editNum.getText().toString()).compareTo(exchangeConfBeans.getExchange_conf_max()) > 0) {
            showMessage("最大可演替数量为" + exchangeConfBeans.getExchange_conf_max());
            return;
        }
        tron(psd);
    }

    public void tron(String psd) {
        showLoading();
        if (select != null) {
            if (select.getNum().compareTo(new BigDecimal(editNum.getText().toString())) < 0) {
                showMessage("金额错误");
                return;
            }

            CoinBean select = dao.select(new CoinBean().setSymbol(exchangeConfBeans.getExchange_conf_spend()));
            ThreadUtil.execAsync(new Runnable() {
                @Override
                public void run() {
                    Message tron = block.tron(exchangeConfBeans.getExchange_address(), select.getContract_address(), new BigDecimal(editNum.getText().toString()), Constants.getWalletName(), psd);

                    if (tron.getType() == Message.SUCCESS) {
                        initiateRedemption(tron.getDate().toString());
                    } else {
                        hideLoading(  );

                        showMessage(tron.getMsg());

                    }
                }
            });


        }
    }

    private void initiateRedemption(String hash) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("from_hash", hash);
                put("exchange_conf_id", exchangeConfBeans.getExchange_conf_id() + "");
            }
        };

        MyApp.requestSend.sendData("exchange.method.initiateRedemption", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(message.getMsg());
                hideLoading();
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                hideLoading();
                return Message.ok();
            }
        });
    }

    private void startarea1Pop(String usdt, String dc) {
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));

        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_exchange1, null);
        TextView usdt_num = inflate.findViewById(R.id.usdt_num);
        TextView price_num = inflate.findViewById(R.id.price_num);
        price_num.setText(editNum.getText().toString() + "DC");
        usdt_num.setText(select.getAddress());
        TextView dc_num = inflate.findViewById(R.id.dc_num);
        dc_num.setText(exchangeConfBeans.getExchange_address());
//        dc_num.setText(select.getAddress());
        Button btn_ture = inflate.findViewById(R.id.btn_ture);
        ImageView exchange_record = inflate.findViewById(R.id.exchange_record);
        PopupWindow popupWindow = new PopupWindow(inflate);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int height = defaultDisplay.getHeight();
        popupWindow.setHeight((height/5)*2);
        popupWindow.showAsDropDown(findViewById(R.id.exchange_main), Gravity.CENTER, 0, 0);

        exchange_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_ture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/11/22 进行安全验证
                popupWindow.dismiss();
                start22Pop();
            }
        });
    }

    private void start22Pop() {

        PsdDialog psdDialog = new PsdDialog(getContext());

        psdDialog.setOnFlishClickListener(new PsdDialog.OnFlishClick() {
            @Override
            public void setOnFlishClick() {
                psdDialog.dismiss();
            }
        });


        psdDialog.setOnTrueClickListener(new PsdDialog.OnTrueClick() {
            @Override
            public void setOnTrueClick(String psd) {
                checkMessage(psd);
            }
        });

        psdDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
