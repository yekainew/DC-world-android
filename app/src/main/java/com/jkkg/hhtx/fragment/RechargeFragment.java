package com.jkkg.hhtx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 * 充币
 *
 * @author admin6
 */
public class RechargeFragment extends BaseFragment {

    @BindView(R.id.img_qr_code)
    ImageView imgQrCode;
    @BindView(R.id.recharge_address)
    TextView rechargeAddress;
    @BindView(R.id.btn_copy)
    Button btnCopy;

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recharge, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_copy)
    public void onViewClicked() {
        //点击复制地址
//        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData mClipData = ClipData.newPlainText("inviteCode", rechargeAddress.getText().toString());
//        cm.setPrimaryClip(mClipData);
//        ArmsUtils.snackbarText(getString(R.string.string_text_copy_seccess));
    }
}