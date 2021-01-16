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
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.MineFeedbackAdapter;
import com.jkkg.hhtx.adapter.MineNoticeAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.net.bean.FeedbackBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.PageUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jnr.ffi.annotations.In;

/**
 * 留言列表
 */
public class MineFeedbackListActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;

    private PageUtils pageUtils;
    private View emptyView;
    private MineFeedbackAdapter mineFeedbackAdapter;
    private List<FeedbackBean> myTramListBeans;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_feedback_list;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_feedback_list));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText(getString(R.string.string_right_feedback));
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineFeedbackListActivity.this,MineFeedbackActivity.class));
            }
        });

        pageUtils = new PageUtils(noticeSmart);
        mineFeedbackAdapter = new MineFeedbackAdapter();
        emptyView= LayoutInflater.from(getContext()).inflate(R.layout.item_empty, (ViewGroup) noticeRec.getParent(), false);
        mineFeedbackAdapter.setEmptyView(emptyView);
        mineFeedbackAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(MineFeedbackListActivity.this, MineFeedbackDetailActivity.class);

                intent.putExtra("data",myTramListBeans.get(position));
                startActivity(intent);
            }
        });
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(mineFeedbackAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
                request();
            }
        });
        showLoading();
        request();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        request();
    }

    private void request(){
        Map<String,String> map=new ArrayMap<>();
        map.put("pageNum",pageUtils.getPagNumber()+"");
        map.put("pageSize",pageUtils.getPagSize()+"");
        MyApp.requestSend.sendData("feedback.method.list", GsonUtil.toJsonString(map)).main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                hideLoading();
                String data=message.getDate().toString();
                myTramListBeans = JSONArray.parseArray(data,FeedbackBean.class);
                if (myTramListBeans.size()>0) {
                    if (pageUtils.isRefreshType()) {
                        //刷新
                        mineFeedbackAdapter.setNewInstance(myTramListBeans);
                    }else{
                        //加载 更多
                        mineFeedbackAdapter.addData(myTramListBeans);
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
