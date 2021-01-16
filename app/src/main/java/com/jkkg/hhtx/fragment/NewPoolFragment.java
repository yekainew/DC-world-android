package com.jkkg.hhtx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.MainActivity;
import com.jkkg.hhtx.activity.NewMypoolActivity;
import com.jkkg.hhtx.activity.NewPoolAllActivity;
import com.jkkg.hhtx.activity.NewPoolAlllanActivity;
import com.jkkg.hhtx.activity.NewPoolMinerActivity;
import com.jkkg.hhtx.activity.NewPoolYeildActivity;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.net.bean.IncomeShatterBean;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.jkkg.hhtx.utils.BallModel;
import com.jkkg.hhtx.utils.EnergyTree;
import com.jkkg.hhtx.utils.TipsModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jkkg.hhtx.net.bean.InviteUserCountBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * new 矿池
 */
public class NewPoolFragment extends BaseFragment {


    @BindView(R.id.toolbar)
    TextView toolbar;
    @BindView(R.id.img_shouqu)
    ImageView imgShouqu;
    @BindView(R.id.btn_suanli)
    Button btnSuanli;
    @BindView(R.id.btn_zonglan)
    Button btnZonglan;
    @BindView(R.id.btn_reward)
    Button btnReward;
    @BindView(R.id.btn_mypool)
    Button btnMypool;
    @BindView(R.id.btn_mingxi)
    Button btnMingxi;
    @BindView(R.id.custom_view)
    EnergyTree mWaterFlake;
    private List<BallModel> mBallList;
    private List<TipsModel> mTipsList;

    public NewPoolFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_pool, container, false);
    }



    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        /*config();
        shatter();*/
        imgShouqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaterFlake.takeAll();
                incomeShatter();
//                mWaterFlake.cleanAll();
//                mWaterFlake.setModelList(mBallList,mTipsList);
            }
        });

       /* mWaterFlake.setOnBallItemListener(new EnergyTree.OnBallItemListener() {
            @Override
            public void onItemClick(BallModel ballModel) {
                incomeShatter(ballModel.getId());
            }
        });*/
        imgShouqu.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        mWaterFlake.isCollectTips(false);
        mWaterFlake.setOnBallItemListener(new EnergyTree.OnBallItemListener() {
            @Override
            public void onItemClick(BallModel ballModel) {
                incomeShatter(ballModel.getId());
//                Toast.makeText(getContext(),"收取了"+ballModel.getValue()+"能量",Toast.LENGTH_SHORT).show();
            }
        });
        mWaterFlake.setOnTakeAllListener(new EnergyTree.OnTakeAllListener() {
            @Override
            public void onTakeAllClick() {
                incomeShatter();
            }
        });

    }



  /*  private void initData() {
        mBallList = new ArrayList<>();
      *//*  mBallList.add(new BallModel("能量","5g"));
        mBallList.add(new BallModel("能量","7g"));
        mBallList.add(new BallModel("能量","15g"));
        mBallList.add(new BallModel("能量","1g"));
        mBallList.add(new BallModel("能量","2g"));
        mBallList.add(new BallModel("能量","9g"));
        mBallList.add(new BallModel("能量","9g"));*//*
        mTipsList = new ArrayList<>();
    }*/

    @OnClick({R.id.img_shouqu, R.id.btn_suanli, R.id.btn_zonglan, R.id.btn_reward, R.id.btn_mypool, R.id.btn_mingxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_shouqu:
                showMessage("尽情期待");
                break;
            case R.id.btn_suanli:
                Intent intent4 = new Intent(getContext(), NewPoolMinerActivity.class);
                intent4.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent4);
                break;
            case R.id.btn_zonglan:
                Intent intent3 = new Intent(getContext(), NewPoolAlllanActivity.class);
                intent3.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent3);
                break;
            case R.id.btn_reward:
                Intent intent2 = new Intent(getContext(), NewPoolAllActivity.class);
                intent2.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent2);
                break;
            case R.id.btn_mypool:
                Intent intent = new Intent(getContext(), NewMypoolActivity.class);
                intent.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent);
                break;
            case R.id.btn_mingxi:
                Intent intent1 = new Intent(getContext(), NewPoolYeildActivity.class);
                intent1.putExtra("poolConfigBean",poolConfigBean);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onResume() {
        config();
        shatter();
        super.onResume();
    }



    PoolConfigBean poolConfigBean;

    private void config() {
        showLoading();
        MyApp.requestSend.sendData("holdarea.method.conf").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                List<PoolConfigBean> poolConfigBeans = JSONArray.parseArray(message.getDate().toString(), PoolConfigBean.class);
                if (poolConfigBeans!=null&&!poolConfigBeans.isEmpty()) {
                    poolConfigBean = poolConfigBeans.get(0);
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
    public void shatter(){
        MyApp.requestSend.sendData("holdarea.method.shatter").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<IncomeShatterBean> incomeShatterBeans = JSONArray.parseArray(message.getDate().toString(), IncomeShatterBean.class);

                if (incomeShatterBeans.size()!=0) {
                    for (IncomeShatterBean incomeShatterBean : incomeShatterBeans) {
                        mBallList = new ArrayList<>();
                        if (poolConfigBean!=null) {
                            mBallList.add(new BallModel(incomeShatterBean.getShatter_num().setScale(2, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()+poolConfigBean.getFreed_type_name(),"",incomeShatterBean.getIncome_shatter_id()+""));
                        }else{
                            mBallList.add(new BallModel(incomeShatterBean.getShatter_num().setScale(2, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString(),"",incomeShatterBean.getIncome_shatter_id()+""));
                        }

                        mTipsList = new ArrayList<>();
                        mWaterFlake.setModelList(mBallList,mTipsList);
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

    public void incomeShatter(){
        MyApp.requestSend.sendData("holdarea.method.incomeShatter").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Toast.makeText(mContext, "收益成功", Toast.LENGTH_SHORT).show();
                showMessage(message.getMsg());
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                Toast.makeText(mContext, "收益失败", Toast.LENGTH_SHORT).show();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
    public void incomeShatter(String id){
        Map<String,String> map=new ArrayMap<>();
        map.put("income_shatter_id",id);
        MyApp.requestSend.sendData("holdarea.method.incomeShatter",map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Toast.makeText(mContext, "收益成功", Toast.LENGTH_SHORT).show();
                showMessage(message.getMsg());
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                Toast.makeText(mContext, "收益失败", Toast.LENGTH_SHORT).show();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBallList!=null) {
            mBallList.clear();

        }

        if (mTipsList!=null) {
            mTipsList.clear();
        }
    }
}
