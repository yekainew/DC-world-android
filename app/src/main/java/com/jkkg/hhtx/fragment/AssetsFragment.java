package com.jkkg.hhtx.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.AddWalletActivity;
import com.jkkg.hhtx.activity.AssetDetailActivity;
import com.jkkg.hhtx.activity.BroadbandActivity;
import com.jkkg.hhtx.activity.CreateSelectActivity;
import com.jkkg.hhtx.activity.ExchangeActivity;
import com.jkkg.hhtx.activity.ManageWalletActivity;
import com.jkkg.hhtx.activity.MemberActivity;
import com.jkkg.hhtx.activity.MineExtractActivity;
import com.jkkg.hhtx.activity.MineRechargeActivity;
import com.jkkg.hhtx.activity.MineTransferActivity;
import com.jkkg.hhtx.activity.WalletManageActivity;
import com.jkkg.hhtx.activity.WalletManagementActivity;
import com.jkkg.hhtx.activity.ZxingActivity;
import com.jkkg.hhtx.adapter.AssetsAdapter;
import com.jkkg.hhtx.adapter.ManageWalletLeftAdapter;
import com.jkkg.hhtx.adapter.WalletManagementListAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.net.bean.OtcWalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.sql.BlockAssetsBean;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.block.sql.BlockWalletBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 资产
 * pop_asset_list
 */
public class AssetsFragment extends BaseFragment {

    @BindView(R.id.asset_select)
    ImageView assetSelect;
    @BindView(R.id.assets_code)
    ImageView assetsCode;
    @BindView(R.id.asstes_address)
    TextView asstesAddress;
    @BindView(R.id.assets_copy)
    ImageView assetsCopy;
    @BindView(R.id.assets_more)
    ImageView assetsMore;
    @BindView(R.id.asstes_usdt)
    TextView asstesUsdt;
    @BindView(R.id.asset_title)
    TextView asset_title;
    @BindView(R.id.mine_tab1)
    LinearLayout mineTab1;
    @BindView(R.id.mine_tab2)
    LinearLayout mineTab2;
    @BindView(R.id.mine_tab3)
    LinearLayout mineTab3;
    @BindView(R.id.mine_tab4)
    LinearLayout mineTab4;
    @BindView(R.id.assets_rec)
    RecyclerView assetsRec;
    @BindView(R.id.choose_bi)
    TextView chooseBi;
    @BindView(R.id.asstes_detail)
    TextView asstesDetail;
    @BindView(R.id.eye_show)
    ImageView eyeShow;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.assets_jiedian)
    ImageView assetsJiedian;
    @BindView(R.id.asset_tab1)
    LinearLayout assetTab1;
    @BindView(R.id.asset_tab2)
    LinearLayout assetTab2;
    @BindView(R.id.asset_tab3)
    LinearLayout assetTab3;
    @BindView(R.id.asset_tab4)
    LinearLayout assetTab4;
    @BindView(R.id.sousuo)
    ImageView sousuo;
    @BindView(R.id.sousuo_edit)
    ClearEditText sousuoEdit;
    @BindView(R.id.asset_add)
    ImageView assetAdd;
    @BindView(R.id.assets_smart)
    SmartRefreshLayout assetsSmart;

    private Intent intent;
    private boolean islogin;
    @Autowired
    private Dao dao;
    private AssetsAdapter assetsAdapter;
    private String s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }


    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

        assetsAdapter = new AssetsAdapter(getContext(), new ArrayList<>(), new ArrayList<>(), "0.3");
        assetsRec.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRec.setAdapter(assetsAdapter);

        assetsSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initDate();
            }
        });
        assetsAdapter.setOnClickListener(new AssetsAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, OtcWalletBean.ListBean listBean) {
                //点击条目进详情
                Intent intent = new Intent(mContext, AssetDetailActivity.class);
                intent.putExtra("assetdetail", listBean);
                startActivity(intent);
            }
        });


        initDate();
        Glide.with(mContext).load(R.drawable.icon_logo_ff).into(img);
    }



    private void initDate() {
        BigDecimal all = BigDecimal.ZERO;
        BigDecimal all_cny = BigDecimal.ZERO;
        List<OtcWalletBean.ListBean> list = new LinkedList<>();
        chooseBi.setText(Constants.getWalletName());
        BlockWalletBean now_bean = dao.select(new BlockWalletBean().setWallet_name(Constants.getWalletName()));
        List<BlockCoinBean> coinBeans = dao.selectList(new BlockCoinBean().setBlock_wallet_id(now_bean.getBlock_wallet_id()));
        for (BlockCoinBean bean : dao.selectList(new BlockCoinBean())) {
            BlockAssetsBean select = dao.select(new BlockAssetsBean().setBlock_wallet_id(bean.getBlock_wallet_id()).setSymbol(bean.getSymbol()));
            if (select != null) {
                OtcWalletBean.ListBean bean1 = new OtcWalletBean.ListBean();
                bean1.setUsable(select.getNum().stripTrailingZeros().toPlainString());
                bean1.setRMB(select.getNum_cny().stripTrailingZeros().toPlainString());
                bean1.setUSDT(select.getNum_usd().stripTrailingZeros().toPlainString());
                bean1.setCurrency_name(bean.getSymbol());
                bean1.setFrozen("0");
                all = all.add(select.getNum_usd());
                all_cny = all.add(select.getNum_cny());
                list.add(bean1);
            }

        }

        s = all.stripTrailingZeros().toPlainString();
        if (isShow) {
            asstesUsdt.setText("≈" + s + " USDT");
        } else {
            asstesUsdt.setText("≈" + "****" + " USDT");
        }

//        asstesUsdt.setText("≈" +"305.681032"+ " BTC");
        String s1 = all_cny.stripTrailingZeros().toPlainString();

        assetsAdapter.setDate(list, coinBeans);
           BlockWalletBean select = dao.select(new BlockWalletBean().setWallet_name(Constants.getWalletName()));
        String address = select.getAddress();
        asset_title.setText(Constants.getWalletName());
        asstesAddress.setText(address);
        assetsSmart.finishRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDate();
    }

    Disposable disposable;
    boolean isShow = true;

    @OnClick({R.id.asset_select, R.id.assets_code, R.id.assets_copy, R.id.assets_more, R.id.mine_tab1, R.id.mine_tab2, R.id.mine_tab3, R.id.mine_tab4, R.id.choose_bi, R.id.asstes_detail, R.id.eye_show, R.id.assets_jiedian, R.id.asset_tab1, R.id.asset_tab2, R.id.asset_tab3, R.id.asset_tab4, R.id.sousuo, R.id.sousuo_edit, R.id.asset_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.asset_select:
                //钱包选择
                startActivity(new Intent(mContext, WalletManagementActivity.class));
                break;
            case R.id.assets_code:
                //扫一扫
                RxPermissions rxPermissions = new RxPermissions(this);
                disposable = rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_failed_to_get_permission));
                                } else {
                                    Intent intent = new Intent(mContext, ZxingActivity.class);
                                    intent.setAction("3");
                                    // TODO: 2020/11/23 2代表从其他页面过来的
                                    startActivityForResult(intent, 303);
                                }
                            }
                        });
                break;
            case R.id.assets_copy:
                //复制地址
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, asstesAddress.getText().toString());
                clipboard.setPrimaryClip(clipData);
                showMessage("复制成功");
                break;
            case R.id.assets_more:
                //钱包管理
                startActivity(new Intent(mContext, WalletManageActivity.class));
                break;
            case R.id.mine_tab1:
                //充币
                //                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                //                    showMessage(getString(R.string.string_text_mine_activate_tab1));
                //                } else {
                intent = new Intent(mContext, MineRechargeActivity.class);
                startActivity(intent);
                //                }
                break;
            case R.id.mine_tab2:
                //提币
                //                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                //                    showMessage(getString(R.string.string_text_mine_activate_tab2));
                //                } else {
                intent = new Intent(mContext, MineExtractActivity.class);
                startActivity(intent);
                //                }
                break;
            case R.id.mine_tab3:
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                //兑换
                //                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                //                    showMessage(getString(R.string.string_text_mine_activate_tab3));
                //                } else {
               /* intent = new Intent(mContext, ExchangeFiashActivity.class);
                startActivity(intent);*/
                startActivity(new Intent(getContext(), ExchangeActivity.class));
                //                }
                break;
            case R.id.mine_tab4:
                //转账
                //                if ("ToBeActivated".equals(Constants.getUserActivate())) {
                //                    showMessage(getString(R.string.string_text_mine_activate_tab4));
                //                } else {
                intent = new Intent(mContext, MineTransferActivity.class);
                intent.putExtra("btc","USDT");
                startActivity(intent);
                //                }
                break;
            case R.id.choose_bi:
//                startActivity(new Intent(mContext, ManageWalletActivity.class));
                startarea1Pop();
                break;
            case R.id.asstes_detail:
                //钱包中心WalletManageActivity
                startActivity(new Intent(mContext, WalletManageActivity.class));
                break;
            case R.id.eye_show:
                if (isShow) {
                    isShow = false;
                    asstesUsdt.setText("≈" + "****" + " USDT");
                } else {
                    isShow = true;
                    asstesUsdt.setText("≈" + s + " USDT");
                }
                break;

            case R.id.assets_jiedian:
                //节点设置
                break;
            case R.id.asset_tab1:
                startActivity(new Intent(mContext, BroadbandActivity.class));
//                showMessage("暂未开放");
                break;
            case R.id.asset_tab2:
                showMessage("暂未开放");
                break;
            case R.id.asset_tab3:
                showMessage("暂未开放");
                break;
            case R.id.asset_tab4:
                showMessage("暂未开放");
                break;
            case R.id.sousuo:
                break;
            case R.id.sousuo_edit:
                break;
            case R.id.asset_add:
                showMessage("暂未开放");
                break;
        }
    }

    boolean isBack=false;

    private void startarea1Pop() {
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
        List<BlockChainBean> blockChainBeans = dao.selectList(new BlockChainBean());

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_asset_list, null);
        RecyclerView recyc_left = inflate.findViewById(R.id.recyc_left);
        RecyclerView recyc_right = inflate.findViewById(R.id.recyc_right);
        ImageView exchange_record = inflate.findViewById(R.id.exchange_record);
        ImageView wallet_guanli = inflate.findViewById(R.id.wallet_guanli);
        ImageView add_wallet = inflate.findViewById(R.id.add_wallet);

        recyc_left.setLayoutManager(new LinearLayoutManager(mContext));
        recyc_left.setAdapter(new ManageWalletLeftAdapter(mContext,blockChainBeans));

        List<UserWalletBean> userWalletBeans = dao.selectList(new UserWalletBean());

        WalletManagementListAdapter walletManagementListAdapter = new WalletManagementListAdapter(Constants.getWalletName());
        walletManagementListAdapter.setNewInstance(userWalletBeans);
        recyc_right.setLayoutManager(new LinearLayoutManager(mContext));
        recyc_right.setAdapter(walletManagementListAdapter);

        PopupWindow popupWindow = new PopupWindow(inflate);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager windowManager = getActivity().getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        int height = defaultDisplay.getHeight();
        int height1 = getView().getHeight();
        int i = height / 3;
        int i1 = i * 2;
        popupWindow.setHeight(i1);
        popupWindow.showAsDropDown(getView(), Gravity.CENTER, 0, 0);


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
                SpUtil.getInstance(mContext).saveString("this_wallet_name", userWalletBeans.get(position).getName());
                walletManagementListAdapter.upDate(userWalletBeans.get(position).getName());
                initDate();
                popupWindow.dismiss();
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

        add_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddWalletActivity.class));
            }
        });

        exchange_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        wallet_guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }// TODO: 2021/1/11 进入钱包管理
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
                    dao.save(new UserWalletBean().setAddress(userWalletBean.getAddress()).setName(SpUtil.getInstance(mContext).getString("this_wallet_name", "")).setToken(is_login));
                } else {
                    dao.updata(select.setAddress(userWalletBean.getAddress()).setName(SpUtil.getInstance(mContext).getString("this_wallet_name", "")).setToken(is_login));
                }
//                if (StringUtils.isNotEmpty(bindState) && getString(R.string.string_zhu_or_si_null).equals(bindState)) {
//                    startActivity(new Intent(getContext(), MemberActivity.class));
//                } else {
                Constants.saveUserInfo(is_login, user_invitation, user_phone, user_id, user_name, is_activate);
//                    startActivity(new Intent(getContext(), MainActivity.class));
//                }
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
//                overridePendingTransition(0, 0);
                return Message.ok();
            }
        });
    }
}
