package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/15/2020 11:41
 * Email:chencw0715@163.com
 * 私钥备份
 */
public  class CreateSelectSiActivity extends BaseActivity {

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
    @BindView(R.id.text_si)
    TextView textSi;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_select_si;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_selectsi);
        toolbarImageLeft.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_true)
    public void onViewClicked() {
        //下一步
        startActivity(new Intent(this,CreateSelectSiAckActivity.class));
    }
}
