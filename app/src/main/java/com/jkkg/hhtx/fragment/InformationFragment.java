package com.jkkg.hhtx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.HomeItemAdapter;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;


/**
 * Description:
 * Created by ccw on 09/08/2020 15:22
 * Email:chencw0715@163.com
 * 资讯
 *
 * @author admin6
 */
public class InformationFragment extends BaseFragment {


    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    /*    @BindView(R.id.tv_fragment1)
        TextView tvFragment1;
        @BindView(R.id.tv_fragment2)
        TextView tvFragment2;
        @BindView(R.id.fragment)
        FrameLayout fragment;*/
    private FragmentManager fm = null;
    private FragmentTransaction transaction = null;

    private DealExchangeFragment dealExchangeFragment;
    private DealCapitalpoolFragment dealCapitalpoolFragment;
    private HomeItemAdapter homeItemAdapter;

    private final int[] mTitles = {
            R.string.string_home_tab1, R.string.string_new, R.string.string_liaotian
    };


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    private Fragment instantiateFragment(ViewPager2 viewPager, int position, Fragment defaultResult) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }
    private void setTab() {

        mFragments.add(instantiateFragment(viewPager, 0, NewListFragment.newInstance()));
//        mFragments.add(instantiateFragment(viewPager, 1, BookFragment.newInstance(1)));
        mFragments.add(instantiateFragment(viewPager, 1, NewYearFragment.newInstance()));
        mFragments.add(instantiateFragment(viewPager, 2, MessageFragment.newInstance()));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(getString(mTitles[i]), 0, 0));
        }
        commontabLayout.setTabData(mTabEntities);
        if (mTabEntities.size() > 0) {
            //默认情况会选择第一个
            for (int i = 0; i < commontabLayout.getTabCount(); i++) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) commontabLayout.getTabView(i).getLayoutParams();
                layoutParams.leftMargin = ScreenSizeUtil.dp2px(mContext, 5);
                layoutParams.rightMargin = ScreenSizeUtil.dp2px(mContext, 5);
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

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        setTab();
    }




}
