package com.jkkg.hhtx.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.jkkg.hhtx.R;

/**
 * 加载框
 */
public class LoadingProgressbar extends Dialog {
    public LoadingProgressbar(Context context){
        super(context, R.style.LoadingProgressbar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.layout_loading_progressbar_layout);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
    @Override
    public void hide(){
        if(this.isShowing()){
            dismiss();
        }
    }
}