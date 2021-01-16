package com.jkkg.hhtx.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.net.bean.UserBindAddressBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;
import com.king.zxing.CaptureActivity;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Description:
 * Created by ccw on 09/11/2020 10:57
 * Email:chencw0715@163.com
 * 提币
 */
public class MineExtractActivity extends BaseActivity {

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


    private final String[] mTitles = {
            "DC", "USDT"
    };
    @BindView(R.id.extract_phone1)
    ClearEditText extractPhone1;
    @BindView(R.id.extract_cramar)
    ImageView extractCramar;
    @BindView(R.id.extract_num1)
    ClearEditText extractNum1;
    @BindView(R.id.transfer_yue1)
    TextView transferYue1;
    @BindView(R.id.extract_phone_code1)
    ClearEditText extractPhoneCode1;
    @BindView(R.id.send_code1)
    TextView sendCode1;
    @BindView(R.id.transfer_fee1)
    TextView transferFee1;
    @BindView(R.id.transfer_min_num1)
    TextView transferMinNum1;
    @BindView(R.id.extract_true1)
    Button extractTrue1;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    @Autowired
    public Dao dao;
    @Autowired
    public Block block;
    String tabname = "";

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_extract;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_mention_money));
        List<CoinBean> coinBeans = dao.selectList(new CoinBean());
        setTab(coinBeans);

    }

    private void setTab(List<CoinBean> coinBeans) {

        for (int i = 0; i < coinBeans.size(); i++) {
            mTabEntities.add(new TabEntity(coinBeans.get(i).getSymbol(), 0, 0));
        }
        commontabLayout.setTabData(mTabEntities);
        if (mTabEntities.size() > 0) {
            //默认情况会选择第一个
            selectYue(coinBeans.get(0).getSymbol());
            for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) commontabLayout.getTabView(i).getLayoutParams();
                layoutParams.leftMargin = ScreenSizeUtil.dp2px(this, 5);
                layoutParams.rightMargin = ScreenSizeUtil.dp2px(this, 5);
                commontabLayout.getTabView(i).setLayoutParams(layoutParams);
                if (0 == i) {
                    //选中的Tab
                    commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_select);
                    tabname = coinBeans.get(i).getSymbol();
                } else {
                    //未选中的Tab
                    commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_unselect);
                }
            }
        }

        commontabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                tabname = coinBeans.get(position).getSymbol();

                for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                    if (position == i) {
                        //选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_select);

                    } else {
                        //未选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_unselect);
                    }

                    selectYue(coinBeans.get(position).getSymbol());
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    Disposable disposable;

    @OnClick({R.id.extract_cramar, R.id.extract_true1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.extract_cramar:
                RxPermissions rxPermissions = new RxPermissions(MineExtractActivity.this);
                disposable = rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_failed_to_get_permission));
                                } else {
                                    Intent intent = new Intent(MineExtractActivity.this, ZxingActivity.class);
                                    intent.setAction("2");
                                    startActivityForResult(intent, 303);
                                }
                            }
                        });
                break;
            case R.id.extract_true1:
                requestTransfer();
                break;
        }
    }

    public void selectYue(String s) {
        AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(s));
        BigDecimal num_usd = select.getNum();

        transferYue1.setText(s + getString(R.string.string_moneyyue) + num_usd + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 303 && resultCode == RESULT_OK) {
            if (data != null) {
                String result = data.getStringExtra("result");
                if (result != null) {
                    extractPhone1.setText(result);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void requestTransfer() {
        showLoading();
        String phone = extractPhone1.getText().toString();
        String s = extractPhoneCode1.getText().toString();
        WalletBean wallet = block.getWallet(SpUtil.getInstance(MineExtractActivity.this).getString("this_wallet_name",""),s);
        if (wallet == null || StringUtils.isBlank(wallet.getPri())) {
            showMessage(getString(R.string.string_password_fail));
            //提示钱包密码错误
            hideLoading();
            return;
        }
        //TODO 需填入币种名称
        CoinBean coin = dao.select(new CoinBean().setSymbol(tabname));
        AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(coin.getSymbol()));
        if (!Other.isDouble(extractNum1.getText().toString())) {
            //提示金额错误
            showMessage(getString(R.string.string_money_fail));
            hideLoading();
            return;
        }
        BigDecimal bigDecimal = new BigDecimal(extractNum1.getText().toString());
        if (bigDecimal.compareTo(select.getNum()) > 0) {
            //提示金额错误
            showMessage(getString(R.string.string_money_fail));
            hideLoading();
            return;
        }

        ThreadUtil.execAsync(new Runnable() {
            @Override
            public void run() {
                Message tron = block.tron(phone, coin.getContract_address(), bigDecimal, SpUtil.getInstance(MineExtractActivity.this).getString("this_wallet_name",""), s);
                if (tron.getType() == Message.SUCCESS) {
                    showMessage(getString(R.string.string_tibi_success));
                    //转账成功
                    hideLoading();
                } else {
                    showMessage(tron.getMsg());
                    //失败提醒错误原因
                    hideLoading();
                }
            }
        });


    }
}
