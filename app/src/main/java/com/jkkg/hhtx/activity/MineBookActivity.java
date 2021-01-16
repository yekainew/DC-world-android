package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.fragment.BookFragment;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import butterknife.BindView;

/**
 * 我的账本
 */
public class MineBookActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;

    private final int[] mTitles = {
            R.string.string_mine_charge_money, R.string.string_mine_transfer
    };
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    BookFragment bookFragment;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_book;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_common4));
        setTab();
    }

    private Fragment instantiateFragment(ViewPager2 viewPager, int position, Fragment defaultResult) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    private void setTab() {

        mFragments.add(instantiateFragment(viewPager, 0, BookFragment.newInstance(0)));
//        mFragments.add(instantiateFragment(viewPager, 1, BookFragment.newInstance(1)));
        mFragments.add(instantiateFragment(viewPager, 1, BookFragment.newInstance(2)));
//        mFragments.add(instantiateFragment(viewPager, 2, BookFragment.newInstance(3)));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(getString(mTitles[i]), 0, 0));
        }
        commontabLayout.setTabData(mTabEntities);
        if (mTabEntities.size() > 0) {
            //默认情况会选择第一个
            for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) commontabLayout.getTabView(i).getLayoutParams();
                layoutParams.leftMargin = ScreenSizeUtil.dp2px(this, 5);
                layoutParams.rightMargin = ScreenSizeUtil.dp2px(this, 5);
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
                viewPager.setCurrentItem(position, true);

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
}