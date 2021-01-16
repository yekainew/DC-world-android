package com.jkkg.hhtx.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.HelpBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的-关于我们
 */
public class MineAboutUserActivity extends BaseActivity {

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
    @BindView(R.id.banben_code)
    TextView banbenCode;
    @BindView(R.id.mine_about_tab1)
    TextView mineAboutTab1;
    @BindView(R.id.mine_about_tab2)
    TextView mineAboutTab2;
    @BindView(R.id.mine_about_tab3)
    TextView mineAboutTab3;
    @BindView(R.id.mine_about_tab4)
    TextView mineAboutTab4;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_about_user;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("关于我们");
        String appVersionName = getAppVersionName(getContext());
        banbenCode.setText("版本号:"+appVersionName);
    }

    @OnClick({R.id.mine_about_tab1, R.id.mine_about_tab2, R.id.mine_about_tab3, R.id.mine_about_tab4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_about_tab1:
                help();
                break;
            case R.id.mine_about_tab2:
                break;
            case R.id.mine_about_tab3:
                break;
            case R.id.mine_about_tab4:
                break;
        }
    }

    private List<HelpBean> o;
    private void help() {
        MyApp.requestSend.sendData("app.info.help").main(new NetCall.Call() {




            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                o = gson.fromJson(message.getDate().toString(), new TypeToken<List<HelpBean>>() {
                }.getType());

                forListType("about_us","关于我们");

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    public void forListType(String key,String title){
        for (HelpBean helpBean : o) {
            if (helpBean.getSystem_extra().equals(key)) {
                Intent intent = new Intent(getContext(), HelpWebActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("url",helpBean.getSystem_value());
                startActivity(intent);
            }
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
