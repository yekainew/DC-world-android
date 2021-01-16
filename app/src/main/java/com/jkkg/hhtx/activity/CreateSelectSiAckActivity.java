package com.jkkg.hhtx.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.StringUtils;
import com.mugui.base.base.Autowired;

import org.tron.walletserver.WalletManager;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/15/2020 14:49
 * Email:chencw0715@163.com
 * 私钥备份
 * @author admin6
 */
public class CreateSelectSiAckActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_tv_left)
    TextView toolbarTvLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.create_text)
    TextView createText;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    WalletBean bc_private;
    @BindView(R.id.fuzhi_text)
    TextView fuzhiText;
    private String type;
    private Intent intent;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_select_si_ack;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_select_true);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        intent = getIntent();

        bc_private = (WalletBean) intent.getSerializableExtra("data");
        type = intent.getStringExtra("type");
        if (StringUtils.isNotEmpty(bc_private.getPri())) {
            createText.setText(bc_private.getPri());
        }
    }
@Autowired
Dao dao;
    @OnClick({R.id.btn_true, R.id.fuzhi_text})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_true:
                if (type.equals("1")) {
                    finish();
                } else {
                    if (intent.getAction()!=null) {
                        if (intent.getAction().equals("1")) {

                            finish();
                            overridePendingTransition(0, 0);

                        }else{
                            AppManager.getAppManager().killAll(CreateSelectSiAckActivity.class);
                            //完成备份进入主页
                            SpUtil.getInstance(this).saveBoolean("is_getWallet", true);
                            startActivity(new Intent(this, MemberActivity.class));
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    }
                }
                break;
            case R.id.fuzhi_text:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("inviteCode", createText.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getResources().getString(R.string.string_text_copy_seccess));
                break;
        }

    }

}
