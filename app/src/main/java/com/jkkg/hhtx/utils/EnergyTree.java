package com.jkkg.hhtx.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnergyTree extends FrameLayout {
    private OnBallItemListener mOnBallItemListener;
    private OnTipsItemListener mOnTipsItemListener;
    private OnTakeAllListener mOnTakeAllListener;
    private float mWidth, mHeight;
    private Random mRandom = new Random();
    private LayoutInflater mLayoutInflater;
    private List<Float> mOffsets = Arrays.asList(5.0f, 4.5f, 4.8f, 5.5f, 5.8f, 6.0f, 6.5f);
    private boolean isBall=true,isTips=true;
    public EnergyTree(@NonNull Context context) {
        this(context, null);
    }

    public EnergyTree(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnergyTree(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 点击所有
     */
    public void takeAll(){
        try {
            int count=getChildCount();
            for(int i=0;i<count;i++){
               View childView=getChildAt(i);
                Object tag = childView.getTag();
                if (tag instanceof BallModel) {
                    collectAnimator(childView,isBall);
                }
            }
            if (mOnTakeAllListener != null) {
                mOnTakeAllListener.onTakeAllClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置小球数量
    public void setModelList(final List<BallModel> ballList, final List<TipsModel> tipsList) {
        if (ballList == null || ballList.isEmpty()) {
            return;
        }
        removeAllViews();
        post(new Runnable() {
            @Override
            public void run() {
                addWaterView(ballList,tipsList);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void addWaterView(List<BallModel> ballList,List<TipsModel> tipsList) {




        int[] xRandom = randomCommon(0, 9, ballList.size());
        int[] yRandom = randomCommon2(1, 5, ballList.size());
        if (xRandom == null || yRandom == null) {
            return;
        }


        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int zhong_x = this.getWidth()/2;
        int zhong_y = this.getHeight()/2;

        float min_r=150;
        float max_r=zhong_x-120;



        for (int i = 0; i < ballList.size(); i++) {
            BallModel ballModel = ballList.get(i);
            final  LinearLayout view;
            view = (LinearLayout) mLayoutInflater.inflate(R.layout.item_ball, this, false);
            TextView view_id = view.findViewById(R.id.view_id);
            view_id.setText(ballList.get(i).getContent()+"\n"+ballList.get(i).getValue());
//            view.setText(ballList.get(i).getContent()+"\n"+ballList.get(i).getValue());
//            view.setX((float) ((mWidth * xRandom[i] * 0.1)));
//            view.setY((float) ((mHeight * yRandom[i] * 0.07)));


            double v = RandomUtils.nextDouble(min_r, max_r);

            double v1 = RandomUtils.nextDouble(0, Math.PI);
            double w = Math.cos(v1)*v;

            double h =- Math.sin(v1)*v;
            if(RandomUtils.nextDouble(0,1)>0.5){
                h=-h;
            }
            double v2 = w + zhong_x ;

            view.setX((float) v2-160/2);
            view.setY((float) h+zhong_y-70/2);

            view.setTag(ballModel);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag instanceof BallModel) {
                        if (mOnBallItemListener != null) {
                            mOnBallItemListener.onItemClick((BallModel) tag);
                        }
                        collectAnimator(view,isBall);
                    }
                }
            });
            view.setTag(R.string.isUp, mRandom.nextBoolean());
            setOffset(view);
            addView(view);
            addShowViewAnimation(view);
            start(view);
        }






        int[] num ={5,1,1,5};
        float[] num2 ={(float) 5.5,5,6, (float) 6.5};
        for (int i = 0; i < tipsList.size(); i++) {
            TipsModel tipsModel = tipsList.get(i);
            final  TextView view;
            view = (TextView) mLayoutInflater.inflate(R.layout.item_tips, this, false);
            view.setText(tipsList.get(i).getContent());

            double v = RandomUtils.nextDouble(min_r, max_r);

            double v1 = RandomUtils.nextDouble(0, Math.PI);
            double w = Math.cos(v1)*v;

            double h =- Math.sin(v1)*v;
            if(RandomUtils.nextDouble(0,1)>0.5){
                h=-h;
            }
            view.setX((float) w+zhong_x-this.getX());
            view.setY((float) h+zhong_y-this.getY());


            view.setTag(tipsModel);
            if(i==1||i==2){
//                view.setBackgroundResource(R.mipmap.tips2);
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag instanceof TipsModel) {
                        if (mOnTipsItemListener != null) {
                            mOnTipsItemListener.onItemClick((TipsModel) tag);
                        }
                        collectAnimator(view,isTips);
                    }
                }
            });
            view.setTag(R.string.isUp, mRandom.nextBoolean());
            setOffset(view);
            addView(view);
            addShowViewAnimation(view);
            start(view);
        }
    }

    //设置小球点击事件
    public void setOnBallItemListener(OnBallItemListener onBallItemListener) {
        mOnBallItemListener = onBallItemListener;
    }
    //设置全部收取点击事件
    public void setOnTakeAllListener(OnTakeAllListener onTakeAllListener) {
        mOnTakeAllListener = onTakeAllListener;
    }
    //设置提示点击事件
    public void setOnTipsItemListener(OnTipsItemListener onWaterItemListener) {
        mOnTipsItemListener = onWaterItemListener;
    }

    public void isCollectBall(boolean bl){
        isBall = bl;
    }

    public void isCollectTips(boolean bl){
        isTips = bl;
    }

    public interface OnBallItemListener {
        void onItemClick(BallModel ballModel);
    }

    public interface OnTakeAllListener {
        void onTakeAllClick();
    }

    public interface OnTipsItemListener {
        void onItemClick(TipsModel tipsModel);
    }
    private void collectAnimator(final View view,boolean isRun) {
        if(isRun) {
            ObjectAnimator translatAnimatorY = ObjectAnimator.ofFloat(view, "translationY", mHeight / 2);
            translatAnimatorY.start();

            ObjectAnimator translatAnimatorX = ObjectAnimator.ofFloat(view, "translationX", mWidth / 2 - 60);
            translatAnimatorX.start();

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            alphaAnimator.start();

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(translatAnimatorY).with(translatAnimatorX).with(alphaAnimator);
            animatorSet.setDuration(1500);
            animatorSet.start();
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView(view);
                }
            });
        }
    }

    //点击动画
    public void start(View view) {
        boolean isUp = (boolean) view.getTag(R.string.isUp);
        float offset = (float) view.getTag(R.string.offset);
        ObjectAnimator mAnimator;
        if (isUp) {
            mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getY() - offset, view.getY() + offset, view.getY() - offset);
        } else {
            mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getY() + offset, view.getY() - offset, view.getY() + offset);
        }
        mAnimator.setDuration(3000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    //添加显示动画
    private void addShowViewAnimation(View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(500).start();
    }

    /*
     * 随机指定XY轴不重复的数
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) ((Math.random() * (max - min)) + min);
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static int[] randomCommon2(int min, int max, int n) {
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) ((Math.random() * (max - min)) + min);
            result[count] = num;
            count++;
        }
        return result;
    }

    private void setOffset(View view) {
        float offset = mOffsets.get(mRandom.nextInt(mOffsets.size()));
        view.setTag(R.string.offset, offset);
    }
}
