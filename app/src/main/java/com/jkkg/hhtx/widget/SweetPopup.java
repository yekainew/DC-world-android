package com.jkkg.hhtx.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import java.util.List;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

public class SweetPopup extends BasePopupWindow implements  LifecycleObserver {
    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.tv_dialog_message)
    TextView tvDialogMessage;
    @BindView(R.id.sweet_dialog_line)
    View sweetDialogLine;
    @BindView(R.id.dialog_left_txt)
    TextView dialogLeftTxt;
    @BindView(R.id.dialog_line)
    View dialogLine;
    @BindView(R.id.dialog_right_txt)
    TextView dialogRightTxt;
    @BindView(R.id.sweet_dialog_ll)
    LinearLayout sweetDialogLl;
    Unbinder unbinder;

    private int titleIds;
    private String title;
    private String message;
    private int viewHeight;
    public int background;
    private boolean isblur;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private OnSweetPopupClickListener mNegativeButtonListener;
    private OnSweetPopupClickListener mPositiveButtonListener;

    @Override
    public void onViewCreated(View contentView) {
        autoBindLifecycle(getContext());
        unbinder= ButterKnife.bind(this, contentView);
    }

    public SweetPopup(Context context, Builder builder) {
        super(context);
        this.titleIds = builder.mTitleResId;
        this.title = builder.mTitle;
        this.message = builder.mMessage;
        this.viewHeight = builder.viewHeight;
        this.isblur=builder.isblur;
        this.background=builder.background;
        this.mNegativeButtonText = builder.mNegativeButtonText;
        this.mPositiveButtonText = builder.mPositiveButtonText;
        this.mNegativeButtonListener = builder.mNegativeButtonListener;
        this.mPositiveButtonListener = builder.mPositiveButtonListener;
        updateUI();
    }

    private void updateUI() {
        // title resId
        if (titleIds != 0) {
            tvDialogTitle.setVisibility(View.VISIBLE);
            tvDialogTitle.setText(titleIds);
        }
        // title
        if (hasNull(title)) {
            tvDialogTitle.setVisibility(View.VISIBLE);
            tvDialogTitle.setText(title);
        } else {
            tvDialogTitle.setVisibility(View.GONE);
        }
        // message
        if (hasNull(message)) {
            if (viewHeight != 0) {
                tvDialogMessage.getLayoutParams().height = viewHeight;
            }
            RichText.initCacheDir(getContext());
            RichText.from(message).bind(getContext())
                    .autoFix(true) // 是否自动修复，默认true
                    .showBorder(false)
                    .noImage(false) // 不显示并且不加载图片
                    .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                    .linkFix(new LinkFixCallback() {
                        @Override
                        public void fix(LinkHolder holder) {
                            holder.setColor(Color.BLUE);
                            holder.setUnderLine(true);
                        }
                    })
                    .urlClick(new OnUrlClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            if (!TextUtils.isEmpty(url)) {
//                                Intent intent=new Intent(getContext(), H5Activity.class);
//                                intent.putExtra("url",url);
//                                getContext().startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .into(tvDialogMessage);
                        tvDialogMessage.setText(message);
                        tvDialogMessage.post(new Runnable() {
                            @Override
                            public void run() {
                                int lineCount = tvDialogMessage.getLineCount();
                                if (lineCount <= 1) {
                                    //    1行居中
                                    tvDialogMessage.setGravity(Gravity.CENTER);
                                } else {
                                    //  2行居左
                                    tvDialogMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                                }
                            }
                        });
        }
        //左侧文字为空
        if (!hasNull(mNegativeButtonText) && hasNull(mPositiveButtonText)) {
            dialogLeftTxt.setVisibility(View.GONE);
            dialogLine.setVisibility(View.GONE);
        }
        //右侧文字为空
        if (!hasNull(mPositiveButtonText) && hasNull(mNegativeButtonText)) {
            dialogRightTxt.setVisibility(View.GONE);
            dialogLine.setVisibility(View.GONE);
        }
        if (hasNull(mNegativeButtonText)) {
            dialogLeftTxt.setVisibility(View.VISIBLE);
            dialogLeftTxt.setText(mNegativeButtonText);
            dialogLeftTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(dialogLeftTxt);
                    }
                }
            });
        }
        if (hasNull(mPositiveButtonText)) {
            dialogRightTxt.setVisibility(View.VISIBLE);
            dialogRightTxt.setText(mPositiveButtonText);
            dialogRightTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(dialogRightTxt);
                    }
                }
            });
        }
    }

    public boolean hasNull(CharSequence msg) {
        return !TextUtils.isEmpty(msg);
    }

    /**
     * 控件自动绑定生命周期,宿主可以是activity或者fragment
     */
    private void autoBindLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (Fragment fragment : fragments) {
                View parent = fragment.getView();
                if (parent != null) {
                    fragment.getLifecycle().addObserver(this);
                    return;
                }
            }
        }
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if(unbinder!=null){
            unbinder.unbind();
        }
        RichText.clear(getContext());
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                //                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.layout_dialog_sweet_alert_view);
    }

    public SweetPopup show(){
        this.setBlurBackgroundEnable(isblur)
                .setBackground(background)
                .setPopupGravity(Gravity.CENTER)
                .setBackPressEnable(false)
                .setOutSideTouchable(true)
                .setOutSideDismiss(false)
                .showPopupWindow();
        return this;
    }

    public static class Builder {
        private Context mContext;
        private int mTitleResId;
        private String mTitle;
        private String mMessage;
        private int viewHeight;
        private int background;
        private boolean isblur;
        private CharSequence mNegativeButtonText;
        private CharSequence mPositiveButtonText;
        private OnSweetPopupClickListener mNegativeButtonListener;
        private OnSweetPopupClickListener mPositiveButtonListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public SweetPopup.Builder setBackground(int background) {
            this.background = background;
            return this;
        }

        public SweetPopup.Builder setIsblur(boolean isblur) {
            this.isblur = isblur;
            return this;
        }

        public SweetPopup.Builder setTitle(@StringRes int titleId) {
            this.mTitleResId = titleId;
            return this;
        }

        public SweetPopup.Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public SweetPopup.Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public SweetPopup.Builder setViewHeight(int height) {
            this.viewHeight = height;
            return this;
        }

        public SweetPopup.Builder setNegativeButton(CharSequence text, final OnSweetPopupClickListener listener) {
            this.mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public SweetPopup.Builder setPositiveButton(CharSequence text, final OnSweetPopupClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public SweetPopup build() {
            return new SweetPopup(mContext,this);
        }
    }

    public interface OnSweetPopupClickListener {
        void onClick(View v);
    }
}
