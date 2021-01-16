package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 存币  选择
 */
public class CunbiActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cunbi)
    LinearLayout cunbi;
    @BindView(R.id.licai)
    LinearLayout licai;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_cunbi;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_cunbi));
        toolbarImageLeft.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.cunbi, R.id.licai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cunbi:
                startActivity(new Intent(CunbiActivity.this, ManageLockActivity.class));
                break;
            case R.id.licai:
                startActivity(new Intent(this, ManageFinancesActivity.class));
                break;
            default:
                break;
        }
    }
}
