package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;

import org.bouncycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *重置密码
 */
public class ResetWalletPasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.new_password)
    ClearEditText newPassword;
    @BindView(R.id.et_sy)
    AppCompatEditText et_sy;
    @BindView(R.id.hide_password_new)
    ImageView hidePasswordNew;
    @BindView(R.id.new_password2)
    ClearEditText newPassword2;
    @BindView(R.id.hide_password_new2)
    ImageView hidePasswordNew2;
    @BindView(R.id.btn_true)
    Button btnTrue;

    private boolean isShowPassword2 = false;
    private boolean isShowPassword3 = false;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_reset_wallet_password;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_reset_wallet_password);

        hidePasswordNew.setImageResource(R.mipmap.icon_login_eye_down);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        hidePasswordNew2.setImageResource(R.mipmap.icon_login_eye_down);
        newPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Autowired
    Block block;

    @OnClick({R.id.hide_password_new, R.id.hide_password_new2, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                String newPassword = this.newPassword.getText().toString();
                String newPassword2 = this.newPassword2.getText().toString();

                String string = "";

                if (string.equals(newPassword.trim())) {
                    showMessage(getString(R.string.string_dlpassword_queren));
                    return;
                }

                if (!newPassword2.equals(newPassword)){
                    showMessage(getString(R.string.string_tip_create_password));
                    return;
                }
                boolean b = block.removeWallet(Constants.getWalletName());
                if (b) {
                    boolean b1 = block.importWallet(new WalletBean().setName(Constants.getWalletName()).setPri(et_sy.getText().toString()), newPassword2);
                    if (b1) {
                        startActivity(new Intent(this,MemberActivity.class));
                        finish();
                    }
                }

                break;
        }
    }
}