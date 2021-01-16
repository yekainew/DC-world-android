package com.jkkg.hhtx.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jkkg.hhtx.R;

public class UsdtDialog extends Dialog {

    private TextView usdt_name;

    public UsdtDialog(@NonNull Context context) {
        super(context,R.style.DialogActivityTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_bi);

        usdt_name = findViewById(R.id.usdt_name);

        usdt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
