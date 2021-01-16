package com.jkkg.hhtx.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jkkg.hhtx.R;

import cn.hutool.core.util.StrUtil;

public class ExchangeNameDialog extends Dialog {

    private ClearEditText edit_psd;
    private TextView btn_ture;
    private TextView dialog_left_txt;
    private OnTrueClick mOnTrueClick;
    private OnFalseClick mOnFalseClick;

    public ExchangeNameDialog(@NonNull Context context) {
        super(context, R.style.DialogActivityTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_exchange_name);
        initView();
    }

    private void initView() {
        edit_psd = findViewById(R.id.edit_psd);
        btn_ture = findViewById(R.id.btn_ture);
        dialog_left_txt = findViewById(R.id.dialog_left_txt);


        btn_ture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTrueClick != null) {
                    String s = edit_psd.getText().toString();
                    if (StrUtil.isBlank(s)) {
                        Toast.makeText(getContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mOnTrueClick.setOnTrueClick(s);
                }
            }
        });

        dialog_left_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFalseClick != null) {
                    mOnFalseClick.setOnFalseClick();
                }
            }
        });
    }

    public interface OnTrueClick {
        void setOnTrueClick(String wallet_name);
    }

    public void setOnTrueClickListener(OnTrueClick clickListener) {
        this.mOnTrueClick = clickListener;

    }

    public interface OnFalseClick {
        void setOnFalseClick();
    }

    public void OnFalseClickListener(OnFalseClick clickListener) {
        this.mOnFalseClick = clickListener;

    }
}
