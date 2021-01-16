package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.RewardMoreLogAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.NodeEmancipationBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.net.bean.WalletLog;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 多重奖励
 */
public class RewardActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reward_community_dc)
    TextView rewardCommunityDc;
    @BindView(R.id.reward_community_sf)
    TextView rewardCommunitySf;
    @BindView(R.id.reward_community_tq)
    TextView rewardCommunityTq;
    @BindView(R.id.reward_community_ktq)
    TextView rewardCommunityKtq;
    @BindView(R.id.reward_community_extract_records)
    Button rewardCommunityExtractRecords;
    @BindView(R.id.reward_community_reward_record)
    Button rewardCommunityRewardRecord;
    @BindView(R.id.reward_community_tx)
    Button rewardCommunityTx;
    @BindView(R.id.reward_subscription_dc)
    TextView rewardSubscriptionDc;
    @BindView(R.id.reward_subscription_sf)
    TextView rewardSubscriptionSf;
    @BindView(R.id.reward_subscription_tq)
    TextView rewardSubscriptionTq;
    @BindView(R.id.reward_subscription_ktq)
    TextView rewardSubscriptionKtq;
    @BindView(R.id.reward_subscription_extract_records)
    Button rewardSubscriptionExtractRecords;
    @BindView(R.id.reward_subscription_reward_record)
    Button rewardSubscriptionRewardRecord;
    @BindView(R.id.reward_subscription_tx)
    Button rewardSubscriptionTx;
/*    @BindView(R.id.reward_community)
    TextView rewardCommunity;
    @BindView(R.id.reward_community_time)
    TextView rewardCommunityTime;
    @BindView(R.id.reward_community_num)
    TextView rewardCommunityNum;*/
   /* @BindView(R.id.reward_subscription)
    TextView rewardSubscription;*/
    /*@BindView(R.id.reward_subscription_time)
    TextView rewardSubscriptionTime;*/
    /*@BindView(R.id.reward_subscription_num)
    TextView rewardSubscriptionNum;*/
    @BindView(R.id.reward_more)
    Button rewardMore;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Intent intent;
    private NodeEmancipationBean bean;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_reward;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getResources().getColor(R.color.transparent);
        toolbarImageLeft.setVisibility(View.VISIBLE);

        communityRewards();
        redeemRewards();
        rewardsRecord ();
    }
    /**
     * 兑换奖励
     * @return {@link Object}
     */
    private void redeemRewards() {
        MyApp.requestSend.sendData("emlog.app.method.redeemRewards").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                bean = NodeEmancipationBean.newBean(NodeEmancipationBean.class, message.getDate());
                if (bean !=null) {
                    rewardSubscriptionSf.setText(bean.getAlready_released().stripTrailingZeros().toPlainString() + bean.getDc_type_name());
                    rewardSubscriptionKtq.setText(bean.getExtractable().stripTrailingZeros().toPlainString() + bean.getDc_type_name());
                    rewardSubscriptionDc.setText(bean.getAmount().stripTrailingZeros().toPlainString());
                    rewardSubscriptionTq.setText(bean.getExtracted().stripTrailingZeros().toPlainString() + bean.getDc_type_name());
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    /**
     * 奖励记录
     */
    public void rewardsRecord (){
        Map<String,String> map=new HashMap<String, String>(){
            {
                put("pageNum",1+"");
                put("pageSize",2000+"");
            }
        };
        MyApp.requestSend.sendData("emlog.app.method.recordDetail",map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<WalletLog> walletLogs = JSONArray.parseArray(message.getDate().toString(), WalletLog.class);
                RewardMoreLogAdapter rewardMoreLogAdapter=new RewardMoreLogAdapter();
                rewardMoreLogAdapter.setContext(RewardActivity.this).setWalletLogs(walletLogs);
                recyclerView.setLayoutManager(new LinearLayoutManager(RewardActivity.this));
                recyclerView.setAdapter(rewardMoreLogAdapter);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }
    private NodeEmancipationBean nodeEmancipationBean;

    /**
     * 社区奖励
     *
     * @return {@link Object}
     */
    private void communityRewards() {
        MyApp.requestSend.sendData("emlog.app.method.communityRewards").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                nodeEmancipationBean = NodeEmancipationBean.newBean(NodeEmancipationBean.class, message.getDate());
                if (nodeEmancipationBean !=null) {
                    rewardCommunitySf.setText(nodeEmancipationBean.getAlready_released().stripTrailingZeros().toPlainString()+ nodeEmancipationBean.getDc_type_name());
                    rewardCommunityKtq.setText(nodeEmancipationBean.getExtractable().stripTrailingZeros().toPlainString()+ nodeEmancipationBean.getDc_type_name());
                    rewardCommunityDc.setText(nodeEmancipationBean.getAmount().stripTrailingZeros().toPlainString());
                    rewardCommunityTq.setText(nodeEmancipationBean.getExtracted().stripTrailingZeros().toPlainString()+ nodeEmancipationBean.getDc_type_name());
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @OnClick({R.id.reward_community_extract_records, R.id.reward_community_reward_record, R.id.reward_community_tx, R.id.reward_subscription_extract_records, R.id.reward_subscription_reward_record, R.id.reward_subscription_tx, R.id.reward_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reward_community_extract_records:
                //提取记录 社区奖励
                intent = new Intent(this,RewardListActivity.class).setAction("4");
                startActivity(intent);
                break;
            case R.id.reward_community_reward_record:
                //奖励记录   社区奖励
                intent = new Intent(this,RewardListActivity.class).setAction("2");
                startActivity(intent);
                break;
            case R.id.reward_community_tx:
                //提余额  社区奖励RewardWithdrawActivity
                intent = new Intent(this,RewardWithdrawActivity.class);
                intent.setAction("4");
                intent.putExtra("date",nodeEmancipationBean);
                startActivity(intent);
                break;
            case R.id.reward_subscription_extract_records:
                //提取记录 认购奖励
                intent = new Intent(this,RewardListActivity.class).setAction("3");
                startActivity(intent);
                break;
            case R.id.reward_subscription_reward_record:
                //奖励记录   认购奖励
                intent = new Intent(this,RewardListActivity.class).setAction("1");
                startActivity(intent);
                break;
            case R.id.reward_subscription_tx:
                //提余额   认购奖励
                intent = new Intent(this,RewardWithdrawActivity.class);
                intent.setAction("5");
                intent.putExtra("date",bean);
                startActivity(intent);
                break;
            case R.id.reward_more:
                //更多
                break;
            default:
                break;
        }
    }
}
