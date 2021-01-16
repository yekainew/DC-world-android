package com.jkkg.hhtx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dx.mobile.captcha.DXCaptchaEvent;
import com.dx.mobile.captcha.DXCaptchaListener;
import com.dx.mobile.captcha.DXCaptchaView;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.Country;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录注册
 */
public class MemberActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.create_phone)
    ClearEditText createPhone;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.user_deal)
    TextView userDeal;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.img_guoqi)
    ImageView imgGuoqi;
    @BindView(R.id.text_guojia)
    TextView textGuojia;
    @BindView(R.id.text_code)
    TextView textCode;
    @BindView(R.id.choose_guojia)
    LinearLayout chooseGuojia;
    @BindView(R.id.dxCaptcha)
    com.dx.mobile.captcha.DXCaptchaView dxCaptcha;
//    private DXCaptchaView dxCaptcha;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_member;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        /*dxCaptcha = (DXCaptchaView) findViewById(R.id.dxCaptcha);
        dxCaptcha.init("20a1a9b1aa2cb5df03bc20bce7a62a32");
        dxCaptcha.setVisibility(View.INVISIBLE);*/
        toolbarImageLeft.setVisibility(View.INVISIBLE);
        toolbarImageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventImpl.goToOne());
                finish();
            }
        });


    }
  /*  String str="";
    int code;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 303 && resultCode == Activity.RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            if (country.flag != 0) {
                str = country.name;
                textGuojia.setText(str);
                code = country.code;
                Glide.with(getContext()).load(country.flag).into(imgGuoqi);
                textCode.setText("+" + code );

            }
        }
    }*/
   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dxCaptcha != null) {
            dxCaptcha.destroy();
        }
    }*/

    @OnClick({R.id.btn_true, R.id.user_deal, R.id.login, R.id.choose_guojia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_true:
                showLoading();
                String phone = createPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    hideLoading();
                    showMessage(getString(R.string.string_tip_down_phone));
                } else {
                    getSignMsg();
                }
                //发送验证码
                /*
                if (TextUtils.isEmpty(phone)) {
                    showMessage(getString(R.string.string_tip_down_phone));
                } else {
                    dxCaptcha.setVisibility(View.VISIBLE);
                    dxCaptcha.showContextMenu();
                    dxCaptcha.startToLoad(new DXCaptchaListener() {
                        @Override
                        public void handleEvent(WebView webView, DXCaptchaEvent dxCaptchaEvent, Map<String, String> map) {
                            switch (dxCaptchaEvent) {
                                case DXCAPTCHA_SUCCESS: // 验证成功的回调
                                    String token = map.get("token"); // 成功时会传递token参数
                                    Log.i(TAG, "Verify Success. token: " + token);
                                    dxCaptcha.setVisibility(View.INVISIBLE);
                                    request(phone,token);
                                    break;
                                case DXCAPTCHA_FAIL:      // 验证失败回调
                                    Log.i(TAG, "Verify Failed.");
                                    break;
                                default:
                                    break;
                            }
                        }
                    });


                }*/
                break;
            case R.id.user_deal:
                //用户协议
                break;
            case R.id.login:

                //用户协议
                break;
            case R.id.choose_guojia:
               //选择国家
//                startActivityForResult(new Intent(getContext(), PickActivity.class), 303);
                break;
            default:
                break;
        }
    }
    @Autowired
    Dao dao;
    @Autowired
    Block block;
    private void getSignMsg() {
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));

        if (select==null) {
            hideLoading();
            showMessage("请添加钱包或者更改正确的钱包");
            return;
        }

        Message sign = block.sign(select.getName(), createPhone.getText().toString(), select.getAddress());
        if (sign.getDate()==null) {
            hideLoading();
            showMessage("密码错误");
            return;
        }
        Map<String,String> map=new ArrayMap<>();
        map.put("bind_address",select.getAddress());
        map.put("sign",sign.getDate().toString());

        MyApp.requestSend.sendData("user.login.getSignMsg",map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                //要签名的东西
                String s = message.getDate().toString();
                SignMsg(select,sign,s);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    private void SignMsg(UserWalletBean bean,Message sign,String s) {
        Message sign1 = block.sign(bean.getName(), createPhone.getText().toString(), s);
        Map<String,String> map=new ArrayMap<>();
        map.put("bind_address",bean.getAddress());
        map.put("sign",sign1.getDate().toString());
        map.put("is_login",s);
        map.put("login_log_device",UniqueIdUtils.getUniquePsuedoID(this));
        MyApp.requestSend.sendData("user.login.signLogin",map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                //要签名的东西
                User user=User.newBean(User.class,message.getDate());
                String is_login=user.get("is_login").toString();
                UserWalletBean select = dao.select(new UserWalletBean().setAddress(bean.getAddress()));
                if (select==null) {
                    dao.save(new UserWalletBean().setAddress( bean.getAddress()).setName(SpUtil.getInstance(MemberActivity.this).getString("this_wallet_name","")).setToken(is_login).setPhone(getIntent().getStringExtra("phone")));
                }else{
                    dao.updata(select.setAddress( bean.getAddress()).setName(SpUtil.getInstance(MemberActivity.this).getString("this_wallet_name","")).setToken(is_login).setPhone(getIntent().getStringExtra("phone")));
                }
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call")==null?"":user.get("user_call").toString();
                Log.d("TAG_Send",user.getUser_Invitation());
                SpUtil.getInstance(MemberActivity.this).saveString("invitation",user.getUser_Invitation());
                Constants.saveUserInfo(is_login,user_invitation,user_phone,user_id,user_name,is_activate);
                hideLoading();
                startActivity(new Intent(getContext(), MainActivity.class));
                AppManager.getAppManager().killActivity(MemberActivity.class);
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                String msg = message.getMsg();
                showMessage(msg);
                createPhone.setText("");
                return Message.ok();
            }
        });
    }
//map.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(this));


    /*private void request(String phone,String token) {
        showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("sms_log_number", phone);
        map.put("token",token);
        map.put("areaCode", code+"");
        MyApp.requestSend.sendData("user.login.sms", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                startActivity(new Intent(MemberActivity.this, SendCodeActivity.class).putExtra("phone", phone));
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                String msg = message.getMsg();
                showMessage(msg);
                return Message.ok();
            }
        });
    }
*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            EventBus.getDefault().post(new EventImpl.goToOne());
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
    }*/
}