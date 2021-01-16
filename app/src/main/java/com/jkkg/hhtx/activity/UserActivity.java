package com.jkkg.hhtx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.SweetAlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/10/2020 16:49
 * Email:chencw0715@163.com
 * 用户资料
 */
public class UserActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.walletMsg_phone)
    TextView walletMsgPhone;
    @BindView(R.id.walletMsg_id)
    TextView walletMsgId;
    @BindView(R.id.update_name)
    TextView updateName;
    @BindView(R.id.update_head)
    TextView updateHead;
    private SweetAlertDialog nameAlertDialog;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_user;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_user));

        walletMsgPhone.setText(Constants.getUserPhone());
        walletMsgId.setText(Constants.getUserId());
        updateName.setText(Constants.getUserName());
    }

/*
    //修改名称
    private void updateNameDialog() {
        nameAlertDialog = null;
        View view = View.inflate(UserActivity.this, R.layout.self_dialog_update_name, null);
        ClearEditText tv = view.findViewById(R.id.selfDialogUpdateName_edit);
        if (wallet != null && StringUtils.isNotEmpty(wallet.getName())) {
            tv.setText(wallet.getName());
            tv.setSelection(tv.getText().toString().length());
        }
        nameAlertDialog = new SweetAlertDialog.Builder(UserActivity.this)
                .setTitle(getString(R.string.string_mine_user_edit_name))
                .setView(view)
                .setViewHeight(ArmsUtils.dip2px(UserActivity.this, 70))
                .setPositiveButton(getString(R.string.string_sweet_confirm), new SweetAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (StringUtils.isEmpty(tv.getText().toString())) {
                            showMessage(getString(R.string.string_mine_user_null_name));
                        } else {
                            mPresenter.requestUpdateName(tv.getText().toString());
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.string_sweet_cancle), new SweetAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
*/
    @OnClick({R.id.update_name, R.id.update_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_name:
                //编辑名字
//                updateNameDialog();
                showMessage(getString(R.string.string_text_mine_null));
                break;
            case R.id.update_head:
                //上传头像
                showMessage(getString(R.string.string_text_mine_null));
                break;
            default:
                break;
        }
    }
}
