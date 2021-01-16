package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.MineTeamAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.MyTramListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.PageUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 我的团队
 *
 * @author admin6
 */
public class MineTeamActivity extends BaseActivity {
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
    @BindView(R.id.team_rec)
    RecyclerView teamRec;
    @BindView(R.id.team_smart)
    SmartRefreshLayout teamSmart;
    private PageUtils pageUtils;
    private MineTeamAdapter mineTeamAdapter;

    int page = 1;
    int num = 50;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_team;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_team));

        pageUtils = new PageUtils(teamSmart);
        mineTeamAdapter = new MineTeamAdapter(new ArrayList<>(), this);
        teamRec.setLayoutManager(new LinearLayoutManager(this));
        teamRec.setAdapter(mineTeamAdapter);
        request();

        teamSmart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //加载
                page++;
                request();
                teamSmart.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
                page = 1;
                request();
                teamSmart.finishRefresh();
            }
        });


    }

    private void request() {

        Map<String, String> map = new ArrayMap<>();
        map.put("pageNum", page + "");
        map.put("pageSize", num + "");
        MyApp.requestSend.sendData("holdarea.method.users", GsonUtil.toJsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                String data = message.getDate().toString();

                List<MyTramListBean> myTramListBeans = JSONArray.parseArray(data, MyTramListBean.class);

                Log.d("MineTeamActivity", "myTramListBeans.size():" + myTramListBeans.size());
                if (page == 1) {
                    //刷新

                    if (myTramListBeans.size() > 0) {
                        mineTeamAdapter.setData(myTramListBeans);
                    } else {
                        setContentView(R.layout.item_empty_data);
                    }
                } else {
                    //加载 更多
                    if (myTramListBeans.size() > 0) {
                        mineTeamAdapter.upData(myTramListBeans);
                    }
                }

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
}