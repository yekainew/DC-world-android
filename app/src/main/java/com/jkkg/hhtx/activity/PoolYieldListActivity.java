package com.jkkg.hhtx.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jkkg.hhtx.adapter.PoolProfitListAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.net.bean.UserBindAddressBean;
import com.jkkg.hhtx.net.bean.UserHoldIncomeBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.PageUtils;
import com.jkkg.hhtx.widget.PasswordDialog;
import com.jkkg.hhtx.widget.SweetAlertDialog;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.json.JSONUtil;
import lombok.experimental.Accessors;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * @author admin6
 * 矿池-收益列表
 */
public class PoolYieldListActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.yield_num)
    TextView yieldNum;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.yes_ti)
    TextView yesTi;
    @BindView(R.id.yes_yi)
    TextView yesYi;
    @BindView(R.id.ti_yue)
    Button tiYue;

    private PageUtils pageUtils;
    private PoolProfitListAdapter poolYieldListAdapter;
    private View emptyView;
    private SweetAlertDialog passwordAlertDialog;
    private String s;
    private BasePopupWindow languagePopupWindow;
    private String usable;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pool_yield_list;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_earning_list));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText("收益规则");
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipDialog();
            }
        });

        pageUtils = new PageUtils(noticeSmart);
        poolYieldListAdapter = new PoolProfitListAdapter(new ArrayList<>(), this,"");
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.item_empty, (ViewGroup) noticeRec.getParent(), false);
//        poolYieldListAdapter.setEmptyView(emptyView);
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(poolYieldListAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
//                request();
                incomeLog();
            }
        });
        tiYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoolYieldListActivity.this, IncomeWithdrawalActivity.class);
                intent.putExtra("date",usable);
                intent.putExtra("type","2");
                startActivityForResult(intent,303);
            }
        });
        incomeLog();
        income();

        yesti();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==303) {
            if (resultCode==101) {
                yesti();
                /*String setdata = data.getStringExtra("setdata");
                yesTi.setText("可提：" + setdata);
                String s = new BigDecimal(setdata).subtract(new BigDecimal(usable)).stripTrailingZeros().toPlainString();
                yesYi.setText("已提：" + s);*/
            }
        }
    }

    private void yesti() {
        Map<String, String> map = new HashedMap<String, String>() {
            {
                put("currency_name", "DC");
            }
        };
        MyApp.requestSend.sendData("app.wallet.wallet", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                WalletDCBean walletDCBean = WalletDCBean.newBean(WalletDCBean.class, message.getDate());
                usable = walletDCBean.getUsable();
                yesTi.setText("可提：" + usable);
                String s = new BigDecimal(PoolYieldListActivity.this.s).subtract(new BigDecimal(usable)).stripTrailingZeros().toPlainString();
                yesYi.setText("已提：" + s);
                return Message.ok();
            }
            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void income() {
        MyApp.requestSend.sendData("holdarea.method.income").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                if (message.getDate() != null) {
                    s = message.getDate().toString();
                    yieldNum.setText(message.getDate().toString());
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    private void incomeLog() {
        MyApp.requestSend.sendData("holdarea.method.income_log").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                List<UserHoldIncomeBean> o = gson.fromJson(message.getDate().toString(), new TypeToken<List<UserHoldIncomeBean>>() {
                }.getType());
                poolYieldListAdapter.setData(o,"");
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    private void showTipDialog() {
        passwordAlertDialog = null;
        passwordAlertDialog = new SweetAlertDialog.Builder(this)
                .setTitle("收益规则")
                .setMessage("购买后T+1开始计算收益，当日收益受前日算力排名影响；\n不取决于排名，推荐数量与社区算力变动同样影响当日收益。")
                .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}