package com.jkkg.hhtx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.CreateSelectHelpActivity;
import com.jkkg.hhtx.activity.MainActivity;
import com.jkkg.hhtx.activity.RecoverActivity;
import com.jkkg.hhtx.activity.WalletManagementActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.block.BIP39;
import com.mugui.block.TronBlockHandle;
import com.mugui.block.ValidationException;
import com.mugui.block.sql.BlockWalletBean;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;

import org.tron.common.utils.ByteArray;
import org.tron.walletserver.WalletManager;

import butterknife.BindView;
import butterknife.OnClick;
import jnr.ffi.annotations.Clear;

/**
 * 恢复助记词
 */
public class RecoverZhujiFragment extends BaseFragment {
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.et_zjc)
    AppCompatEditText etZjc;
    @BindView(R.id.recover_password)
    ClearEditText recoverPassword;
    @BindView(R.id.hide_password)
    ImageView hidePassword;
    @BindView(R.id.recover_password2)
    ClearEditText recoverPassword2;
    @BindView(R.id.hide_password2)
    ImageView hidePassword2;
    @BindView(R.id.recover_name)
    ClearEditText recover_name;
    private boolean isShowPassword1 = false;
    private boolean isShowPassword2 = false;
    String action;
    public RecoverZhujiFragment (String action){
        this.action=action;
    }
    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover_zhuji, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        hidePassword.setImageResource(R.mipmap.icon_login_eye_down);
        recoverPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        hidePassword2.setImageResource(R.mipmap.icon_login_eye_down);
        recoverPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @OnClick({R.id.hide_password, R.id.hide_password2,R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hide_password:
                if (isShowPassword1) {
                    isShowPassword1 = false;
                    hidePassword.setImageResource(R.mipmap.icon_login_eye_down);
                    recoverPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    recoverPassword.setSelection(recoverPassword.getText().toString().length());
                } else {
                    isShowPassword1 = true;
                    hidePassword.setImageResource(R.mipmap.icon_login_eye_up);
                    recoverPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    recoverPassword.setSelection(recoverPassword.getText().toString().length());
                }
                break;
            case R.id.hide_password2:
                //再次确定密码 隐藏或显示
                if (isShowPassword2) {
                    isShowPassword2 = false;
                    hidePassword2.setImageResource(R.mipmap.icon_login_eye_down);
                    recoverPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    recoverPassword2.setSelection(recoverPassword2.getText().toString().length());
                } else {
                    isShowPassword2 = true;
                    hidePassword2.setImageResource(R.mipmap.icon_login_eye_up);
                    recoverPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    recoverPassword2.setSelection(recoverPassword2.getText().toString().length());
                }
                break;
            case R.id.btn_true:
                String password = recoverPassword.getText().toString();
                String password2 = recoverPassword2.getText().toString();
                String string_zjc=etZjc.getText().toString();
                if (!password.equals(password2)){
                    showMessage(getString(R.string.string_tip_create_password));
                    return;
                }
                if (TextUtils.isEmpty(string_zjc)){
                    showMessage(getString(R.string.string_mnemonic_not_empty));
                    return;
                }
                UserWalletBean select = dao.select(new UserWalletBean().setName(recover_name.getText().toString()));
                if (select!=null) {
                    showMessage("钱包已存在");
                    return;
                }
                requestLogin();
                break;
            default:
                break;
        }
    }

    @Autowired
    public Block block;
    @Autowired
    Dao dao;

    @Autowired
    TronBlockHandle blockHandle;
    private void requestLogin(){
        showLoading();
        String privKey = "";
        try {
            privKey = ByteArray.toHexString(BIP39.decode(etZjc.getText().toString(), "pass"));
            BlockWalletBean blockWalletBean = blockHandle.import_base(privKey, recoverPassword.getText().toString(), recover_name.getText().toString());
            if (blockWalletBean!=null) {
                if (action!=null) {
                    UserWalletBean select = dao.select(new UserWalletBean().setName(recover_name.getText().toString()));
                    if (select==null) {
                        dao.save(new UserWalletBean().setName(recover_name.getText().toString()).setAddress(WalletManager.getWallet(recover_name.getText().toString()).getAddress()));
                    }else{
                        dao.updata(select.setName(recover_name.getText().toString()).setAddress(WalletManager.getWallet(recover_name.getText().toString()).getAddress()));
                    }
                    getActivity().finish();
                    AppManager.getAppManager().killActivity(WalletManagementActivity.class);
                }else{
                    SpUtil.getInstance(getContext()).saveBoolean("is_getWallet",true);
                    SpUtil.getInstance(mContext).saveString("this_wallet_name",recover_name.getText().toString());
                    AppManager.getAppManager().killAll(RecoverActivity.class);
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
//                    dao.save(new UserWalletBean().setAddress(block.getWallet(recover_name.getText().toString(),recoverPassword.getText().toString()).getAddress()).setName(recover_name.getText().toString()));
                }
            }else{
                showMessage("导入失败，助记词错误");
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
            e.printStackTrace();
        }
//        boolean b = block.importWallet(new WalletBean().setRecoveryPhrase(etZjc.getText().toString()).setName(recover_name.getText().toString()), recoverPassword.getText().toString());

        hideLoading();
       /* if (b) {
            if (action!=null) {
                UserWalletBean select = dao.select(new UserWalletBean().setName(recover_name.getText().toString()));
                if (select==null) {
                    dao.save(new UserWalletBean().setName(recover_name.getText().toString()).setAddress(WalletManager.getWallet(recover_name.getText().toString()).getAddress()));
                }else{
                    dao.updata(select.setName(recover_name.getText().toString()).setAddress(WalletManager.getWallet(recover_name.getText().toString()).getAddress()));
                }
                getActivity().finish();
                AppManager.getAppManager().killActivity(WalletManagementActivity.class);
            }else{
                SpUtil.getInstance(getContext()).saveBoolean("is_getWallet",true);
                AppManager.getAppManager().killAll(RecoverActivity.class);
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                SpUtil.getInstance(mContext).saveString("this_wallet_name",recover_name.getText().toString());
                dao.save(new UserWalletBean().setAddress(block.getWallet(recover_name.getText().toString(),recoverPassword.getText().toString()).getAddress()).setName(recover_name.getText().toString()));
            }

        }else{
            showMessage(getString(R.string.string_login_fail));
        }*/

    }
}