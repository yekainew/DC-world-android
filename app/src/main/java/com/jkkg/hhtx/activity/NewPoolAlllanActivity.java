package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.PoolConfigBean;
import com.liys.view.LineProView;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * new pool 全览
 */
public class NewPoolAlllanActivity extends BaseActivity {
    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.mPieChart)
    PieChart mPieChart;
    @BindView(R.id.tv_chart1)
    TextView tvChart1;
    @BindView(R.id.tv_chart1_num)
    TextView tvChart1Num;
    @BindView(R.id.tv_chart2)
    TextView tvChart2;
    @BindView(R.id.tv_chart2_num)
    TextView tvChart2Num;
    @BindView(R.id.tv_chart3)
    TextView tvChart3;
    @BindView(R.id.tv_chart3_num)
    TextView tvChart3Num;
    @BindView(R.id.linepro)
    LineProView linepro;
    @BindView(R.id.baifenbi)
    TextView baifenbi;
    @BindView(R.id.bili_num)
    TextView biliNum;
    @BindView(R.id.center_text)
    TextView centerText;
    @BindView(R.id.alllan_tv)
    TextView alllanTv;
    private PoolConfigBean poolConfigBean;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_pool_alllan;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        poolConfigBean = (PoolConfigBean) intent.getSerializableExtra("poolConfigBean");
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initChart();
        centerText.setText(Html.fromHtml("本矿池将设立<font color=\"#FE9516\"><big><b>21</b></big></font>个创世节点及" +
                "<font color=\"#FE9516\"><big><b>108</b></big></font>个超级节点，回馈忠实的粉丝用户,有多项权益。"));
        linepro.init();
        overview();
    }

    private void initChart() {
        mPieChart.setUsePercentValues(false);
        mPieChart.getDescription().setEnabled(false);
        //设置中间文字
        mPieChart.setCenterText(generateCenterSpannableText());
        mPieChart.setCenterTextColor(Color.parseColor("#ffffff"));
        mPieChart.setCenterTextSize(10);
        mPieChart.setDrawCenterText(true);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.TRANSPARENT);
        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(false);
        //不显示图例
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
        //模拟数据
       /* ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        //挖矿
        entries.add(new PieEntry(50, ""));
        //生态
        entries.add(new PieEntry(20, ""));
        //认购
        entries.add(new PieEntry(30, ""));
        //设置数据
        setData(entries);*/
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("全网\n累计挖矿");
        return s;
    }


    public void overview() {
        MyApp.requestSend.sendData("holdarea.method.overview").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                JSONObject parse = JSONObject.parseObject(message.getDate().toString());
                BigDecimal freed_total = parse.getBigDecimal("freed_total");//释放总量
                BigDecimal release_total = parse.getBigDecimal("release_total");//已释放
                String shengtai = parse.getString("shengtai");//生态
                String rengou = parse.getString("rengou");//认购

                biliNum.setText(release_total.stripTrailingZeros().toPlainString() + "/" + freed_total.stripTrailingZeros().toPlainString());
                alllanTv.setText("累计挖矿"+freed_total.stripTrailingZeros().toPlainString()+poolConfigBean.getFreed_type_name()+"时投票");
                BigDecimal divide = release_total.divide(freed_total, 2, BigDecimal.ROUND_DOWN);
                BigDecimal bigDecimal = new BigDecimal("100");
                BigDecimal multiply = divide.multiply(bigDecimal);

                BigDecimal subtract = new BigDecimal(shengtai).subtract(bigDecimal);//生态
                BigDecimal subtract1 = release_total.subtract(bigDecimal);//矿池
                BigDecimal subtract2 = new BigDecimal(rengou).subtract(bigDecimal);//认购
                tvChart1Num.setText(freed_total.stripTrailingZeros().toPlainString());
                tvChart2Num.setText(shengtai);
                tvChart3Num.setText(rengou);
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                //挖矿
                entries.add(new PieEntry(subtract1.floatValue(), ""));
                //生态
                entries.add(new PieEntry(subtract.floatValue(), ""));
                //认购
                entries.add(new PieEntry(subtract2.floatValue(), ""));
                //设置数据
                setData(entries);

                BigDecimal add = subtract.add(subtract1).add(subtract2);

                BigDecimal divide1 = subtract.divide(add, 4, BigDecimal.ROUND_DOWN).multiply(bigDecimal);//生态百分比
                BigDecimal divide2 = subtract1.divide(add, 4, BigDecimal.ROUND_DOWN).multiply(bigDecimal);//矿池百分比
                BigDecimal divide3 = subtract2.divide(add, 4, BigDecimal.ROUND_DOWN).multiply(bigDecimal);//生态百分比

                tvChart1.setText("挖矿：" + divide2.stripTrailingZeros().toPlainString() + " %");
                tvChart2.setText("生态：" + divide1.stripTrailingZeros().toPlainString() + " %");
                tvChart3.setText("认购：" + divide3.stripTrailingZeros().toPlainString() + " %");


                linepro.setProgress(multiply.doubleValue());


                baifenbi.setText(multiply.stripTrailingZeros().toPlainString() + " %");
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //挖矿
        colors.add(Color.parseColor("#D6112D"));
        //生态
        colors.add(Color.parseColor("#FE9516"));
        //认购
        colors.add(Color.parseColor("#0B6AEC"));
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0f);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}