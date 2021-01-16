package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.FeedbackBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 我的留言详情
 */
public class MineFeedbackDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.feedback_title)
    TextView feedbackTitle;
    @BindView(R.id.feedback_time)
    TextView feedbackTime;
    @BindView(R.id.feedback_text)
    TextView feedbackText;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.official_title)
    TextView officialTitle;
    @BindView(R.id.official_time)
    TextView officialTime;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_feedback_detail;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_feedback_detail));

        Intent intent = getIntent();
        FeedbackBean data = (FeedbackBean) intent.getSerializableExtra("data");
        feedbackText.setText(data.getContent());
        feedbackTime.setText(DateFormatUtils.format(data.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
        officialTitle.setText(getString(R.string.string_guanfang) + data.getReply());
        officialTime.setText(DateFormatUtils.format(data.getUpdate_time(), "yyyy-MM-dd HH:mm:ss"));

    }

}
