package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.WalletManagementListAdapter;
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

import org.tron.walletserver.WalletManager;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

/**
 * 钱包管理
 * 条目 item_multichain
 * 适配器 WalletManagementListAdapter
 */
public class WalletManagementActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @Autowired
    Dao dao;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wallet_management;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_more_wellte_manage);

        List<UserWalletBean> userWalletBeans = dao.selectList(new UserWalletBean());
        String this_wallet_name = SpUtil.getInstance(this).getString("this_wallet_name", "");
        WalletManagementListAdapter walletManagementListAdapter = new WalletManagementListAdapter(this_wallet_name);
        walletManagementListAdapter.setNewInstance(userWalletBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(walletManagementListAdapter);
        walletManagementListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                showLoading();
                String token = userWalletBeans.get(position).getToken();
                Constants.loginOut();
                if (StrUtil.isNotBlank(token)) {
                    isBack=true;
                    login(token, userWalletBeans.get(position));
                    /*SpUtil.getInstance(WalletManagementActivity.this).saveString("this_wallet_name", userWalletBeans.get(position).getName());
                    walletManagementListAdapter.upDate(userWalletBeans.get(position).getName());*/
                }else{
                    hideLoading();
                }
                SpUtil.getInstance(WalletManagementActivity.this).saveString("this_wallet_name", userWalletBeans.get(position).getName());
                walletManagementListAdapter.upDate(userWalletBeans.get(position).getName());

            }
        });
    }


    public void login(String token, UserWalletBean address) {
        Map<String, Object> requestMap1 = new ArrayMap<>();
        requestMap1.put("is_login", token);
        requestMap1.put("bind_address", address.getAddress());
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        MyApp.requestSend.sendData("user.login.token", GsonUtil.toJsonString(requestMap1)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                User user = User.newBean(User.class, data);
                String is_login = user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();
                Constants.saveUserInfo(is_login, user_invitation, user_phone, user_id, user_name, is_activate);
                UserWalletBean select = dao.select(new UserWalletBean().setAddress(address.getAddress()));
                if (select==null) {
                    dao.save(new UserWalletBean().setAddress(address.getAddress()).setName(SpUtil.getInstance(WalletManagementActivity.this).getString("this_wallet_name","")).setToken(is_login));
                }else{
                    dao.updata(select.setAddress(address.getAddress()).setName(SpUtil.getInstance(WalletManagementActivity.this).getString("this_wallet_name","")).setToken(is_login));
                }
                hideLoading();
                isBack=false;
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                //返回错误信息
                Constants.loginOut();
                showMessage(message.getMsg());
                dao.updata(address.setToken(""));
                hideLoading();
                isBack=false;
                return Message.ok();
            }
        });
    }
    boolean isBack=false;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isBack) {
            //do something.

            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //导入钱包
                startActivity(new Intent(this, RecoverActivity.class).setAction("1"));
                break;
            case R.id.btn_create:
                //创建钱包
                startActivity(new Intent(this, CreateUserActivity.class).setAction("1"));
                break;
        }
    }
}
