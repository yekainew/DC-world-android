package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.AssetsDetailAdapter;
import com.jkkg.hhtx.app.GlideApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.fragment.AssetsDetailFragment;
import com.jkkg.hhtx.fragment.AssetsDetailFromFragment;
import com.jkkg.hhtx.fragment.AssetsDetailToFragment;
import com.jkkg.hhtx.net.bean.OtcWalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;
import com.mugui.base.base.Autowired;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的资产详情
 * 点进去的详情  AssetTransactionDetailsActivity
 *
 * @author admin6
 */
public class AssetDetailActivity extends BaseActivity {

    @BindView(R.id.asset_name)
    TextView assetName;
    @BindView(R.id.asset_usable)
    TextView assetUsable;
    @BindView(R.id.asset_freeze)
    TextView assetFreeze;/*
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;*/
    @BindView(R.id.asset_equivalent)
    TextView assetEquivalent;
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.btn_zz)
    Button btnZz;
    @BindView(R.id.btn_sk)
    Button btnSk;
    @BindView(R.id.bi_img)
    ImageView bi_img;
    //    private PageUtils pageUtils;
    private OtcWalletBean.ListBean assetdetail;
    private AssetsDetailAdapter assetsDetailAdapter;
    private final int[] mTitles = {
            R.string.string_all, R.string.string_from, R.string.string_to
    };
    @Autowired
    Dao dao;
    int pageNum = 1;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_asset_detail;
    }

    @Autowired
    Block block;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        assetdetail = (OtcWalletBean.ListBean) intent.getSerializableExtra("assetdetail");
        assetName.setText(assetdetail.getCurrency_name());
        assetUsable.setText(assetdetail.getUsable() + "");
        assetFreeze.setText(assetdetail.getFrozen() + "");
        assetEquivalent.setText(assetdetail.getRMB() + "");
//        GlideApp.with(getContext()).load(assetdetail.getFrozen())
        List<CoinBean> coinBeans = dao.selectList(new CoinBean());

        for (CoinBean coinBean : coinBeans) {
            if (assetdetail.getCurrency_name().equals(coinBean.getSymbol())) {

                String icon_url = coinBean.getIcon_url();

                GlideApp.with(getContext()).load(icon_url).into(bi_img);
            }

        }
        setTab();
    }

    private Fragment instantiateFragment(ViewPager2 viewPager, int position, Fragment defaultResult) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private void setTab() {
        mFragments.add(instantiateFragment(viewPager, 1, AssetsDetailFragment.newInstance(assetdetail.getCurrency_name())));
        mFragments.add(instantiateFragment(viewPager, 2, AssetsDetailToFragment.newInstance(assetdetail.getCurrency_name())));
        mFragments.add(instantiateFragment(viewPager, 3, AssetsDetailFromFragment.newInstance(assetdetail.getCurrency_name())));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(getString(mTitles[i]), 0, 0));
        }
        commontabLayout.setTabData(mTabEntities);
        if (mTabEntities.size() > 0) {
            //默认情况会选择第一个
            for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) commontabLayout.getTabView(i).getLayoutParams();
                layoutParams.leftMargin = ScreenSizeUtil.dp2px(getContext(), 5);
                layoutParams.rightMargin = ScreenSizeUtil.dp2px(getContext(), 5);
                commontabLayout.getTabView(i).setLayoutParams(layoutParams);
                if (0 == i) {
                    //选中的Tab
                    commontabLayout.getTabView(i).setBackgroundResource(R.mipmap.new_icon_book_chick);
                } else {
                    //未选中的Tab
                    commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_unselect);
                }
            }
        }

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return mFragments.size();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments.get(position);
            }
        });

        commontabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                    viewPager.setCurrentItem(position, true);
                    if (position == i) {
                        //选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.mipmap.new_icon_book_chick);
                    } else {
                        //未选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_unselect);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                commontabLayout.setCurrentTab(position);
                for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                    if (position == i) {
                        //选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.mipmap.new_icon_book_chick);
                    } else {
                        //未选中的Tab
                        commontabLayout.getTabView(i).setBackgroundResource(R.drawable.bg_tab_corner_unselect);
                    }
                }
            }
        });
        viewPager.setUserInputEnabled(true);//可以滑动
        viewPager.setOffscreenPageLimit(3);
    }

    Intent intent;

    @OnClick({R.id.btn_zz, R.id.btn_sk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_zz:
                //转账
                intent = new Intent(this, MineTransferActivity.class);
                intent.putExtra("btc", assetdetail.getCurrency_name());
                startActivity(intent);
                break;
            case R.id.btn_sk:
                //收款
                intent = new Intent(this, MineRechargeActivity.class);
                startActivity(intent);
                break;
        }
    }
}