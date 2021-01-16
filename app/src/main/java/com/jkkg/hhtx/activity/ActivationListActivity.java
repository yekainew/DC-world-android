package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.InvateLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.UserAssociation;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 激活列表
 * 条目 item_activation_list
 * 条目详情 ActivationDetailsActivity
 */
public class ActivationListActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_activation_list;
    }

    private Handler handler=new Handler();


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_jihuo_list));
        getList();
        handler.postDelayed(runnable,2000);
    }
    Runnable runnable=  new Runnable() {
        @Override
        public void run() {
            getList();
            handler.postDelayed(runnable,2000);
        }
    };
    @Autowired
    Dao dao;

    private void getList() {

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNum","1");
        jsonObject.put("pageSize","2000");
        MyApp.requestSend.sendData("invite.method.invaleteLog",jsonObject).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<UserAssociation> userAssociations = JSONArray.parseArray(message.getDate().toString(), UserAssociation.class);
                InvateLogAdapter invateLogAdapter = new InvateLogAdapter();
                invateLogAdapter.setNewInstance(userAssociations);
                noticeRec.setLayoutManager(new LinearLayoutManager(getContext()));
                noticeRec.setAdapter(invateLogAdapter);

               /* invateLogAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        Intent intent = new Intent(ActivationListActivity.this, ActivationDetailsActivity.class);
                        intent.putExtra("list",userAssociations.get(position));
                        startActivity(intent);
                    }
                });*/
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
