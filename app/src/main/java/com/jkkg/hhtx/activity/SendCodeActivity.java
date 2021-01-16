package com.jkkg.hhtx.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.wynsbin.vciv.VerificationCodeInputView;

import org.tron.walletserver.WalletManager;
import org.web3j.abi.datatypes.Int;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 发送验证码
 */
public class SendCodeActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.editHide)
    VerificationCodeInputView editHide;
    @BindView(R.id.time)
    TextView time;

    private Disposable disposable;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_send_code;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarImageLeft.setVisibility(View.VISIBLE);
        phone.setText(getIntent().getStringExtra("phone"));
        editHide.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
            @Override
            public void onComplete(String code) {
                login(code);
            }

            @Override
            public void onInput() {

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.setClickable(false);
                request();
            }
        });
        time.setClickable(false);
        sendCode();

    }
    /**
     * 发送验证码
     * 倒计时
     */
    public void sendCode() {
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
                        if(time==null) return;
                        time.setText("(" + l + " s )");
                        if (l == 1) {
                            time.setClickable(true);
                            time.setText(getString(R.string.string_registration_send_code));
                            disposable.dispose();
                        }
                    }
                });
    }

    private void request(){
        showLoading();
        Map<String,String> map = new HashMap<>();
        map.put("sms_log_number",getIntent().getStringExtra("phone"));
        MyApp.requestSend.sendData("user.login.sms", GsonUtil.gsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                sendCode();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                time.setClickable(true);
                String msg = message.getMsg();
                showMessage(msg);
                return Message.ok();
            }
        });
    }

    @Autowired
    Dao dao;
    private void login(String code){
        showLoading();
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(this).getString("this_wallet_name", "")));
        Map<String,String> map = new HashMap<>();
        map.put("sms_log_code",code);
        map.put("bind_address", this_wallet_name.getAddress());
        map.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(this));
        MyApp.requestSend.sendData("user.login.phone", GsonUtil.gsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                User user=User.newBean(User.class,message.getDate());
                String is_login=user.get("is_login").toString();
                UserWalletBean select = dao.select(new UserWalletBean().setAddress(this_wallet_name.getAddress()));
                if (select==null) {
                    dao.save(new UserWalletBean().setAddress( this_wallet_name.getAddress()).setName(SpUtil.getInstance(SendCodeActivity.this).getString("this_wallet_name","")).setToken(is_login).setPhone(getIntent().getStringExtra("phone")));
                }else{
                    dao.updata(select.setAddress( this_wallet_name.getAddress()).setName(SpUtil.getInstance(SendCodeActivity.this).getString("this_wallet_name","")).setToken(is_login).setPhone(getIntent().getStringExtra("phone")));
                }
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();
                Log.d("TAG_Send",user.getUser_Invitation());
                SpUtil.getInstance(SendCodeActivity.this).saveString("invitation",user.getUser_Invitation());
                Constants.saveUserInfo(is_login,user_invitation,user_phone,user_id,user_name,is_activate);
                startActivity(new Intent(getContext(), MainActivity.class));
                AppManager.getAppManager().killActivity(MemberActivity.class);
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                String msg = message.getMsg();
                showMessage(msg);
                editHide.clearCode();
                return Message.ok();
            }
        });
    }


}