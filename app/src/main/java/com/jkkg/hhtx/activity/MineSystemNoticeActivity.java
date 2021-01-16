package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.MineNoticeAdapter;
import com.jkkg.hhtx.adapter.MineTeamAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.net.bean.MyTramListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.PageUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 系统公告
 *
 * @author admin6
 */
public class MineSystemNoticeActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_tv_left)
    TextView toolbarTvLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;

    private PageUtils pageUtils;
    private MineNoticeAdapter mineNoticeAdapter;
    private View emptyView;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_system_notice;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_syatem_notice));

        pageUtils = new PageUtils(noticeSmart);
        mineNoticeAdapter = new MineNoticeAdapter();
        emptyView= LayoutInflater.from(getContext()).inflate(R.layout.item_empty, (ViewGroup) noticeRec.getParent(), false);
        mineNoticeAdapter.setEmptyView(emptyView);
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(mineNoticeAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
                request();
            }
        });
        mineNoticeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AnnouncementBean announcementBean = (AnnouncementBean) adapter.getData().get(position);
                if (announcementBean != null) {
                    //公告详情
                    Intent intent = new Intent(MineSystemNoticeActivity.this, MineSystemNoticeDetailActivity.class);
                    intent.putExtra("data", announcementBean);
                    startActivity(intent);
                }
            }
        });

        showLoading();
        request();
    }
    private void request(){
        Map<String,String> map=new ArrayMap<>();
        map.put("pageNum",pageUtils.getPagNumber()+"");
        map.put("pageSize",pageUtils.getPagSize()+"");
        MyApp.requestSend.sendData("announcement.app.method.list", GsonUtil.toJsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data=message.getDate().toString();
                List<AnnouncementBean> myTramListBeans= JSONArray.parseArray(data,AnnouncementBean.class);
                if (myTramListBeans.size()>0) {
                    if (pageUtils.isRefreshType()) {
                        //刷新
                        mineNoticeAdapter.setNewInstance(myTramListBeans);
                    }else{
                        //加载 更多
                        mineNoticeAdapter.addData(myTramListBeans);
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