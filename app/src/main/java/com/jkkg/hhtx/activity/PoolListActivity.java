package com.jkkg.hhtx.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.PoolListAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.PoolListBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.CHSUtils;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.SweetAlertDialog;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.widget.QuickPopup;

/**
 * 矿机列表
 *
 * @author admin6
 */
public class PoolListActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    private SweetAlertDialog passwordAlertDialog;
    private PoolListAdapter poolListAdapter;
    private List<PoolListBean> o;
    QuickPopup quickPopup;
    private BigDecimal num;
    private String contract_address;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pool_list;
    }
    @Autowired
    Dao dao;

    @Autowired
    Block block;
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_tab_pool_list));
        toolbarImageRight.setVisibility(View.VISIBLE);
        toolbarImageRight.setImageResource(R.mipmap.icon_why);

        CoinBean dc1 = dao.select(new CoinBean().setSymbol("DC"));

        contract_address = dc1.getContract_address();

        AssetsBean dc = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol("DC"));
        num = dc.getNum();
        poolListAdapter = new PoolListAdapter(this, new ArrayList<>());
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(poolListAdapter);
        toolbarImageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showTipDialog();
            }
        });

        list();

        poolListAdapter.setOnClickListener(new PoolListAdapter.OnClick() {
            @Override
            public void setOnClick(View v, int position) {
                // TODO: 2020-10-21 跳转详情
                PoolListBean poolListBean = o.get(position);

                Intent intent = new Intent(PoolListActivity.this, PoolDetailsActivity.class);
                intent.putExtra("poolListBean",poolListBean);
                startActivity(intent);
            }
        });

        poolListAdapter.setOnAddClickListener(new PoolListAdapter.OnAddClick() {

            private ClearEditText edit_psw;
            private TextView sl_num;
            private TextView dc_num;
            private ClearEditText edit_num;

            @Override
            public void setOnAddClick(View v, int position) {

                PoolListBean poolListBean = o.get(position);
                String invest_address = poolListBean.getInvest_address();
                // TODO: 2020-10-21 增加
                 quickPopup = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.poppool_add)
                .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.IN)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.OUT)
                                .toDismiss())

                        .withClick(R.id.all_dc, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               //全部dc
                                edit_num.setText(num.stripTrailingZeros().toPlainString());
                            }
                        })
                         .withClick(R.id.dialog_left_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //取消
                                quickPopup.dismiss();

                            }
                        }).withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (StrUtil.isBlank(edit_psw.getText().toString())) {
                                    showMessage(getString(R.string.string_input_pwd));
                                    return;
                                }
                                if (StrUtil.isBlank(edit_num.getText().toString())) {
                                    showMessage(getString(R.string.string_money_no_null));
                                    return;
                                }

                                BigDecimal bigDecimal = new BigDecimal(edit_num.getText().toString());
                                ThreadUtil.execAsync(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message tron = block.tron(invest_address, contract_address, bigDecimal, SpUtil.getInstance(PoolListActivity.this).getString("this_wallet_name",""), edit_psw.getText().toString());
                                        if (tron.getType()== Message.SUCCESS) {
                                            showMessage(getString(R.string.string_buy_success));

                                            hideLoading();
                                            finish();
                                        }else{
                                            showMessage(tron.getMsg());
                                            hideLoading();
                                        }

                                    }
                                });

                                //确定
                                quickPopup.dismiss();

                            }
                        })).show();

                 //输入dc数量
                edit_num = quickPopup.findViewById(R.id.edit_num);
                //dc总数量
                dc_num = quickPopup.findViewById(R.id.dc_num);

                edit_psw = quickPopup.findViewById(R.id.edit_psw);

                dc_num.setText(num.stripTrailingZeros().toPlainString());
                //算力数量
                sl_num = quickPopup.findViewById(R.id.sl_num);

                edit_num.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (Other.isDouble(s.toString())) {
                            String ghs = CHSUtils.ghs(new BigDecimal(s.toString()));
                            sl_num.setText(ghs+"GH/S");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });


    }



    private void list() {
        MyApp.requestSend.sendData("holdarea.method.list").main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                o = gson.fromJson(message.getDate().toString(), new TypeToken<List<PoolListBean>>() {
                }.getType());
                poolListAdapter.setData(o);
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
                .setTitle(getString(R.string.string_sweet_title))
                .setMessage(getString(R.string.string_tip_pool))
                .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}