package com.jkkg.hhtx.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.jkkg.hhtx.R;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnUrlClickListener;


public class SweetAlertDialog {
    private Context context;
    private int titleIds;
    private String title;
    private String message;
    private View view;
    private int viewHeight;
    private TextView mContentTitle;
    private TextView mContentMessage;
    private boolean cancelable;
    private Dialog mDialog;
    private FrameLayout tv_dialog_fl;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private OnDialogClickListener mNegativeButtonListener;
    private OnDialogClickListener mPositiveButtonListener;
    private TextView mLeftTxt;
    private TextView mRightTxt;
    private View mCenterLine;
    private View sweet_dialog_line;
    private LinearLayout sweet_dialog_ll;


    public SweetAlertDialog(Builder builder) {
        this.context = builder.mContext;
        this.titleIds = builder.mTitleResId;
        this.title = builder.mTitle;
        this.message = builder.mMessage;
        this.view = builder.view;
        this.viewHeight = builder.viewHeight;
        this.cancelable=builder.cancelable;
        this.mNegativeButtonText = builder.mNegativeButtonText;
        this.mPositiveButtonText = builder.mPositiveButtonText;
        this.mNegativeButtonListener = builder.mNegativeButtonListener;
        this.mPositiveButtonListener = builder.mPositiveButtonListener;
        this.initView();
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_sweet_alert_view, null);
        mContentTitle = rootView.findViewById(R.id.tv_dialog_title);
        mContentMessage = rootView.findViewById(R.id.tv_dialog_message);
        mLeftTxt = rootView.findViewById(R.id.dialog_left_txt);
        mRightTxt = rootView.findViewById(R.id.dialog_right_txt);
        tv_dialog_fl = rootView.findViewById(R.id.tv_dialog_fl);
        mCenterLine = rootView.findViewById(R.id.dialog_line);
        sweet_dialog_line = rootView.findViewById(R.id.sweet_dialog_line);
        sweet_dialog_ll = rootView.findViewById(R.id.sweet_dialog_ll);

        // 定义Dialog布局和参数
        mDialog = new Dialog(context, R.style.Sweet_Alert_Dialog);
        mDialog.setContentView(rootView);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
        updateDialogUI();
    }

    private void updateDialogUI() {
        // title resId
        if (titleIds != 0) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(titleIds);
        }
        // title
        if (hasNull(title)) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(title);
        } else {
            mContentTitle.setVisibility(View.GONE);
        }
        // message
        if (hasNull(message)) {
            RichText.initCacheDir(context);
            RichText.from(message).bind(context)
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
//                                Intent intent=new Intent(context, H5Activity.class);
//                                intent.putExtra("url",url);
//                                context.startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .into(mContentMessage);
            mContentMessage.setText(message);
            mContentMessage.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = mContentMessage.getLineCount();
                    if (lineCount <= 1) {
                        //    1行居中
                        mContentMessage.setGravity(Gravity.CENTER);
                    } else {
                        //  2行居左
                        mContentMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    }
                }
            });
        }
        if (view != null) {
            mContentMessage.setVisibility(View.GONE);
            tv_dialog_fl.addView(view);
            if (viewHeight != 0) {
                tv_dialog_fl.getLayoutParams().height = viewHeight;
            }
        }
        //左侧文字为空
        if (!hasNull(mNegativeButtonText) && hasNull(mPositiveButtonText)) {
            mLeftTxt.setVisibility(View.GONE);
            mCenterLine.setVisibility(View.GONE);
        }
        //右侧文字为空
        if (!hasNull(mPositiveButtonText) && hasNull(mNegativeButtonText)) {
            mRightTxt.setVisibility(View.GONE);
            mCenterLine.setVisibility(View.GONE);
        }
        if (hasNull(mNegativeButtonText)) {
            mLeftTxt.setVisibility(View.VISIBLE);
            mLeftTxt.setText(mNegativeButtonText);
            mLeftTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(mDialog, 0);
                    }
                }
            });
        }
        if (hasNull(mPositiveButtonText)) {
            mRightTxt.setVisibility(View.VISIBLE);
            mRightTxt.setText(mPositiveButtonText);
            mRightTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(mDialog, 1);
                    }
                }
            });
        }
    }

    public boolean hasNull(CharSequence msg) {
        return !TextUtils.isEmpty(msg);
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public boolean isShowing() {
        if (mDialog != null) {
           return mDialog.isShowing();
        }
        return false;
    }

    public void disDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public static class Builder {
        private Context mContext;
        private int mTitleResId;
        private String mTitle;
        private String mMessage;
        private View view;
        private int viewHeight;
        private boolean cancelable=false;
        private CharSequence mNegativeButtonText;
        private CharSequence mPositiveButtonText;
        private OnDialogClickListener mNegativeButtonListener;
        private OnDialogClickListener mPositiveButtonListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.mTitleResId = titleId;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setViewHeight(int height) {
            this.viewHeight = height;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnDialogClickListener listener) {
            this.mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnDialogClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public SweetAlertDialog show() {
            return new SweetAlertDialog(this);
        }
    }

    public interface OnDialogClickListener {
        void onClick(Dialog dialog, int which);
    }
}


