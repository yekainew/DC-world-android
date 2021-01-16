package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.event.EventBusTags;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.StringUtils;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.tron.walletserver.WalletManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
 * Description:
 * Created by ccw on 09/14/2020 10:44
 * Email:chencw0715@163.com
 * 创建账户
 */
public  class CreateUserActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.create_password)
    ClearEditText createPassword;
    @BindView(R.id.hide_password)
    ImageView hidePassword;
    @BindView(R.id.create_password2)
    ClearEditText createPassword2;
    @BindView(R.id.hide_password2)
    ImageView hidePassword2;
    @BindView(R.id.create_pay_password)
    ClearEditText createPayPassword;
    @BindView(R.id.hide_pay_password)
    ImageView hidePayPassword;
    @BindView(R.id.create_phone)

    ClearEditText createPhone;
    @BindView(R.id.create_phone_code)
    ClearEditText createPhoneCode;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.create_name)
    ClearEditText createName;
    @BindView(R.id.create_pay_password2)
    ClearEditText createPayPassword2;
    @BindView(R.id.hide_pay_password2)
    ImageView hidePayPassword2;
    @BindView(R.id.send_code)
    TextView sendCode;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;



    private boolean isShowPassword1 = false;
    private boolean isShowPassword2 = false;
    private boolean isShowPassword3 = false;
    private boolean isShowPassword4 = false;
    private Disposable disposable;
    String action="";
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_user;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_createuser);
        toolbarImageLeft.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent.getAction()!=null) {
            if (intent.getAction().equals("1")) {
                action="1";
            }
        }

        hidePassword.setImageResource(R.mipmap.icon_login_eye_down);
        createPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        hidePassword2.setImageResource(R.mipmap.icon_login_eye_down);
        createPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        hidePayPassword.setImageResource(R.mipmap.icon_login_eye_down);
        createPayPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        hidePayPassword2.setImageResource(R.mipmap.icon_login_eye_down);
        createPayPassword2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    @OnClick({R.id.hide_password, R.id.hide_password2, R.id.hide_pay_password, R.id.hide_pay_password2,R.id.btn_true, R.id.send_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hide_password:
                //密码 隐藏或显示
                if (isShowPassword1) {
                    isShowPassword1 = false;
                    hidePassword.setImageResource(R.mipmap.icon_login_eye_down);
                    createPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    createPassword.setSelection(createPassword.getText().toString().length());
                } else {
                    isShowPassword1 = true;
                    hidePassword.setImageResource(R.mipmap.icon_login_eye_up);
                    createPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    createPassword.setSelection(createPassword.getText().toString().length());
                }
                break;
            case R.id.hide_password2:
                //再次确定密码 隐藏或显示
                if (isShowPassword2) {
                    isShowPassword2 = false;
                    hidePassword2.setImageResource(R.mipmap.icon_login_eye_down);
                    createPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    createPassword2.setSelection(createPassword2.getText().toString().length());
                } else {
                    isShowPassword2 = true;
                    hidePassword2.setImageResource(R.mipmap.icon_login_eye_up);
                    createPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    createPassword2.setSelection(createPassword2.getText().toString().length());
                }
                break;
            case R.id.hide_pay_password:
                //支付密码 隐藏或显示
                if (isShowPassword3) {
                    isShowPassword3 = false;
                    hidePayPassword.setImageResource(R.mipmap.icon_login_eye_down);
                    createPayPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    createPayPassword.setSelection(createPayPassword.getText().toString().length());
                } else {
                    isShowPassword3= true;
                    hidePayPassword.setImageResource(R.mipmap.icon_login_eye_up);
                    createPayPassword.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    createPayPassword.setSelection(createPayPassword.getText().toString().length());
                }
                break;
            case R.id.hide_pay_password2:
                //确认支付密码 隐藏或显示
                if (isShowPassword4) {
                    isShowPassword4 = false;
                    hidePayPassword2.setImageResource(R.mipmap.icon_login_eye_down);
                    createPayPassword2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    createPayPassword2.setSelection(createPayPassword2.getText().toString().length());
                } else {//
                    isShowPassword4 = true;
                    hidePayPassword2.setImageResource(R.mipmap.icon_login_eye_up);
                    createPayPassword2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    createPayPassword2.setSelection(createPayPassword2.getText().toString().length());
                }
                break;
            case R.id.send_code:
                //点击发送验证码
                String phone = createPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    showMessage(getString(R.string.string_tip_down_phone));
                }else {
                    sendCode.setClickable(false);
                    sendCode.setBackground(null);
                    sendCodeRequest();
                }
                break;
            case R.id.btn_true:
//                // 判断注册的条件
//                // 1.身份名不能超过10个字符
//                // 2.登录密码(8-16数字与字母组合)
//                // 3.支付密码(6位数字)
//                // 4.create_password==create_password2   createPayPassword==createPayPassword2
                String name = createName.getText().toString();
                String password = createPassword.getText().toString();
                String password2 = createPassword2.getText().toString();
                String paypassword = createPayPassword.getText().toString();
                String paypassword2 = createPayPassword2.getText().toString();
                String phone1 = createPhone.getText().toString();
                String phoneCode = createPhoneCode.getText().toString();

                String string = "";
                if (string.equals(password.trim())) {
                    showMessage(getString(R.string.string_dlpassword_no_null));
                    return;
                }
                if (string.equals(password2.trim())) {
                    showMessage(getString(R.string.string_dlpassword_queren));
                    return;
                }

               if (!password.equals(password2)){
                    showMessage(getString(R.string.string_tip_create_password));
                    return;
                }

                if (string.equals(name)) {
                    showMessage(getString(R.string.string_wallet_name_no_null));
                    return;
                }

                try {
                    UserWalletBean select = dao.select(new UserWalletBean().setName(name));


                    if (select!=null) {
                        showMessage(getString(R.string.string_wallet_cunzia));
                        return;
                    }
                    block.createWallet(name,password);
                    Intent intent = new Intent(this, CreateSelectHelpActivity.class);
                    intent.putExtra("qianbao",block.getWallet(name,password));
                    intent.putExtra("type","2");
                    intent.setAction(action);
                    if (select==null) {
                        dao.save(new UserWalletBean().setName(name).setAddress(WalletManager.getWallet(name).getAddress()));
                    }
                    if (action!=null) {
                        if (!action.equals("1")) {
                            SpUtil.getInstance(this).saveString("this_wallet_name",name);
                        }
                    }
                    startActivity(intent);
//                    dao.save(new UserWalletBean().setAddress(block.getWallet(name,password).getAddress()).setName(name));
                    finish();
                } catch (Exception e) {
                    showMessage(e.getMessage());
                }
                break;
            default:
                break;
        }
    }
    @Autowired
    Dao dao;

    @Autowired
    public Block block;

    private void sendCodeRequest(){
        showLoading();
        String phone = createPhone.getText().toString();
        Map<String, String> requestMap = new ArrayMap<>();
        requestMap.put("sms_log_number", phone);
        MyApp.requestSend.sendData("user.insert.sendCode", GsonUtil.toJsonString(requestMap)).main(new NetCall.Call() {
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

    private void register(){
        showLoading();
        String name = createName.getText().toString();
        String password = createPassword.getText().toString();
        String paypassword = createPayPassword.getText().toString();
        String phone1 = createPhone.getText().toString();
        String phoneCode = createPhoneCode.getText().toString();
        Map<String, String> requestMap1 = new ArrayMap<>();
        requestMap1.put("user_call", name);
        requestMap1.put("user_password", password);
        requestMap1.put("user_phone", phone1);
        requestMap1.put("user_pay_password", paypassword);
        requestMap1.put("sms_code", phoneCode);
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        MyApp.requestSend.sendData("user.insert.register", GsonUtil.toJsonString(requestMap1)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data=message.getDate().toString();
                User user=User.newBean(User.class,data);
                String is_login=user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();
                Constants.saveUserInfo(is_login,user_invitation,user_phone,user_id,user_name,is_activate);
                startActivity(new Intent(CreateUserActivity.this,CreateSelectHelpActivity.class));
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
