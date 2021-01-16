package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.ExchangeAssetAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeBean1;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 星际演替-资产
 * <p>
 * 条目  item_exchange_asset
 */
public class ExchangeAssetActivity extends BaseActivity {

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    int page = 1;
    int pagenum = 200;
    @BindView(R.id.usdt_text)
    TextView usdtText;
    @BindView(R.id.dc_text)
    TextView dcText;
    private ExchangeAssetAdapter exchangeAssetAdapter;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange_asset;
    }
    @Autowired
    Dao dao;
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("记录");
        exchangeAssetAdapter = new ExchangeAssetAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(exchangeAssetAdapter);
        setData();

        AssetsBean usdt = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("USDT"));
        AssetsBean dc = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("DC"));


        if (usdt!=null&&dc!=null) {
            usdtText.setText(usdt.getNum().stripTrailingZeros().toPlainString());
            dcText.setText(dc.getNum().stripTrailingZeros().toPlainString());
        }

        exchangeAssetAdapter.setOnClickListener(new ExchangeAssetAdapter.OnClick() {
            @Override
            public void setOnClick(View view, ExchangeBean1 exchangeBean1, int code) {
                if (code==1) {
                    showMessage("进行中，请稍后再试");
                }else if (code==2){
                    showMessage("失败了，请查看原因");
                }else {
                    Intent intent = new Intent(getContext(), ExchangeAssetsDetailActivity.class);
                    intent.putExtra("exchangeBean1",exchangeBean1);
                    startActivity(intent);
                }
            }
        });
    }

    private void setData() {
        Map<String, String> map = new ArrayMap<>();
        map.put("pageNum", page + "");
        map.put("pageNum", pagenum + "");
        MyApp.requestSend.sendData("exchange.method.log").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<ExchangeBean1> exchangeBean1s = JSONArray.parseArray(message.getDate().toString(), ExchangeBean1.class);
                if (page == 1) {
                    exchangeAssetAdapter.upDate(exchangeBean1s);
                } else {
                    exchangeAssetAdapter.setDate(exchangeBean1s);
                }


                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
