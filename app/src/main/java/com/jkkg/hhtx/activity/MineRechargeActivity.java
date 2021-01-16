package com.jkkg.hhtx.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;
import com.king.zxing.util.CodeUtils;
import com.mugui.base.base.Autowired;

import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/11/2020 10:56
 * Email:chencw0715@163.com
 * 充币
 *
 * @author admin6
 */
public class MineRechargeActivity extends BaseActivity {

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
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.img_qr_code1)
    ImageView imgQrCode1;
    @BindView(R.id.recharge_address1)
    TextView rechargeAddress1;
    @BindView(R.id.btn_copy1)
    ImageView btnCopy1;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_mine_recharge;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_charge_money));
//        request();
        List<CoinBean> coinBeans = dao.selectList(new CoinBean());
        requestAddresses(0);
        setTab(coinBeans);
    }

    private void setTab(List<CoinBean> coinBeans) {
        for (int i = 0; i < coinBeans.size(); i++) {
            mTabEntities.add(new TabEntity(coinBeans.get(i).getSymbol(), 0, 0));
        }
        commontabLayout.setTabData(mTabEntities);

        if (coinBeans.size() > 0) {
            requestAddresses(0);
        }
        commontabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                requestAddresses(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Autowired
    private Dao dao;

    private void requestAddresses(int postion) {
        Wallet mugui = WalletManager.getWallet(Constants.getWalletName());
        Bitmap qrCode = CodeUtils.createQRCode(mugui.getAddress(), ScreenSizeUtil.dp2px(getContext(), 100));
        imgQrCode1.setImageBitmap(qrCode);
        rechargeAddress1.setText(mugui.getAddress());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_copy1)
    public void onViewClicked() {
        copyData2Clipboard(rechargeAddress1.getText().toString());
    }
}
