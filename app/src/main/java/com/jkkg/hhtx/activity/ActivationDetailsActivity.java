package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.mugui.base.bean.user.UserAssociation;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 激活记录详情
 */
public class ActivationDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.dilatationDetail_uid_line)
    View dilatationDetailUidLine;
    @BindView(R.id.bizhong)
    TextView bizhong;
    @BindView(R.id.dilatationDetail_level_line)
    View dilatationDetailLevelLine;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.dilatationDetail_coin_line)
    View dilatationDetailCoinLine;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.dilatationDetail_sy_line)
    View dilatationDetailSyLine;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.dilatationDetail_all_line)
    View dilatationDetailAllLine;
    @BindView(R.id.hash)
    TextView hash;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_activation_details;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();

        UserAssociation list = (UserAssociation) intent.getSerializableExtra("list");

        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_jilu_detail);
/*
        number.setText(list.getNum().stripTrailingZeros().toPlainString());
        bizhong.setText(list.getSymbol());
        code.setText(list.getInvite_code());
        time.setText(DateFormatUtils.format(list.getTime(), "yyyy-MM-dd HH:mm:ss"));
        hash.setText(list.getHash());
        type.setText("已完成");*/
    }

}
