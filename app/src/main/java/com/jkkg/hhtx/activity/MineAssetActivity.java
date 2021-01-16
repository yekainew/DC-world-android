package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.AssetsAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.OtcWalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

/**
 * Description:
 * Created by ccw on 09/21/2020 14:39
 * Email:chencw0715@163.com
 * 我的资产
 */
public class MineAssetActivity extends BaseActivity {

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
    @BindView(R.id.usdt_num)
    TextView usdtNum;
    @BindView(R.id.rmb_num)
    TextView rmbNum;
    @BindView(R.id.assets_rec)
    RecyclerView assetsRec;
    AssetsAdapter assetsAdapter;

    @Autowired
    private Dao dao;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_asset;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_common1));

        BigDecimal all = BigDecimal.ZERO;
        BigDecimal all_cny = BigDecimal.ZERO;
        List<OtcWalletBean.ListBean> list = new LinkedList<>();
        for (CoinBean bean : dao.selectList(new CoinBean())) {
            AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(bean.getSymbol()));
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
        String s = all.stripTrailingZeros().toPlainString();
        usdtNum.setText(s + "");
        String s1 = all_cny.stripTrailingZeros().toPlainString();
        rmbNum.setText("≈￥" + s1);
//        assetsAdapter = new AssetsAdapter(getContext(), list);
        assetsRec.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRec.setAdapter(assetsAdapter);

       /* assetsAdapter.setOnClickListener(new AssetsAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position,) {
                //点击条目进详情
                Intent intent = new Intent(MineAssetActivity.this, AssetDetailActivity.class);
                intent.putExtra("assetdetail", list.get(position));
                startActivity(intent);
            }
        });*/
    }
}
