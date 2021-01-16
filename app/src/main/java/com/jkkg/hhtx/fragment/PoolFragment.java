package com.jkkg.hhtx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.DefiActivity;
import com.jkkg.hhtx.activity.PoolIntroductionActivity;
import com.jkkg.hhtx.activity.PoolListActivity;
import com.jkkg.hhtx.activity.PoolStartActivity;
import com.jkkg.hhtx.activity.PoolYieldListActivity;
import com.jkkg.hhtx.adapter.PowerRankListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.net.bean.HoldAreaCountBean;
import com.jkkg.hhtx.net.bean.OptiamsBean;
import com.jkkg.hhtx.net.bean.OptimalKeepBean;
import com.jkkg.hhtx.net.bean.PowerRankListBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.jkkg.hhtx.utils.MPChartUtils;
import com.jkkg.hhtx.widget.LineChartMarkView;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import cn.hutool.json.JSONUtil;

import static cn.hutool.poi.excel.sax.AttributeName.s;
import static cn.hutool.poi.excel.sax.AttributeName.t;


/**
 * Description:
 * Created by ccw on 09/08/2020 15:20
 * Email:chencw0715@163.com
 * 矿池
 *
 * @author admin6
 */
public class PoolFragment extends BaseFragment {

    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
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
    @BindView(R.id.detail_chart_unit)
    TextView detailChartUnit;
    @BindView(R.id.detail_chart_unit_name)
    TextView detailChartUnitName;
    @BindView(R.id.yes_chibi)
    TextView yes_chibi;

    @BindView(R.id.wakuang_all)
    TextView wakuang_all;
  @BindView(R.id.suanli_xh)
    TextView suanli_xh;

    @BindView(R.id.liutong_all)
    TextView liutong_all;
    @BindView(R.id.xiaohui_all)
    TextView xiaohui_all;
    @BindView(R.id.min_all)
    TextView min_all;
    @BindView(R.id.chart)
    LineChart chart;
    List<String> xDataList;//x轴数据源
    List<Entry> yDataList;//y轴数据
    @BindView(R.id.pool_start)
    LinearLayout poolStart;
    @BindView(R.id.pool_list)
    LinearLayout poolList;
    @BindView(R.id.pool_introduce)
    LinearLayout poolIntroduce;
    @BindView(R.id.pool_deli)
    LinearLayout poolDeli;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PowerRankListAdapter powerRankListAdapter;
    private LineData data;
    private boolean isVisi=false;
    public static PoolFragment newInstance() {
        PoolFragment fragment = new PoolFragment();
        return fragment;
    }

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isVisi=true;
        return inflater.inflate(R.layout.fragment_pool, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        ImmersionBar.setTitleBar(this, toolbar);
        toolbarImageLeft.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_tab_sift);
        toolbarTvRight.setText(R.string.string_earning_list);

        powerRankListAdapter = new PowerRankListAdapter(mContext, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(powerRankListAdapter);
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PoolYieldListActivity.class));
            }
        });

       // optimals();
       // initChartView();
//        powerRank();

    }

    private void count() {
        MyApp.requestSend.sendData("holdarea.method.count").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                JSONArray objects = JSONArray.parseArray(message.getDate().toString());
                HoldAreaCountBean holdAreaCountBean = HoldAreaCountBean.newBean(HoldAreaCountBean.class, objects.get(0));

                BigDecimal optimal_keep = holdAreaCountBean.getOptimal_keep();
                yes_chibi.setText(CHSUtils.ghs(optimal_keep) +" GH/S");
//                yes_chibi.setText(holdAreaCountBean.getOptimal_keep().stripTrailingZeros().toPlainString()+" GH/S");
                wakuang_all.setText(holdAreaCountBean.getRelease_total().setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"DC");
                xiaohui_all.setText(holdAreaCountBean.getInvest_consume().setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"DC");
                min_all.setText(holdAreaCountBean.getMin_power().stripTrailingZeros().toPlainString()+"GH/S");
                liutong_all.setText(holdAreaCountBean.getCirculation().setScale(4,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()+"DC");
                suanli_xh.setText(holdAreaCountBean.getPower_consume().stripTrailingZeros().toPlainString()+" GH/S");
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void initChartView() {

        chart.removeAllViews();
        MPChartUtils.configChart(chart);
        //绘制图表
        chart.invalidate();



        MPChartUtils.configChartXY(chart, xDataList, max, 0, 7, false);
        LineDataSet d = MPChartUtils.getLineData(yDataList, "", chart.getResources().getColor(R.color.colorAccent)
                , chart.getResources().getColor(R.color.colorAccent), chart.getResources().getDrawable(R.drawable.bg_chart), true);
        data = new LineData(d);

        // 添加到图表中
        chart.setData(data);
        //绘制图表
        chart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        chart.setVisibleXRangeMaximum(7);
        LineChartMarkView mv = new LineChartMarkView(getContext());
        chart.setMarker(mv);
        //移到某个位置
        chart.moveViewToX(data.getEntryCount());


    }

    @Override
    public void onResume() {
        powerRank();
        count();
        optimals();
//        count();
        super.onResume();
    }

    private void optimals() {
        Map<String,String> map=new HashMap<String,String>(){
            {
                put("pageNum","1");
                put("pageSize","2000");
            }
        };
        MyApp.requestSend.sendData("holdarea.method.optimals", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                cn.hutool.json.JSONArray objects = JSONUtil.parseArray(message.getDate().toString());

                List<OptiamsBean> list=new ArrayList<>();

                Iterator<Object> iterator = objects.iterator();

                while (iterator.hasNext()){
                    OptiamsBean optiamsBean = OptiamsBean.newBean(OptiamsBean.class, iterator.next());
                    list.add(optiamsBean);
                }

                initChart(list);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    private void powerRank() {
        MyApp.requestSend.sendData("holdarea.method.power_rank").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                List<PowerRankListBean> o = gson.fromJson(message.getDate().toString(), new TypeToken<List<PowerRankListBean>>() {
                }.getType());
                powerRankListAdapter.setData(o);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    float max=0;
    private void initChart( List<OptiamsBean> o ) {

            xDataList = new ArrayList<>();
            yDataList = new ArrayList<>();

        //设置数据

        Collections.sort(o, new Comparator<OptiamsBean>() {
            @Override
            public int compare(OptiamsBean optiamsBean, OptiamsBean t1) {
                return (int)(optiamsBean.getDate().getTime()-t1.getDate().getTime());
            }
        });

        for (int i=0;i<o.size();i++) {
            xDataList.add(DateFormatUtils.format(o.get(i).getDate(),"MM/dd"));
            yDataList.add(new Entry(i,o.get(i).getOptimal_keep().floatValue()));
            float v = o.get(i).getOptimal_keep().floatValue();
            if (max<v){
                max=v;
            }
        }
        initChartView();
    }


    @OnClick({R.id.pool_start, R.id.pool_list, R.id.pool_introduce, R.id.pool_deli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pool_start:
                //开启矿机
               // startActivity(new Intent(mContext, PoolStartActivity.class));
                Toast.makeText(DataSave.app, "暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pool_list:
                //矿机列表
                startActivity(new Intent(mContext, PoolListActivity.class));
                break;
            case R.id.pool_introduce:
                //项目介绍
                startActivity(new Intent(mContext, PoolIntroductionActivity.class));
                break;
            case R.id.pool_deli:
                //defi孵化池
                startActivity(new Intent(mContext, DefiActivity.class));
                break;
            default:
                break;
        }
    }
}

