package com.jkkg.hhtx.widget.arcLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;

/**
 * 弧形的view
 */
public class ArcView extends View {
    private Paint mPaint;
    private PointF mStartPoint, mEndPoint, mControlPoint;
    private int mWidth;
    private int mHeight;
    private Path mPath = new Path();
    private int mArcHeight = 100;// 圆弧高度
    private int mStartColor;
    private int mEndColor;
    private LinearGradient mLinearGradient;

    public ArcView(Context context) {
        super(context);
        init(context,null);
    }
    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mArcHeight = typedArray.getDimensionPixelOffset(R.styleable.ArcView_arcHeight, 100);
        mStartColor=typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#ef1d22"));
        mEndColor = typedArray.getColor(R.styleable.ArcView_lgColor,  Color.parseColor("#ff4c39"));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);

        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlPoint = new PointF(0, 0);
    }

    public void setArcHeight(int height) {
        this.mArcHeight=height;
        invalidate();
    }

    /**
     *
     * @param startColor
     * @param endColor
     */
    public void setColor(@ColorInt int startColor, @ColorInt int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        mLinearGradient = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPath.reset();

        mPath.moveTo(0, 0);
        mPath.addRect(0, 0, mWidth, mHeight - mArcHeight, Path.Direction.CCW);

        mStartPoint.x = 0;
        mStartPoint.y = mHeight - mArcHeight;

        mEndPoint.x = mWidth;
        mEndPoint.y = mHeight - mArcHeight;

        mControlPoint.x = mWidth / 2;
        mControlPoint.y = mHeight + mArcHeight;        // 初始化shader
        mLinearGradient = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, mStartColor, mEndColor, Shader.TileMode.MIRROR);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(mLinearGradient);
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
        canvas.drawPath(mPath, mPaint);
    }
}
