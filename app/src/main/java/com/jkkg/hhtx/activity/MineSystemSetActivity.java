package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;

import butterknife.BindView;

/**
 * 系统设置
 */
public class MineSystemSetActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mine_xitong_tab)
    TextView mineXitongTab;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_system_set;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_xitong_set));

        mineXitongTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MineSystemSetActivity.this,NodeSettingsActivity.class));
            }
        });
    }

}