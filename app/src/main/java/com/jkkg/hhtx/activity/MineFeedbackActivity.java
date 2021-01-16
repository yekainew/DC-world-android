package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

/**
 * 留言
 */
public class MineFeedbackActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_title)
    ClearEditText editTitle;
    @BindView(R.id.edit_text)
    ClearEditText editText;
    @BindView(R.id.btn_true)
    Button btnTrue;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_feedback;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_feedback));



    }

    @OnClick(R.id.btn_true)
    public void onViewClicked() {
        showLoading();
        save();
    }

    private void save() {

        String s = editText.getText().toString();
        if (StrUtil.isBlank(s)) {
            showMessage(getString(R.string.string_feedback_nonull));
            hideLoading();
            return;
        }
        Map<String,String> map=new HashMap<String,String>(){
            {
                put("content",editText.getText().toString());
            }
        };
        MyApp.requestSend.sendData("feedback.method.save", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
}
