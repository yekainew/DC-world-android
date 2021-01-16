package com.jkkg.hhtx.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.ExchangeConfBeanss;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.DefaultJsonBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 激活下级
 */
public class ActivationUserActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.invite_activate)
    ClearEditText inviteActivate;
    @BindView(R.id.invite_psw)
    ClearEditText invite_psw;
    @BindView(R.id.num_dc)
    TextView numDc;
    @BindView(R.id.fee_dc)
    TextView feeDc;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.avtivation_qrcode)
    ImageView avtivation_qrcode;
    @BindView(R.id.jihuo_why)
    ImageView jihuo_why;
    @BindView(R.id.buy_jihuo)
    TextView buy_jihuo;
    @BindView(R.id.jihuo_number)
    TextView jihuo_number;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_activation_user;
    }

    Disposable disposable;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_button_activate));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText(getString(R.string.string_jilu));
        toolbarTvRight.getResources().getColor(R.color.tab_color_show);
        buy_jihuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivationBuyActivity.class));
            }
        });
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录
                Intent intent = new Intent(ActivationUserActivity.this, ActivationListActivity.class);
                startActivity(intent);
            }
        });

        conf();
        exchangeConf();
        avtivation_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions(ActivationUserActivity.this);
                disposable = rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_failed_to_get_permission));
                                } else {
                                    Intent intent = new Intent(ActivationUserActivity.this, ZxingActivity.class);
                                    intent.setAction("1");// TODO: 2020/11/23 1代表从激活过来的
                                    startActivityForResult(intent, 303);
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        exchangeConf();
    }

    String result = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 303 && resultCode == RESULT_OK) {
            if (data != null) {
                result = data.getStringExtra("result");
                if (result != null) {
                    inviteActivate.setText(result);
                }
            }
        }
    }


    @OnClick({R.id.btn_join, R.id.jihuo_why, R.id.buy_jihuo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                showLoading();
                //确认激活
                if (StrUtil.isBlank(inviteActivate.getText().toString())) {
                    showMessage("请输入激活码");
                    hideLoading();
                    return;
                }

                if (StrUtil.isBlank(invite_psw.getText().toString())) {
                    showMessage("请输入密码");
                    hideLoading();
                    return;
                }
                if (usable==0) {
                    showMessage("您的激活次数不足，请购买激活次数之后重新激活");
                    hideLoading();
                    return;
                }
                checkInvite();
                break;
            case R.id.jihuo_why:

                break;
            case R.id.buy_jihuo:

                break;
            default:
                break;
        }
    }

    private int usable;
    private void exchangeConf() {
        MyApp.requestSend.sendData("invite.method.exchangeConf").main(new NetCall.Call() {




            @Override
            public Message ok(Message message) {
                ExchangeConfBeanss exchangeConfBeanss = ExchangeConfBeanss.newBean(ExchangeConfBeanss.class, message.getDate());
                usable = exchangeConfBeanss.getUsable();
                jihuo_number.setText(usable + "");
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
    private String invite_num;
    private String contract_address;

    public void conf() {
        MyApp.requestSend.sendData("invite.method.conf").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                DefaultJsonBean defaultJsonBean = DefaultJsonBean.newBean(DefaultJsonBean.class, message.getDate());
                invite_num = (String) defaultJsonBean.get("invite_num");
                contract_address = (String) defaultJsonBean.get("contract_address");
                feeDc.setText("激活需要" + invite_num + contract_address);
                AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(contract_address));

                if (select != null) {
                    numDc.setText(select.getNum().stripTrailingZeros().toPlainString() + " " + contract_address);
                } else {
                    numDc.setText("0 " + contract_address);
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @Autowired
    Block block;

    /**
     * 激活下级
     */
    private void checkInvite() {
        showLoading();
        String s = inviteActivate.getText().toString();
        Map<String, String> map = new ArrayMap<>();
        map.put("user_Invitation", s);
        MyApp.requestSend.sendData("invite.method.checkInvite", GsonUtil.toJsonString(map)).son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
               /* String s1 = message.getDate().toString();
                String[] split = s1.split("[|]");
                String s2 = split[1];
                String s3 = split[2];
                AssetsBean select = dao.select(new AssetsBean().setSymbol(s3).setWallet_name(Constants.getWalletName()));
                CoinBean select1 = dao.select(new CoinBean().setSymbol(s3));
                BigDecimal bigDecimal = new BigDecimal(s2);
                if (bigDecimal.compareTo(select.getNum()) > 0) {
                    showMessage(getString(R.string.string_money_fail));
                    hideLoading();
                } else {*/
//                    dao.save(new InvateLogBean().setHash(s4).setInvite_code(inviteActivate.getText().toString()).setNum(new BigDecimal(invite_num)).setSymbol(contract_address).setTime(new Date()).setStatus(InvateLogBean.status_0));
                activation(s);
//                }

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

    private void activation(String s) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("user_Invitation", s);
            }
        };

        MyApp.requestSend.sendData("invite.method.activation", map).son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                showMessage("激活提交成功");
                exchangeConf();
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

}
