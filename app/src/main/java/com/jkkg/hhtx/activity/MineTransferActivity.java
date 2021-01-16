package com.jkkg.hhtx.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.net.bean.TansferDetailBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.PublicChainNameEnum;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.PsdDialog;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.util.Other;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Created by ccw on 09/11/2020 11:38
 * Email:chencw0715@163.com
 * <p>
 * 转账
 * pop_exchange1
 * pop_exchange2
 *
 * @author admin6
 */
public class MineTransferActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.transfer_phone1)
    ClearEditText transferPhone1;
    @BindView(R.id.transfer_yue1)
    TextView transferYue1;
    @BindView(R.id.transfer_num1)
    ClearEditText transferNum1;/*
    @BindView(R.id.transfer_phone_code1)
    ClearEditText transferPhoneCode1;*/
    @BindView(R.id.send_code1)
    TextView sendCode1;
    @BindView(R.id.transfer_fee1)
    TextView transferFee1;
    @BindView(R.id.transfer_min_num1)
    TextView transferMinNum1;
    @BindView(R.id.btn_true1)
    Button btnTrue1;
    String bb_name = "USDT";
    TansferDetailBean tansferDetailBean;
    Disposable disposable;
    @BindView(R.id.transfer_cramar)
    ImageView transferCramar;
    @BindView(R.id.start_address)
    ImageView start_address;
    @BindView(R.id.choose_bi)
    TextView chooseBi;
    @BindView(R.id.gaoji_set)
    TextView gaojiSet;
    @BindView(R.id.set_gone)
    LinearLayout setGone;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<CoinBean> list;
    String result = "";

    boolean isGone = false;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_transfer;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_transfer));
        list = dao.selectList(new CoinBean());
        Intent intent = getIntent();

        result = intent.getStringExtra("result");
        bb_name = intent.getStringExtra("btc");
        transferPhone1.setText(result);

        chooseBi.setText(bb_name);
        if (isGone) {
            setGone.setVisibility(View.VISIBLE);
        } else {
            setGone.setVisibility(View.GONE);
        }

        requestWallet(bb_name);
    }

    @Autowired
    private Dao dao;

    @Autowired
    public Block block;

    @OnClick({R.id.send_code1, R.id.btn_true1, R.id.transfer_cramar, R.id.choose_bi, R.id.gaoji_set, R.id.start_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code1:
                sendCode1.setBackground(null);
                sendCode1.setClickable(false);
                requestCode();
                //获取验证码
                break;
            case R.id.btn_true1:

                //提交
                if (TextUtils.isEmpty(transferPhone1.getText().toString())) {
                    showMessage(getString(R.string.string_edit_user_phone));
                    return;
                }
                /*if (TextUtils.isEmpty(transferPhoneCode1.getText().toString())) {
                    showMessage(getString(R.string.string_edit_code));
                    return;
                }*/
                if (TextUtils.isEmpty(transferNum1.getText().toString())) {
                    showMessage(getString(R.string.string_transfer_num));
                    return;
                }

                startarea1Pop("1", "2");
                break;
            case R.id.transfer_cramar:

                RxPermissions rxPermissions = new RxPermissions(MineTransferActivity.this);
                disposable = rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_failed_to_get_permission));
                                } else {
                                    Intent intent = new Intent(MineTransferActivity.this, ZxingActivity.class);
                                    intent.setAction("2");
                                    startActivityForResult(intent, 303);
                                }
                            }
                        });
                break;
            case R.id.choose_bi:
                startActivityForResult(new Intent(this, SelectBcNameActivity.class), 101);
                break;
            case R.id.gaoji_set:
                if (isGone) {
                    setGone.setVisibility(View.VISIBLE);
                    isGone = false;
                } else {

                    setGone.setVisibility(View.GONE);
                    isGone = true;
                }
                break;

            case R.id.start_address:
                Intent intent = new Intent(this, MineAddressBookActivity.class);
                intent.setAction("1");
                startActivityForResult(intent, 101);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 303 && resultCode == RESULT_OK) {
            if (data != null) {
                result = data.getStringExtra("result");
                if (result != null) {
                    transferPhone1.setText(result);
                }
            }
        }

        if (requestCode == 101) {
            if (resultCode == 303) {
                if (data != null) {
                    bb_name = data.getStringExtra("name");
                    chooseBi.setText(bb_name);
                    requestWallet(bb_name);
                }
            } else if (resultCode == 404) {
                String address = data.getStringExtra("address");
                transferPhone1.setText(address);

            }


        }
    }


    private void requestWallet(String currency_name) {

        AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(currency_name));
        BigDecimal num_usd = select.getNum();

        Log.d("symbol", num_usd + "");
        transferYue1.setText(bb_name + getString(R.string.string_moneyyue) + num_usd + "");

    }

    private void requestCode() {
        showLoading();
        MyApp.requestSend.sendData("transfer.method.sendSMS").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                sendCode();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                sendCode1.setClickable(true);
                sendCode1.setText(getString(R.string.string_registration_send_code));
                sendCode1.setBackgroundResource(R.drawable.bg_sift_corner2);
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    @Autowired
    private BagSend bagSend;

    private void requestTransfer(String psd) {
        showLoading();
        String phone = transferPhone1.getText().toString();/*
        String s = transferPhoneCode1.getText().toString();*/
        WalletBean wallet = block.getWallet(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", ""), psd);
        if (wallet == null || StringUtils.isBlank(wallet.getPri())) {
            //提示钱包密码错误
            showMessage("钱包密码错误");
            hideLoading();
            return;
        }
        //TODO 需填入币种名称
        CoinBean coin = dao.select(new CoinBean().setSymbol(bb_name));
        AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(coin.getSymbol()));
        if (!Other.isDouble(transferNum1.getText().toString())) {
            //提示金额错误
            showMessage("金额错误");
            hideLoading();
            return;
        }
        BigDecimal bigDecimal = new BigDecimal(transferNum1.getText().toString());
        if (bigDecimal.compareTo(select.getNum()) > 0) {
            //提示金额错误
            showMessage("金额错误");
            hideLoading();
            return;
        }

        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {
                Message tron = block.tron(phone, coin.getContract_address(), bigDecimal, SpUtil.getInstance(DataSave.app).getString("this_wallet_name", ""), psd);
                if (tron.getType() == Message.SUCCESS) {
                    showMessage(getString(R.string.string_transfer_success));
                    dao.save(new AssetsLogBean().setHash(tron.getDate().toString()).setTo_phone(phone));
                    finish();
                    //转账成功
                } else {
                    showMessage(tron.getMsg());
                    //失败提醒错误原因
                }
                hideLoading();
            }
        });

    }

    /**
     * 发送验证码
     * 倒计时
     */
    public void sendCode() {
        //发送成功
        //倒计时
        disposable = Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 60 - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long l) throws Exception {
                        sendCode1.setText("(" + l + ")");
                        if (l == 1) {
                            sendCode1.setClickable(true);
                            sendCode1.setText(getString(R.string.string_registration_send_code));
                            sendCode1.setBackgroundResource(R.drawable.bg_sift_corner2);
                            disposable.dispose();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void startarea1Pop(String usdt, String dc) {
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));

        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_exchange1, null);
        TextView usdt_num = inflate.findViewById(R.id.usdt_num);
        TextView price_num = inflate.findViewById(R.id.price_num);
        price_num.setText(transferNum1.getText().toString() + bb_name);
        usdt_num.setText(select.getAddress());
        TextView dc_num = inflate.findViewById(R.id.dc_num);
        dc_num.setText(transferPhone1.getText().toString());
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
        popupWindow.showAsDropDown(findViewById(R.id.transfer_main), Gravity.CENTER, 0, 0);

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
                requestTransfer(psd);
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

    @OnClick(R.id.gaoji_set)
    public void onViewClicked() {
    }
}
