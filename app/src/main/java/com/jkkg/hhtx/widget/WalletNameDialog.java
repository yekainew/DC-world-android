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

public class WalletNameDialog extends Dialog {

    private ClearEditText edit_name;
    private TextView dialog_left_txt;
    private TextView btn_ture;
    private OnTrueClick mOnTrueClick;
    private OnFalseClick mOnFalseClick;

    public WalletNameDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.name_dialog);


        initView();
    }

    private void initView() {

        edit_name = findViewById(R.id.edit_name);
        dialog_left_txt = findViewById(R.id.dialog_left_txt);
        btn_ture = findViewById(R.id.btn_ture);


        dialog_left_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnFalseClick!=null) {
                    mOnFalseClick.setOnFalseClick();
                }
            }
        });

        btn_ture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnTrueClick!=null) {


                    String s = edit_name.getText().toString();
                    if (StrUtil.isBlank(s)) {
                        Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mOnTrueClick.setOnTrueClick(s);
                }
            }
        });

    }

    public interface OnTrueClick{
        void setOnTrueClick(String name);
    }

    public interface OnFalseClick{
        void setOnFalseClick();
    }

    public void setOnTrueClickListener(OnTrueClick clickListener){
        this.mOnTrueClick=clickListener;
    }

    public void setOnFalseClickListener (OnFalseClick clickListener){
        this.mOnFalseClick=clickListener;
    }
}
