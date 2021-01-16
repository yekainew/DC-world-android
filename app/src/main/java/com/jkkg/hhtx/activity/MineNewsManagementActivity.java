package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.NewListAdapter;
import com.jkkg.hhtx.adapter.NewListManageAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.NewListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.PageUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.json.JSONUtil;

/**
 * 资讯管理
 */
public class MineNewsManagementActivity extends BaseActivity {

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
    NewListManageAdapter newListAdapter;
    private List<NewListBean> o;
    private PageUtils pageUtils;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_news_management;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_new_management));
        pageUtils = new PageUtils(noticeSmart);
        newListAdapter = new NewListManageAdapter(new ArrayList<>(), this);
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(newListAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
                newsList();
            }
        });

        newListAdapter.setOnClickListener(new NewListManageAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position) {
                if (o!=null) {
                    int information_id = o.get(position).getInformation_id();
                    deleteInformation(information_id,position);
                }
            }
        });

        newsList();

    }

    private void deleteInformation(int information_id,int p) {
        Map<String,String> map=new HashMap<String,String>(){
            {
                put("information_id",information_id+"");
            }
        };
        MyApp.requestSend.sendData("information.app.method.deleteInformation", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(getString(R.string.string_delete_success));
                o.remove(p);
                newListAdapter.setData(o);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void newsList() {
        Map<String,String> map=new ArrayMap<>();
        map.put("pageNum",pageUtils.getPagNumber()+"");
        map.put("pageSize",pageUtils.getPagSize()+"");

        MyApp.requestSend.sendData("information.app.method.personalInformation", GsonUtil.toJsonString(map)).main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                String s = message.getDate().toString();
                Gson gson = new Gson();

                o = gson.fromJson(s, new TypeToken<List<NewListBean>>() {
                }.getType());


                if (o.size()>0) {
                    if (pageUtils.isRefreshType()) {
                        newListAdapter.setData(o);
                    }else{
                        newListAdapter.upData(o);
                    }
                }
                return Message.ok();
            }
            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
