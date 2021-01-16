package com.jkkg.hhtx.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by
 * on 2020/11/23
 * Email:oubaoyi@outlook.com
 */
public class CustomRecyclerView extends RecyclerView {

    private static final String TAG = "CustomRecyclerView";
    private Context mContext;
    private Paint paint;
    private int height;
    private int width;
    private int spanPixel = 200;

    public CustomRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext=context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    public void setSpanPixel(int spanPixel) {
        this.spanPixel = spanPixel;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        //上下渐变
//        float spanFactor = dp2px(spanPixel) / (height / 2f);
//        LinearGradient linearGradient = new LinearGradient(0, 0, 0, height / 2,
//                new int[]{0x00000000, 0xff000000, 0xff000000}, new float[]{0, spanFactor, 1f}, Shader.TileMode.MIRROR);
        float spanFactor = dp2px(spanPixel) /height;
        //底部渐变
        LinearGradient linearGradient = new LinearGradient(0, spanPixel, 0, height,
                new int[]{ 0xff000000, 0x00000000}, new float[]{ spanFactor, 1f}, Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
    }

    @Override
    public void draw(Canvas c) {
        c.saveLayer(0, 0, width, height, null);
        super.draw(c);
        c.drawRect(0, 0, width, height, paint);
        c.restore();
    }

    /**
     * dp转px
     */
    private  int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }
}
