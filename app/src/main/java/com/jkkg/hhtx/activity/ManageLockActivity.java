package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.FinancesListAdapter;
import com.jkkg.hhtx.adapter.LockListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.FinancesListBean;
import com.jkkg.hhtx.net.bean.MyFinancesListBean;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 锁币生息
 */
public class ManageLockActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_earnings_num)
    TextView addEarningsNum;
    @BindView(R.id.yesterday_earnings_num)
    TextView yesterdayEarningsNum;

    @BindView(R.id.finances_layout)
    CommonTabLayout finances_layout;

    @BindView(R.id.finances_list)
    RecyclerView finances_list; //投资列表

    @BindView(R.id.my_finances_list)
    RecyclerView my_finances_list;//我的投资
    @BindView(R.id.yield)
    TextView yield;

    private LockListAdapter financesListAdapter;

    private List<FinancesListBean> o;
    private List<MyFinancesListBean> o1;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_lock;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_cunbisx));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText(getString(R.string.string_rule));

        financesListAdapter = new LockListAdapter(new JSONObject(),new ArrayList<>(), this);
        finances_list.setLayoutManager(new LinearLayoutManager(this));
        finances_list.setAdapter(financesListAdapter);
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageLockActivity.this, GuiZeActivity.class));
            }
        });

        financesListAdapter.setOnClickListener(new LockListAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position) {
                String s = list.get(position);
                JSONArray jsonArray = jsonObject.getJSONArray(s);
                Intent intent = new Intent(ManageLockActivity.this, LockupActivity.class);
                intent.putExtra("financesListBean",jsonArray);
                startActivity(intent);
            }
        });
        lockcoin();

    }
    private JSONObject jsonObject;
    List<String> list=new ArrayList<>();
    private void lockcoin() {
        MyApp.requestSend.sendData("lockcoin.app.method.list").main(new NetCall.Call() {

            @Override
            public Message ok(Message message) {
                jsonObject = JSONObject.parseObject(message.getDate().toString());
                Set<String> strings = jsonObject.keySet();
                for (String string : strings) {
                    list.add(string);
                }
                financesListAdapter.setData(jsonObject,list);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    @OnClick(R.id.yield)
    public void onViewClicked() {
        //收益列表
    }
}
