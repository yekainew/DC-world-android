package com.jkkg.hhtx.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.SweetAlertDialog;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.widget.QuickPopup;

/**
 * 选择币种
 * 条目 item_select_bi
 * 币种选择状态 icon_choose_bi未选择    icon_choose_bi1选择
 * 创建弹窗 pop_create_bizhong
 */
public class CreateSelectBizhongActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_true)
    Button btnTrue;

    private SweetAlertDialog passwordAlertDialog;
    QuickPopup quickPopup;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_select_bizhong;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_select_bi);
        toolbarImageLeft.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_true)
    public void onViewClicked() {
        //下一步
        showTipDialog();
    }


    private void showTipDialog() {
        quickPopup = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.pop_create_bizhong)
                .show();
    }
}
