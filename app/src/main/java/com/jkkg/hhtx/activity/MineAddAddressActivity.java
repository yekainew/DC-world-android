package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.AssetsBookAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.AssetsBookAddBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AddressBookBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

/**
 * 添加地址
 * <p>
 * 钱包地址item  item_wallet_address
 */
public class MineAddAddressActivity extends BaseActivity {

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
    @BindView(R.id.name)
    ClearEditText name;
    @BindView(R.id.beizhu)
    ClearEditText beizhu;
    @BindView(R.id.add_address)
    ImageView addAddress;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_create)
    Button btnCreate;
    private AssetsBookAdapter assetsBookAdapter;
    List<AssetsBookAddBean> list = new ArrayList<>();
    int type = 0;
    private Intent intent;
    private String action;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_add_address;
    }
    int p=0;
    int id;
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);

        intent = getIntent();
        action = intent.getAction();
        assetsBookAdapter = new AssetsBookAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(assetsBookAdapter);


        if (action.equals("1")) {
            toolbarTitle.setText(R.string.string_edittext_address);
            AddressBookBean bean = (AddressBookBean) intent.getSerializableExtra("bean");
            Gson gson = new Gson();
            String assets_json = bean.getAssets_json();

            list = gson.fromJson(assets_json, new TypeToken<List<AssetsBookAddBean>>() {
            }.getType());

            assetsBookAdapter.setDate(list);
            name.setText(bean.getContacts());
            beizhu.setText(bean.getBei_zhu());

            AddressBookBean select = dao.select(new AddressBookBean().setContacts(bean.getContacts()));
            if (select!=null) {
                id = select.getId();
            }
            p=1;

        } else {
            toolbarTitle.setText(R.string.string_new_address);
            p=0;
        }


    }

    @Autowired
    Dao dao;


    @OnClick({R.id.add_address, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_address:
                //添加地址
                startActivityForResult(new Intent(this, MineAddWalletAddressActivity.class), 101);
                break;
            case R.id.btn_create:
                //完成

                String s1 = name.getText().toString();
                if (StrUtil.isBlank(s1)) {
                    showMessage("请输入名称");
                    return;
                }

                if (list.size() == 0) {
                    showMessage("请添加地址");
                    return;
                }


                Gson gson = new Gson();
                String s = gson.toJson(list);
                if (p==1) {
                    AddressBookBean select = dao.select(new AddressBookBean().setId(id));
                    select.setAssets_json(s).setWallet_bottom("TRX").setWallet_name(Constants.getWalletName()).setContacts(name.getText().toString()).setBei_zhu(beizhu.getText().toString());
                    dao.updata(select);
                }else{
                    dao.save(new AddressBookBean().setAssets_json(s).setWallet_bottom("TRX").setWallet_name(Constants.getWalletName()).setContacts(name.getText().toString()).setBei_zhu(beizhu.getText().toString()));
                }
                setResult(101, intent.putExtra("type", "1"));
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Gson gson = new Gson();
            AssetsBookAddBean gson2 = gson.fromJson(data.getStringExtra("gson"), AssetsBookAddBean.class);

            list.add(gson2);
            if (type == 0) {
                assetsBookAdapter.setDate(list);
                type++;
            } else {
                assetsBookAdapter.setDate(list);
            }
        }
    }
}
