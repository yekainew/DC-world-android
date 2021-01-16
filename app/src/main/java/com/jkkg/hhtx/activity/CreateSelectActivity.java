package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;


/**
 * Description:
 * Created by ccw on 09/15/2020 09:39
 * //选择备份
 * Email:chencw0715@163.com
 */
public class CreateSelectActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.femle)
    RadioButton femle;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.select_dc_psw)
    ClearEditText selectDcPsw;
    int i=1;
    private String wallet_name;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        wallet_name = intent.getStringExtra("wallet_name");
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_safe_wallet);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.male) {
                    Toast.makeText(getApplicationContext(), getString(R.string.string_select_zhujici), Toast.LENGTH_SHORT).show();
                    i=0;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.string_select_siyao), Toast.LENGTH_SHORT).show();
                    i=1;
                }
            }
        });

    }

    @Autowired
    Block block;

    @OnClick(R.id.btn_true)
    public void onViewClicked() {

        if (StrUtil.isBlank(selectDcPsw.getText().toString())) {
            showMessage(getString(R.string.string_password_no_null));
            return;
        }
        if (StrUtil.isBlank(wallet_name)) {
            wallet_name= Constants.getWalletName();
        }
        if (i==0) {
            WalletBean wallet = block.getWallet(wallet_name,selectDcPsw.getText().toString());
            if (wallet!=null) {
                Intent intent = new Intent(this, CreateSelectHelpActivity.class);
                intent.putExtra("qianbao",wallet);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else {
                showMessage(getString(R.string.string_password_fail));
            }

        }else{
            WalletBean wallet = block.getWallet(wallet_name,selectDcPsw.getText().toString());
            if (wallet!=null) {
                Intent intent = new Intent(this, CreateSelectSiAckActivity.class);
                intent.putExtra("data",wallet);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else {
                showMessage(getString(R.string.string_password_fail));
            }
        }


    }

}


