package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * newpool
 * 提取记录
 * item item_newpool_jilu提取记录
 * 状态 1进行中 2成功 3失败
 */
public class NewPoolTiquJiluActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartref)
    SmartRefreshLayout smartref;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_tiqu_jilu;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}