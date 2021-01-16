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

public class PasswordDialog extends Dialog {

    private ClearEditText edit_num;
    private ClearEditText edit_password;
    private ClearEditText edit_yanzheng;
    private TextView dialog_left_txt;
    private TextView dialog_right_txt;
    private OnYesClick mOnYesClick;
    private OnNoClick mOnNoClick;

    private Context context;


    public PasswordDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_withdraw_deposit);
        initView();
    }

    private void initView() {
        edit_num = findViewById(R.id.edit_num);
        edit_password = findViewById(R.id.edit_password);
        dialog_left_txt = findViewById(R.id.dialog_left_txt);
        dialog_right_txt = findViewById(R.id.dialog_right_txt);
        edit_yanzheng = findViewById(R.id.edit_yanzheng);


        dialog_right_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StrUtil.isBlank(edit_num.getText().toString())) {
                    Toast.makeText(context, "金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (StrUtil.isBlank(edit_yanzheng.getText().toString())) {
                    Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mOnYesClick!=null) {
                    mOnYesClick.setOnYesClick(edit_num.getText().toString(),edit_yanzheng.getText().toString());
                }

            }
        });

        dialog_left_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNoClick!=null) {
                    mOnNoClick.setOnNoClick();
                }
            }
        });
    }

    public interface OnYesClick{
        void setOnYesClick(String num,String yanzheng);
    }
    public interface OnNoClick{
        void setOnNoClick();
    }

    public void setOnYesClickListener (OnYesClick clickListener){
        this.mOnYesClick=clickListener;
    }

    public void setOnNoClickListener (OnNoClick clickListener){
        this.mOnNoClick=clickListener;
    }



}
