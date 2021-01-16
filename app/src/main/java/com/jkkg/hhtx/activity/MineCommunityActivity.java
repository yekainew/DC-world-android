package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.InviteUserCountBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的-社区
 */
public class MineCommunityActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.commontab_num)
    TextView commontabNum;
    @BindView(R.id.direct_num)
    TextView directNum;
    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.total_revenue)
    TextView totalRevenue;
    @BindView(R.id.ranking_earnings)
    TextView rankingEarnings;
    @BindView(R.id.promotion_earnings)
    TextView promotionEarnings;
    @BindView(R.id.community_earnings)
    TextView communityEarnings;
    @BindView(R.id.ranking_force)
    TextView rankingForce;
    @BindView(R.id.promotion_force)
    TextView promotionForce;
    @BindView(R.id.community_force)
    TextView communityForce;
    @BindView(R.id.community_linearlayout)
    LinearLayout communityLinearlayout;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_community;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_common3));
        request();
    }

    @OnClick(R.id.community_linearlayout)
    public void onViewClicked() {
        //我的团队
        startActivity(new Intent(this, MineTeamActivity.class));
    }

    private void request() {
        showLoading();
        MyApp.requestSend.sendData("holdarea.method.community").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                InviteUserCountBean inviteUserCountBean = InviteUserCountBean.newBean(InviteUserCountBean.class, message.getDate());
                directNum.setText(inviteUserCountBean.getPush_size() + "");
                commontabNum.setText(inviteUserCountBean.getTeam_size() + "");
                rank.setText((inviteUserCountBean.getRank()) + "");


                totalRevenue.setText(inviteUserCountBean.getIncome_num().stripTrailingZeros().toPlainString() + "");
                rankingEarnings.setText(inviteUserCountBean.getSum_self_num().stripTrailingZeros().toPlainString() + "");
                promotionEarnings.setText(inviteUserCountBean.getSum_push_num().stripTrailingZeros().toPlainString() + "");

                String ghs = CHSUtils.ghs(inviteUserCountBean.getSelf_hold_num());

                rankingForce.setText(ghs);

                promotionForce.setText(inviteUserCountBean.getDirect_push_num().stripTrailingZeros().toPlainString() + "");

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