package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.InviteUserCountBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * new矿池
 * 矿工算力
 */
public class NewPoolMinerActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.pool_all_num)
    TextView poolAllNum;
    @BindView(R.id.pool_chibi_num)
    TextView poolChibiNum;
    @BindView(R.id.pool_tuiguang_num)
    TextView poolTuiguangNum;
    @BindView(R.id.pool_zong_num1)
    TextView poolZongNum1;
    @BindView(R.id.pool_zong_num2)
    TextView poolZongNum2;
    @BindView(R.id.pool_zong_num3)
    TextView poolZongNum3;
    @BindView(R.id.zong_num)
    TextView zongNum;
    @BindView(R.id.chibi_num)
    TextView chibiNum;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_miner;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        request();
    }

    private void request() {
        showLoading();
        MyApp.requestSend.sendData("holdarea.method.community").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                InviteUserCountBean inviteUserCountBean = InviteUserCountBean.newBean(InviteUserCountBean.class, message.getDate());
                BigDecimal sum_team_num = inviteUserCountBean.getIncome_num();//团队收益pool_chibi_num
                BigDecimal self_hold_num = inviteUserCountBean.getSelf_hold_num();//持币算例
                BigDecimal direct_push_num = inviteUserCountBean.getDirect_push_num();//推广算例
                int team_size = inviteUserCountBean.getTeam_size();//团队人数

                BigDecimal push_size = inviteUserCountBean.getNow_output_dc();

                BigDecimal net_all_power = inviteUserCountBean.getNet_all_power();//全网挖矿总算力
                BigDecimal net_push_power = inviteUserCountBean.getNet_push_power();//全网挖矿总算力


                poolChibiNum.setText(CHSUtils.ghs(self_hold_num)+"GH/S");
                poolTuiguangNum.setText(CHSUtils.ghs(direct_push_num)+"GH/S");
                poolAllNum.setText(sum_team_num.stripTrailingZeros().toPlainString());
                chibiNum.setText(direct_push_num.stripTrailingZeros().toPlainString()+"GH/S");


                poolZongNum1.setText(push_size.setScale(0,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"GH/S");
                poolZongNum2.setText(net_all_power.setScale(0,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"GH/S");
                poolZongNum3.setText(net_push_power.setScale(0,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"GH/S");
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
