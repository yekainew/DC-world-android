package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.net.bean.NewListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 系统公告详情页
 */
public class MineSystemNoticeDetailActivity extends BaseActivity {

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
    @BindView(R.id.notice_title)
    TextView noticeTitle;
    @BindView(R.id.notice_time)
    TextView noticeTime;
    @BindView(R.id.notice_text)
    TextView noticeText;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    AnnouncementBean announcementBean;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_system_notice_detail;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_notice_detail));
        announcementBean = (AnnouncementBean)getIntent().getSerializableExtra("data");
        noticeTitle.setText(announcementBean.getTitle());
        noticeText.setText(announcementBean.getContent());
        noticeTime.setText(DateFormatUtils.format(announcementBean.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));

    }


}
