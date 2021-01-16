package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.fragment.AssetsFragment;
import com.jkkg.hhtx.fragment.DealFragment;
import com.jkkg.hhtx.fragment.HomeFragment;
import com.jkkg.hhtx.fragment.InformationFragment;
import com.jkkg.hhtx.fragment.MineFragment;
import com.jkkg.hhtx.fragment.NewPoolFragment;
import com.jkkg.hhtx.fragment.PoolFragment;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.ToastUtil;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;
import com.luck.picture.lib.tools.ScreenUtils;
import com.wynsbin.vciv.DensityUtils;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 主页
 */
public  class MainActivity extends BaseActivity {
    @BindView(R.id.main_vp)
    ViewPager2 mainVp;
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;

    private int[] mTitles = { R.string.string_assets, R.string.string_tab_shichang, R.string.string_tab_faxian, R.string.string_home_tab1,R.string.string_tab_guanli};
    private int[] mIconUnselectIds = {
            R.mipmap.new_icon1, R.mipmap.new_icon2, R.mipmap.new_icon3, R.mipmap.new_icon4, R.mipmap.new_icon5};
    private int[] mIconSelectIds = {
            R.mipmap.new_icon1_show, R.mipmap.new_icon2_show, R.mipmap.new_icon3_show, R.mipmap.new_icon4_show, R.mipmap.new_icon5_show};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    HomeFragment homeFragment;
    DealFragment dealFragment;
    PoolFragment poolFragment;
    NewPoolFragment newPoolFragment;
    AssetsFragment assetsFragment;
    MineFragment mineFragment;
    InformationFragment informationFragment;
    private double firstTime = 0;
    private long mExitTime;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    private Fragment instantiateFragment(ViewPager2 viewPager, int position, Fragment defaultResult) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(getString(mTitles[i]), mIconSelectIds[i], mIconUnselectIds[i]));
        }
        homeFragment=new HomeFragment();
        dealFragment=new DealFragment();
        poolFragment=new PoolFragment();
        assetsFragment=new AssetsFragment();
        mineFragment=new MineFragment();
        newPoolFragment = new NewPoolFragment();
        informationFragment = new InformationFragment();
        mFragments.add(instantiateFragment(mainVp, 0, assetsFragment));
        mFragments.add(instantiateFragment(mainVp, 1, dealFragment));
        mFragments.add(instantiateFragment(mainVp, 2, homeFragment));
        mFragments.add(instantiateFragment(mainVp, 3, informationFragment));
        mFragments.add(instantiateFragment(mainVp, 4, mineFragment));
        mainVp.setAdapter(new FragmentStateAdapter(this) {
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
        commontabLayout.setTabData(mTabEntities);

/**
 *          放大中间tab图标
 *          ImageView imageView=commontabLayout.getIconView(2);
 *          LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) imageView.getLayoutParams();
 *          layoutParams.width= ScreenUtils.dip2px(this,45);
 *          layoutParams.height= ScreenUtils.dip2px(this,45);
 *          imageView.setLayoutParams(layoutParams);
 *          RelativeLayout tablayout= (RelativeLayout) commontabLayout.getTabView(2);
 *          LinearLayout.LayoutParams tablayoutLayoutParams= (LinearLayout.LayoutParams) tablayout.getLayoutParams();
 *          tablayoutLayoutParams.topMargin= -DensityUtils.dp2px(this,20);
 *          tablayout.setLayoutParams(tablayoutLayoutParams);
 */

        commontabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                boolean islogin = SpUtil.getInstance(MainActivity.this).getBoolean("islogin", false);
//                if (!islogin) {
//                    if (position==2) {
//                        commontabLayout.setCurrentTab(0);
//                        mainVp.setCurrentItem(0, false);
//                        startActivity(new Intent(MainActivity.this,MemberActivity.class));
//                    }else{
//                        mainVp.setCurrentItem(position, false);
//                    }
//                }else{
//                    mainVp.setCurrentItem(position, false);
//                }
                mainVp.setCurrentItem(position, false);
                Log.d("TAGMain","走到这里了");

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mainVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                boolean islogin = SpUtil.getInstance(MainActivity.this).getBoolean("islogin", false);
//                if (!islogin) {
//                    if (position==2) {
//                        commontabLayout.setCurrentTab(0);
//                    }else{
//                        commontabLayout.setCurrentTab(position);
//                    }
//                }else{
//                    commontabLayout.setCurrentTab(position);
//                }
                commontabLayout.setCurrentTab(position);
                Log.d("TAGMain","走到这里了2");

            }
        });
        mainVp.setUserInputEnabled(false);//禁止滑动
        mainVp.setOffscreenPageLimit(1);

    }

    public ImageView getIconView(int tab){
       return null;
    }
    public void setDotNum(int num) {
        if (commontabLayout != null) {
            if (num > 0) {
                commontabLayout.showDot(2);
            } else {
                commontabLayout.hideMsg(2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtil.show(getString(R.string.string_tip_text_back));
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().killAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isgoto(EventImpl.goToOne goToOne){
        mainVp.setCurrentItem(0, false);
        commontabLayout.setCurrentTab(0);
    }
}
