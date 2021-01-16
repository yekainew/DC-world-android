package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改钱包密码
 */
public class ModifyWalletPasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.old_password)
    ClearEditText oldPassword;
    @BindView(R.id.hide_password_old)
    ImageView hidePasswordOld;
    @BindView(R.id.new_password)
    ClearEditText newPassword;
    @BindView(R.id.hide_password_new)
    ImageView hidePasswordNew;
    @BindView(R.id.new_password2)
    ClearEditText newPassword2;
    @BindView(R.id.hide_password_new2)
    ImageView hidePasswordNew2;
    @BindView(R.id.btn_true)
    Button btnTrue;

    private boolean isShowPassword1 = false;
    private boolean isShowPassword2 = false;
    private boolean isShowPassword3 = false;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_modify_wallet_password;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_modify_password));

        //初始化 小眼睛 闭着的
        hidePasswordOld.setImageResource(R.mipmap.icon_login_eye_down);
        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        hidePasswordNew.setImageResource(R.mipmap.icon_login_eye_down);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        hidePasswordNew2.setImageResource(R.mipmap.icon_login_eye_down);
        newPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @Autowired
    Block block;

    @OnClick({R.id.hide_password_old, R.id.hide_password_new, R.id.hide_password_new2, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hide_password_old:
                //原密码 显示或隐藏
                if (isShowPassword1) {
                    isShowPassword1 = false;
                    hidePasswordOld.setImageResource(R.mipmap.icon_login_eye_down);
                    oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    oldPassword.setSelection(oldPassword.getText().toString().length());
                } else {
                    isShowPassword1 = true;
                    hidePasswordOld.setImageResource(R.mipmap.icon_login_eye_up);
                    oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    oldPassword.setSelection(oldPassword.getText().toString().length());
                }
                break;
            case R.id.hide_password_new:
                //新密码 显示或隐藏
                if (isShowPassword2) {
                    isShowPassword2 = false;
                    hidePasswordNew.setImageResource(R.mipmap.icon_login_eye_down);
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPassword.setSelection(newPassword.getText().toString().length());
                } else {
                    isShowPassword2 = true;
                    hidePasswordNew.setImageResource(R.mipmap.icon_login_eye_up);
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPassword.setSelection(newPassword.getText().toString().length());
                }
                break;
            case R.id.hide_password_new2:
                //确认新密码 显示或隐藏

                if (isShowPassword3) {
                    isShowPassword3 = false;
                    hidePasswordNew2.setImageResource(R.mipmap.icon_login_eye_down);
                    newPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPassword2.setSelection(newPassword2.getText().toString().length());
                } else {
                    isShowPassword2 = true;
                    hidePasswordNew2.setImageResource(R.mipmap.icon_login_eye_up);
                    newPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPassword2.setSelection(newPassword2.getText().toString().length());
                }
                break;
            case R.id.btn_true:
                //确认修改
                String oldPassword = this.oldPassword.getText().toString();
                String newPassword = this.newPassword.getText().toString();
                String newPassword2 = this.newPassword2.getText().toString();

                String string = "";
                if (string.equals(oldPassword.trim())) {
                    showMessage(getString(R.string.string_dlpassword_no_null));
                    return;
                }
                if (string.equals(newPassword.trim())) {
                    showMessage(getString(R.string.string_dlpassword_queren));
                    return;
                }

                if (!newPassword2.equals(newPassword)){
                    showMessage(getString(R.string.string_tip_create_password));
                    return;
                }


                WalletBean wallet = block.getWallet(Constants.getWalletName(),oldPassword);
                boolean b1 = block.removeWallet(Constants.getWalletName());
                if (b1) {
                    boolean b = block.importWallet(new WalletBean().setPri(wallet.getPri()).setName(Constants.getWalletName()).setRecoveryPhrase(wallet.getAddress()).setRecoveryPhrase(wallet.getRecoveryPhrase()), newPassword);
                    if (b) {
                        Intent intent = new Intent(this, MemberActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                break;
        }
    }
}
