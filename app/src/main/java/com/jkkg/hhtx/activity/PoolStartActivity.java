package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;


/**
 * Description:
 * Created by ccw on 09/16/2020 18:45
 * Email:chencw0715@163.com
 * 启动矿机
 *
 * @author admin6
 */
public class PoolStartActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pool_name)
    TextView poolName;
    @BindView(R.id.pool_choose)
    RelativeLayout poolChoose;
    @BindView(R.id.pool_dc_number)
    ClearEditText poolDcNumber;
    @BindView(R.id.pool_all)
    TextView poolAll;
    @BindView(R.id.pool_get)
    TextView poolGet;
    @BindView(R.id.pool_dc_all)
    TextView poolDcAll;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.pool_dc_psw)
    ClearEditText pool_dc_psw;
    @BindView(R.id.price_name)
    TextView priceName;
    @BindView(R.id.name_num)
    TextView nameNum;
    private PopupWindow popupWindow;
    private PoolConfigBean poolConfigBean;
    private BigDecimal num;
    private String contract_address;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pool_start;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_tab_pool_start));

        poolDcNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Other.isDouble(s.toString())) {
                    BigDecimal bigDecimal = new BigDecimal(s.toString());
                    if (bigDecimal.compareTo(new BigDecimal("10000")) > 0) {
                        String s1 = bigDecimal.subtract(new BigDecimal("10000")).multiply(new BigDecimal("10")).add(new BigDecimal("10000")).stripTrailingZeros().toPlainString();
                        poolGet.setText("预计获得的算力值 : " + s1 + "GH/S");
                    } else {
                        String s2 = bigDecimal.multiply(new BigDecimal("10")).stripTrailingZeros().toPlainString();
                        poolGet.setText("预计获得的算力值 : " + s2 + "GH/S");
                    }
                } else {
                    poolGet.setText("预计获得的算力值 : 0 GH/S");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        config();

        getDcNum();
    }

    @Autowired
    Dao dao;

    @Autowired
    Block block;

    private void getDcNum() {
        AssetsBean dc = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("DCB"));
        num = dc.getNum();
        CoinBean dc1 = dao.select(new CoinBean().setSymbol("DCB"));

        contract_address = dc1.getContract_address();
        if (num != null) {
            poolDcAll.setText(num.toPlainString());
        } else {
            poolDcAll.setText(0 + "");
        }

    }

    private void config() {
        showLoading();
        MyApp.requestSend.sendData("holdarea.method.conf").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                Gson gson = new Gson();
                List<PoolConfigBean> poolConfigBeans = gson.fromJson(message.getDate().toString(), new TypeToken<List<PoolConfigBean>>() {
                }.getType());
                if (poolConfigBeans != null) {
                    if (poolConfigBeans.size() > 0) {
                        poolConfigBean = poolConfigBeans.get(0);
                        String hold_area_name = poolConfigBean.getHold_area_name();
                        poolName.setText(hold_area_name);
                        String name = hold_area_name.replace("矿机", "");
                        poolDcNumber.setHint("请输入" + name + "的数量");
                        priceName.setText(name);
                        nameNum.setText(name+"可使用数量:");
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

    @OnClick({R.id.pool_choose, R.id.pool_all, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pool_choose:
                //矿机选择

                break;
            case R.id.pool_all:
                //dc总数量
                if (num != null) {
                    poolDcNumber.setText(num.toPlainString());
                }
                break;
            case R.id.btn_true:
                //点击开启矿机
                showLoading();
                String max_invest_num = poolConfigBean.getHoldAreaConfBean().getMax_invest_num();
                String min_invest_num = poolConfigBean.getHoldAreaConfBean().getMin_invest_num();

                if (min_invest_num.compareTo(poolDcNumber.getText().toString()) > 0) {
                    showMessage("不可小于这么多");
                    return;
                }
                if (poolDcNumber.getText().toString().compareTo(max_invest_num) > 0) {
                    showMessage("不可大于这么多");
                    return;
                }

                if (StrUtil.isBlank(pool_dc_psw.getText().toString())) {
                    showMessage("请输入钱包密码");
                    return;
                }
                if (StrUtil.isBlank(poolDcNumber.getText().toString())) {
                    showMessage("金额不能为空");
                    return;
                }
                BigDecimal bigDecimal = new BigDecimal(poolDcNumber.getText().toString());
                ThreadUtil.execAsync(new Runnable() {
                    @Override
                    public void run() {
                        Message tron = block.tron(poolConfigBean.getInvest_address(), contract_address, bigDecimal, SpUtil.getInstance(PoolStartActivity.this).getString("this_wallet_name",""),pool_dc_psw.getText().toString());
                        if (tron.getType() == Message.SUCCESS) {
                            showMessage("购买成功");

                            hideLoading();
                            finish();
                        } else {
                            showMessage(tron.getMsg());
                            hideLoading();
                        }

                    }
                });


                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
