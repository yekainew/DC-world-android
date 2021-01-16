package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.RewardLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.NodeEmancipationBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 奖励记录
 */
public class RewardListActivity extends BaseActivity {

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
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    int type=1;//默认认购奖励
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_reward_list;


    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.getAction().equals("1")) {
            type=1;//认购
            rewardsRecord();
        }else if (intent.getAction().equals("2")){
            type=2;//社区
            rewardsRecord();
        }else if (intent.getAction().equals("3")){

        }else{

        }


        rewardsRecord();
    }

    @Autowired
    Dao dao;

    private void rewardsRecord() {
        Map<String,String> map=new HashMap<String, String>(){
            {
                put("type",type+"");
            }
        };
        MyApp.requestSend.sendData("emlog.app.method.rewardsRecord",map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<NodeEmancipationBean> nodeEmancipationBeans = JSONArray.parseArray(message.getDate().toString(), NodeEmancipationBean.class);
                RewardLogAdapter rewardLogAdapter = new RewardLogAdapter();
                rewardLogAdapter.setContext(RewardListActivity.this).setNodeEmancipationBeans(nodeEmancipationBeans);
                recyclerview.setLayoutManager(new LinearLayoutManager(RewardListActivity.this));
                recyclerview.setAdapter(rewardLogAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }



}
