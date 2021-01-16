package com.jkkg.hhtx.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.jkkg.hhtx.R;

public class LineChartMarkView extends MarkerView {
    private TextView mXValueTv;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     */
    public LineChartMarkView(Context context) {
        super(context, R.layout.layout_chart_marker);
        mXValueTv = findViewById(R.id.xValues_tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mXValueTv.setText(e.getY()+"");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
