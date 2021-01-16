package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.AddressTwoAdapter;
import com.jkkg.hhtx.adapter.MineAddressBookAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.AssetsBookAddBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AddressBookBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.mugui.base.base.Autowired;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地址簿
 * 条目  item_address
 */
public class MineAddressBookActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_create)
    Button btnCreate;
    private MineAddressBookAdapter mineAddressBookAdapter;
    private Intent intent;
    private List<AddressBookBean> addressBookBeans;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_address_book;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        intent = getIntent();

        String action = intent.getAction();
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_address_book);


        mineAddressBookAdapter = new MineAddressBookAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mineAddressBookAdapter);
        addressBookBeans = dao.selectList(new AddressBookBean().setWallet_name(Constants.getWalletName()));

        mineAddressBookAdapter.setDate(addressBookBeans);

        mineAddressBookAdapter.setOnClickListener(new MineAddressBookAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, AddressBookBean bookBean) {
                if (action.equals("1")) {
                    String assets_json = bookBean.getAssets_json();

                    Gson gson = new Gson();
                    List<AssetsBookAddBean> o = gson.fromJson(assets_json, new TypeToken<List<AssetsBookAddBean>>() {
                    }.getType());


                    if (o.size()==1) {
                        intent.putExtra("address",o.get(0).getAddress());
                        setResult(404, intent);
                        finish();
                    }else{
                        startarea1Pop(o);
                    }
                }else{
                    Intent intent = new Intent(MineAddressBookActivity.this, MineAddAddressActivity.class);
                    intent.putExtra("bean",addressBookBeans.get(position));
                    intent.setAction("1");
                    startActivityForResult(intent,303);
                }
            }
        });

    }

    @OnClick(R.id.btn_create)
    public void onViewClicked() {
        //添加地址
        startActivityForResult(new Intent(this,MineAddAddressActivity.class).setAction("2"),303);
    }

    @Autowired
    Dao dao;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==101) {
            String type = data.getStringExtra("type");
            if (type.equals("1")) {
                addressBookBeans = dao.selectList(new AddressBookBean().setWallet_name(Constants.getWalletName()));
                    mineAddressBookAdapter.setDate(addressBookBeans);
            }
        }
    }


    private void startarea1Pop( List<AssetsBookAddBean> o) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.assets_two_pop, null);
        RecyclerView assets_two_rec = inflate.findViewById(R.id.assets_two_rec);

        AddressTwoAdapter addressTwoAdapter = new AddressTwoAdapter(getContext(), o);
        assets_two_rec.setLayoutManager(new LinearLayoutManager(this));
        assets_two_rec.setAdapter(addressTwoAdapter);
        PopupWindow popupWindow = new PopupWindow(inflate);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int height = defaultDisplay.getHeight();
        int i = height / 3;
        popupWindow.setHeight(i);
        popupWindow.showAsDropDown(findViewById(R.id.address_main), Gravity.CENTER, 0, 0);

        addressTwoAdapter.setOnClickListener(new AddressTwoAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, String name) {
                intent.putExtra("address",name);
                setResult(404,intent);
                finish();
            }
        });
    }

}
