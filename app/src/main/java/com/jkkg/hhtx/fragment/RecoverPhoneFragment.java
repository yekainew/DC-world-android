package com.jkkg.hhtx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.CreateSelectHelpActivity;
import com.jkkg.hhtx.activity.MainActivity;
import com.jkkg.hhtx.activity.RecoverActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 手机号恢复
 */
public class RecoverPhoneFragment extends BaseFragment {
    @BindView(R.id.recover_phone)
    ClearEditText recoverPhone;
    @BindView(R.id.recover_phone_code)
    ClearEditText recoverPhoneCode;
    @BindView(R.id.send_code)
    TextView sendCode;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    Disposable disposable;

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover_phone, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.send_code, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code:
                //点击发送验证码
                String phone = recoverPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    showMessage(getString(R.string.string_tip_down_phone));
                }else {
                    sendCode.setBackground(null);
                    sendCode.setClickable(false);
                    sendSMS();
                }
                break;
            case R.id.btn_true:
                //恢复身份
                String code = recoverPhoneCode.getText().toString();
                String phone1 = recoverPhone.getText().toString();
                if (TextUtils.isEmpty(phone1)){
                    showMessage(getString(R.string.string_tip_down_phone));
                }else if (TextUtils.isEmpty(code)){
                    showMessage(getString(R.string.string_tip_down_phonecode));
                }else {
                    requestLogin();
                }
                break;
            default:
                break;
        }
    }
    private void sendSMS(){
        showLoading();
        String phone = recoverPhone.getText().toString();
        Map<String, String> requestMap = new ArrayMap<>();
        requestMap.put("sms_log_number", phone);
        MyApp.requestSend.sendData("user.insert.sendSMS", GsonUtil.toJsonString(requestMap)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                sendCode();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                sendCode.setClickable(true);
                sendCode.setText(getString(R.string.string_registration_send_code));
                sendCode.setBackgroundResource(R.drawable.bg_sift_corner2);
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void requestLogin(){
        showLoading();
        String code = recoverPhoneCode.getText().toString();
        String phone1 = recoverPhone.getText().toString();
        //请求数据
        Map<String, Object> requestMap1 = new ArrayMap<>();
        requestMap1.put("login_type", 3);
        requestMap1.put("user_phone", phone1);
        requestMap1.put("sms_code", code);
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        MyApp.requestSend.sendData("user.insert.login", GsonUtil.toJsonString(requestMap1)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data=message.getDate().toString();
                User user=User.newBean(User.class,data);
                String bindState=user.get("bindState").toString();
                String is_login=user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();
                Constants.saveUserInfo(is_login,user_invitation,user_phone,user_id,user_name,is_activate);

                if(getString(R.string.string_zhu_or_si_null).equals(bindState)){
                    startActivity(new Intent(getContext(),CreateSelectHelpActivity.class));
                }else{
                    AppManager.getAppManager().killAll(RecoverActivity.class);
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
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
    public void onDestroyView() {
        super.onDestroyView();
        if(disposable!=null){
            disposable.dispose();
        }
    }
}