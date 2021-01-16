package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.SelectBcNameAdapter;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.mugui.base.base.Autowired;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择币种
 * 默认查询的是自己拥有那种币种
 * 自己拥有的币种是根据自己是否持有这个币种的数量来定
 * 也就是说只要你有某种币，那这个币种就会显示出来
 */
public class SelectBcNameActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.bc_name_rec)
    RecyclerView bcNameRec;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_select_bc_name;
    }

    @Autowired
    Dao dao;


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText("选择币种");
        Intent intent = getIntent();
        List<CoinBean> coinBeans = dao.selectList(new CoinBean());


        SelectBcNameAdapter selectBcNameAdapter = new SelectBcNameAdapter(this, coinBeans);
        bcNameRec.setLayoutManager(new LinearLayoutManager(this));
        bcNameRec.setAdapter(selectBcNameAdapter);

        selectBcNameAdapter.setOnClickListener(new SelectBcNameAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, String name) {
                intent.putExtra("name",name);
                setResult(303,intent);
                finish();
            }
        });
    }



}
