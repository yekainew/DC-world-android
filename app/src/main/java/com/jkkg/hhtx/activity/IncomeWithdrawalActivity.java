package com.jkkg.hhtx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 收益提现  通用
 * 提现金额  支付密码
 */
public class IncomeWithdrawalActivity extends BaseActivity {

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
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.edit_password)
    ClearEditText editPassword;
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.send_code)
    TextView sendCode;
    private String date;
    private String type;
    private Intent intent;


    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_income_withdrawal;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        intent = getIntent();
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");

        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_yeild_withdraw));
        if (type.equals("3")) {
            usdtNum.setText(getString(R.string.string_can_mention)+date+"USDT");
        }else{
            usdtNum.setText(getString(R.string.string_can_mention)+date+"DC");
        }
    }
    Disposable disposable;

    @OnClick({R.id.send_code, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code:
                sendSMS();
                break;
            case R.id.btn_ture:
                showLoading();
                if (StrUtil.isBlank(editNum.getText().toString())) {
                    showMessage(getString(R.string.string_num_no_null));
                    hideLoading();
                    return;
                }

                if (new BigDecimal(editNum.getText().toString()).compareTo(new BigDecimal(date))>0) {
                    showMessage(getString(R.string.string_num_no_can_mention));
                    hideLoading();
                    return;
                }

                if (StrUtil.isBlank(editPassword.getText().toString())) {
                    showMessage(getString(R.string.string_tip_down_phonecode));
                    hideLoading();
                    return;
                }

                tibi(editNum.getText().toString(),editPassword.getText().toString());
                break;
        }
    }

    private void sendSMS() {
        MyApp.requestSend.sendData("bc.method.sendSMS").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(getString(R.string.string_send_success));
                sendCode();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void tibi(String num,String yanzheng) {

        Map<String,String> map=new HashedMap<String, String>(){
            {
                if (type.equals("1")) {//签到收益
                    put("transfer_conf_reason","checkin");
                    put("bc_name","DC");
                }else if (type.equals("2")){//DC矿池收益
                    put("transfer_conf_reason","pool");
                    put("bc_name","DC");
                }else if (type.equals("3")){//理财收益
                    put("transfer_conf_reason","manage");
                    put("bc_name","USDT");
                }else if (type.equals("4")){//社区奖励
                    put("transfer_conf_reason","community");
                    put("bc_name","DC");
                }else{//兑换奖励
                    put("transfer_conf_reason","cash");
                    put("bc_name","DC");
                }
                put("withdraw_num",num);
                put("sms_log_code",yanzheng);
            }
        };
        MyApp.requestSend.sendData("bc.method.withdraw", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                showMessage(getString(R.string.string_tibi_success));
                String s = new BigDecimal(date).subtract(new BigDecimal(num)).stripTrailingZeros().toPlainString();
                if (type.equals("3")) {
                    usdtNum.setText(getString(R.string.string_can_mention)+s+"USDT");
                }else{
                    usdtNum.setText(getString(R.string.string_can_mention)+s+"DC");
                }

                intent.putExtra("setdata",s);
                setResult(101,intent);
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    /**
     * 发送验证码
     * 倒计时
     */
    public void sendCode() {
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
                        sendCode.setText("(" + l + " s )");
                        if (l == 1) {
                            sendCode.setClickable(true);
                            sendCode.setText(getString(R.string.string_registration_send_code));
                            sendCode.setBackgroundResource(R.drawable.bg_sift_corner2);
                            disposable.dispose();
                        }
                    }
                });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }
}
