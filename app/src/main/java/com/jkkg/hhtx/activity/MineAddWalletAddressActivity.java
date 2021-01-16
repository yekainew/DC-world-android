package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.widget.ClearEditText;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

/**
 * 添加钱包地址
 */
public class MineAddWalletAddressActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_wallet)
    RelativeLayout chooseWallet;
    @BindView(R.id.extract_phone1)
    ClearEditText extractPhone1;
    @BindView(R.id.extract_cramar)
    ImageView extractCramar;
    @BindView(R.id.beizhu)
    ClearEditText beizhu;
    @BindView(R.id.btn_create)
    Button btnCreate;
    private Intent intent;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_add_wallet_address;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_add_wallet_address);
        intent = getIntent();

    }


    @OnClick({R.id.choose_wallet, R.id.extract_cramar, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_wallet:

                break;
            case R.id.extract_cramar:
                Intent intent = new Intent(this, ZxingActivity.class);
                intent.setAction("2");
                startActivityForResult(intent,121);
                break;
            case R.id.btn_create:
                String s = extractPhone1.getText().toString();
                String s1 = beizhu.getText().toString();

                if (StrUtil.isBlank(s)) {
                    showMessage("请输入钱包地址");
                    return;
                }
                Map<String,String> map=new ArrayMap<>();
                map.put("address",s);
                map.put("remarks",s1);
                map.put("wallet_bottom","Tron");
                Gson gson = new Gson();
                String s2 = gson.toJson(map);

                setResult(RESULT_OK, this.intent.putExtra("gson",s2));
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==121) {
            if (resultCode==RESULT_OK) {
                String result = data.getStringExtra("result");
                extractPhone1.setText(result);
            }
        }
    }
}
