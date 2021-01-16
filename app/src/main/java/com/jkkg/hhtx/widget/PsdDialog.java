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

public class PsdDialog extends Dialog {
    private OnTrueClick mOnTrueClick;
    private OnFlishClick mOnFlishClick;

    public PsdDialog(@NonNull Context context) {
        super(context, R.style.DialogActivityTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_exchange2);

        ClearEditText edit_psd = findViewById(R.id.edit_psd);
        TextView dialog_left_txt = findViewById(R.id.dialog_left_txt);
        TextView btn_ture = findViewById(R.id.btn_ture);


        dialog_left_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFlishClick!=null) {
                    mOnFlishClick.setOnFlishClick();
                }
            }
        });

        btn_ture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTrueClick!=null) {
                    String s = edit_psd.getText().toString();
                    if (StrUtil.isBlank(s)) {
                        Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mOnTrueClick.setOnTrueClick(edit_psd.getText().toString());
                }
            }
        });

    }

    public interface OnTrueClick{
        void setOnTrueClick(String psd);
    }

    public interface OnFlishClick{
        void setOnFlishClick();
    }

    public void setOnTrueClickListener(OnTrueClick clickListener){
        this.mOnTrueClick=clickListener;
    }

    public void setOnFlishClickListener (OnFlishClick clickListener){
        this.mOnFlishClick=clickListener;
    }
}
