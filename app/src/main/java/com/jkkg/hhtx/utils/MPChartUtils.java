package com.jkkg.hhtx.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.jkkg.hhtx.R;

import java.util.List;

import androidx.annotation.ColorInt;

public class MPChartUtils {
    /**
     *  配置Chart 基础设置
     * @param chart 图表
     */
    public static void configChart(LineChart chart) {
        //不显示描述数据
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        //没有数据时显示的文字
        chart.setNoDataText("暂无数据");
        //没有数据时显示文字的颜色
        chart.setNoDataTextColor(chart.getResources().getColor(R.color.colorAccent));
        // 设置是否绘制背景
        chart.setDrawGridBackground(false);
        // 设置是否绘制边框
        chart.setDrawBorders(false);
        // 设置是否可以缩放图表
        chart.setScaleEnabled(false);
        // 设置是否可以用手指移动图表
        chart.setDragEnabled(true);
        //设置为true,图表在手指触碰滑动后,会有惯性继续运动
        chart.setDragDecelerationEnabled(true);
        //阻尼系数
        //chart.setDragDecelerationFrictionCoef(0.9f);
        //设置在曲线图中显示的最大数量 设置数据后调用
        // chart.setVisibleXRangeMaximum(4);
    }

    /**
     *  配置Chart 基础设置
     * @param chart 图表
     * @param xDataList x 轴标签
     * @param yMax y 轴最大值
     * @param yMin y 轴最小值
     * @param labelCount y轴显示数量
     * @param isShowLegend 是否显示图例
     */
    public static void configChartXY(LineChart chart, final List<String> xDataList, float yMax, float yMin, int labelCount, boolean isShowLegend) {
        Legend legend = chart.getLegend();
        // 是否显示图例
        if (isShowLegend) {
            legend.setEnabled(true);
            legend.setTextColor(Color.WHITE);
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setYEntrySpace(20f);
            //图例的大小
            legend.setFormSize(7f);
            // 图例描述文字大小
            legend.setTextSize(12);
            legend.setXEntrySpace(20f);

        } else {
            // 设置图例不展示
            legend.setEnabled(false);
        }


        // x轴相关配置
        XAxis xAxis = chart.getXAxis();
        // 是否显示x轴线
        xAxis.setDrawAxisLine(true);
        // 设置x轴线的颜色
        xAxis.setAxisLineColor(chart.getResources().getColor(R.color.chart_line));
        // 是否绘制x方向网格线
        xAxis.setDrawGridLines(false);
        //x方向网格线的颜色
        xAxis.setGridColor(chart.getResources().getColor(R.color.chart_line));
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 设置x轴数据偏移量
//        xAxis.setXOffset(1);
//        xAxis.setSpaceMin(0.2f);
//        xAxis.setSpaceMax(1f);
        //图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
       // xAxis.setAvoidFirstLastClipping(true);
        // 显示x轴标签
        ValueFormatter formatter = new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index < 0 || index >= xDataList.size()) {
                    return "";
                }
                return xDataList.get(index);
                // return labels.get(Math.min(Math.max((int) value, 0), labels.size() - 1));
            }

        };
        // 引用标签
        xAxis.setValueFormatter(formatter);
        // 设置x轴每最小刻度 interval
        xAxis.setGranularity(1f);
        // 设置x轴文字的大小
        xAxis.setTextSize(12);
        xAxis.setTextColor(chart.getResources().getColor(R.color.font_color_text));

        //y轴相关配置
        YAxis yAxis = chart.getAxisLeft();
        //设置y轴的最大值
        yAxis.setAxisMaximum(yMax);
        // 设置y轴的最小值
        yAxis.setAxisMinimum(yMin);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        //y方向网格线的颜色
        yAxis.setGridColor(chart.getResources().getColor(R.color.chart_line));
        // 是否显示y轴线
        yAxis.setDrawAxisLine(true);
        // 设置y轴线的颜色
        yAxis.setAxisLineColor(chart.getResources().getColor(R.color.chart_line));

        // 设置y轴的文字颜色
        yAxis.setTextColor(chart.getResources().getColor(R.color.font_color_text));
        // 设置y轴文字的大小
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
//         yAxis.setXOffset(1);
        // 设置y轴label 数量
        yAxis.setLabelCount(labelCount, true);
        // 设置y轴的最小刻度
        yAxis.setGranularity(10);//interval
        // x轴执行动画
        chart.animateXY(500, 500);
    }

    /**
     * 获取LineDataSet
     * @param entries
     * @param label
     * @param textColor
     * @param lineColor
     * @return
     */
    public static LineDataSet getLineData(List<Entry> entries, String label, @ColorInt int textColor, @ColorInt int lineColor,Drawable drawable, boolean isFill) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        // 设置曲线的颜色
        dataSet.setColor(lineColor);
        //数值文字颜色
        dataSet.setValueTextColor(textColor);
        //数值文字大小
        dataSet.setValueTextSize(12);
        // 模式为贝塞尔曲线
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        // 是否绘制数据值
        dataSet.setDrawValues(false);
        // 是否绘制圆点
        dataSet.setDrawCircles(false);
        //空心圆
//        dataSet.setDrawCircleHole(true);
        // 隐藏点击时候的高亮线
        dataSet.setHighlightEnabled(true);
        //设置高亮线为透明色
        dataSet.setHighLightColor(lineColor);
        //设置高亮线为虚线
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);

        if (isFill) {
            //是否设置填充曲线到x轴之间的区域
            dataSet.setDrawFilled(true);
            // 填充颜色
//            dataSet.setFillColor(lineColor);
            dataSet.setFillDrawable(drawable);
        }
//        //设置圆点的颜色
//        dataSet.setCircleColor(lineColor);
//        // 设置圆点半径
//        dataSet.setCircleRadius(3.5f);
        // 设置线的宽度
        dataSet.setLineWidth(1f);
        return dataSet;
    }
}
