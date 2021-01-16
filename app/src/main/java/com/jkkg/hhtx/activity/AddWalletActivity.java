package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加钱包
 */
public class AddWalletActivity extends BaseActivity {

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
    @BindView(R.id.add_wallet_tab1)
    TextView addWalletTab1;
    @BindView(R.id.add_wallet_tab2)
    TextView addWalletTab2;
    @BindView(R.id.add_wallet_tab3)
    TextView addWalletTab3;
    @BindView(R.id.add_wallet_tab4)
    TextView addWalletTab4;
    @BindView(R.id.add_wallet_tab5)
    TextView addWalletTab5;
    @BindView(R.id.add_wallet_tab6)
    TextView addWalletTab6;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_add_wallet;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_add_wallet);
    }


    @OnClick({R.id.add_wallet_tab1, R.id.add_wallet_tab2, R.id.add_wallet_tab3, R.id.add_wallet_tab4, R.id.add_wallet_tab5, R.id.add_wallet_tab6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_wallet_tab1:
                //普通创建
                startActivity(new Intent(this,CreateUserActivity.class));
                break;
            case R.id.add_wallet_tab2:
                //冷钱包创建（暂时不需要，已隐藏）
                showMessage("暂未开放");
                break;
            case R.id.add_wallet_tab3:
                //助记词导入
                startActivity(new Intent(getContext(),RecoverZhuJiActivity.class).setAction("1"));
                break;
            case R.id.add_wallet_tab4:
                //私钥导入
                startActivity(new Intent(getContext(),RecoverSiActivity.class).setAction("1"));
                break;
            case R.id.add_wallet_tab5:
                showMessage("暂未开放");
                //观察钱包导入
                break;
            case R.id.add_wallet_tab6:
                showMessage("暂未开放");
                //冷钱包导入
                break;
        }
    }
}
