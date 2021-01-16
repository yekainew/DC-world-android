package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
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
import com.jkkg.hhtx.adapter.ManageWalletLeftAdapter;
import com.jkkg.hhtx.adapter.WalletManagementListAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.block.sql.BlockChainBean;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

/**
 * 管理钱包
 * item_left    item_mannage_wallet_left
 * item_left选中    item_mannage_wallet_left_choose
 * item_right    item_mannage_wallet_right
 */
public class ManageWalletActivity extends BaseActivity {

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
    @BindView(R.id.recyc_left)
    RecyclerView recycLeft;
    @BindView(R.id.bi_name)
    TextView biName;
    @BindView(R.id.add_wallet)
    ImageView addWallet;
    @BindView(R.id.recyc_right)
    RecyclerView recycRight;
    @Autowired
    Dao dao;

    boolean isBack=false;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_wallet;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_guanli_wallet);

        List<BlockChainBean> blockChainBeans = dao.selectList(new BlockChainBean());
        recycLeft.setLayoutManager(new LinearLayoutManager(this));
        recycLeft.setAdapter(new ManageWalletLeftAdapter(this,blockChainBeans));

        List<UserWalletBean> userWalletBeans = dao.selectList(new UserWalletBean());
        String this_wallet_name = SpUtil.getInstance(this).getString("this_wallet_name", "");
        WalletManagementListAdapter walletManagementListAdapter = new WalletManagementListAdapter(this_wallet_name);
        walletManagementListAdapter.setNewInstance(userWalletBeans);
        recycRight.setLayoutManager(new LinearLayoutManager(this));
        recycRight.setAdapter(walletManagementListAdapter);
        walletManagementListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                showLoading();
                String token = userWalletBeans.get(position).getToken();
                Constants.loginOut();
                if (StrUtil.isNotBlank(token)) {
                    isBack=true;
                    login(token, userWalletBeans.get(position));
//                    SpUtil.getInstance(ManageWalletActivity.this).saveString("this_wallet_name", userWalletBeans.get(position).getName());
//                    walletManagementListAdapter.upDate(userWalletBeans.get(position).getName());
                }else{
                    hideLoading();
                }
                SpUtil.getInstance(ManageWalletActivity.this).saveString("this_wallet_name", userWalletBeans.get(position).getName());
                walletManagementListAdapter.upDate(userWalletBeans.get(position).getName());

                finish();
            }
        });

        walletManagementListAdapter.setOnStartClickListener(new WalletManagementListAdapter.OnStartClick() {
            @Override
            public void setOnStartClick(String name) {
                Intent intent = new Intent(getContext(), CreateSelectActivity.class);
                intent.putExtra("wallet_name",name);
                startActivity(intent);
            }
        });
    }

    private void login(String token, UserWalletBean userWalletBean) {
        Map<String, Object> requestMap1 = new ArrayMap<>();
        requestMap1.put("is_login", token);
        System.out.println("本地地址》》》" + userWalletBean.getAddress());
        requestMap1.put("bind_address", userWalletBean.getAddress());
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        MyApp.requestSend.sendData("user.login.token", GsonUtil.toJsonString(requestMap1)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data = message.getDate().toString();
                User user = User.newBean(User.class, data);
//                String bindState = user.get("bindState").toString();
                String is_login = user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();

                UserWalletBean select = dao.select(new UserWalletBean().setAddress(userWalletBean.getAddress()));
                if (select == null) {
                    dao.save(new UserWalletBean().setAddress(userWalletBean.getAddress()).setName(SpUtil.getInstance(ManageWalletActivity.this).getString("this_wallet_name", "")).setToken(is_login));
                } else {
                    dao.updata(select.setAddress(userWalletBean.getAddress()).setName(SpUtil.getInstance(ManageWalletActivity.this).getString("this_wallet_name", "")).setToken(is_login));
                }
//                if (StringUtils.isNotEmpty(bindState) && getString(R.string.string_zhu_or_si_null).equals(bindState)) {
//                    startActivity(new Intent(getContext(), MemberActivity.class));
//                } else {
                Constants.saveUserInfo(is_login, user_invitation, user_phone, user_id, user_name, is_activate);
//                    startActivity(new Intent(getContext(), MainActivity.class));
//                }
                finish();
//                overridePendingTransition(0, 0);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                //返回错误信息
                Constants.loginOut();
                showMessage(message.getMsg());
                dao.updata(userWalletBean.setToken(""));
                startActivity(new Intent(getContext(), MemberActivity.class));
                finish();
//                overridePendingTransition(0, 0);
                return Message.ok();
            }
        });
    }


    @OnClick(R.id.add_wallet)
    public void onViewClicked() {
        //添加钱包
        startActivity(new Intent(this,AddWalletActivity.class));
    }
}
