package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.DayItemAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.LockCoinToEarnInterestBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 锁仓
 * 锁仓记录 LockupRecordActivity
 */
public class LockupActivity extends BaseActivity {

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
    @BindView(R.id.lockup_dc)
    TextView lockupDc;
    @BindView(R.id.lockup_bfb)
    TextView lockupBfb;
    @BindView(R.id.lockup_text)
    TextView lockupText;
    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.dc_num)
    TextView dcNum;
    @BindView(R.id.dc_name)
    TextView dc_name;
    @BindView(R.id.text_day)
    TextView textDay;
    @BindView(R.id.lockup_day)
    RelativeLayout lockupDay;
    @BindView(R.id.yeild_num)
    TextView yeildNum;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.edit_pad)
    ClearEditText editPad;
    @BindView(R.id.bizhongmingcheng)
    TextView bizhongmingcheng;
    private LockCoinToEarnInterestBean lockCoinToEarnInterestBean;
    List<LockCoinToEarnInterestBean> lockCoinToEarnInterestBeans = new ArrayList<>();

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_lockup;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTitle.setText("存币生息");
        toolbarTvRight.setText(getString(R.string.string_lockup_recoed));
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LockupActivity.this, LockupRecordActivity.class));
            }
        });
        Intent intent = getIntent();
        ArrayList financesListBean = (ArrayList) intent.getSerializableExtra("financesListBean");
        for (Object o : financesListBean) {
            lockCoinToEarnInterestBean = LockCoinToEarnInterestBean.newBean(LockCoinToEarnInterestBean.class, new JSONObject((Map<String, Object>) o));
            lockCoinToEarnInterestBeans.add(lockCoinToEarnInterestBean);
        }

        lockCoinToEarnInterestBean = lockCoinToEarnInterestBeans.get(0);
        textDay.setText(lockCoinToEarnInterestBean.getHeaven() + getString(R.string.string_day));
        BigDecimal income = lockCoinToEarnInterestBean.getIncome();
        yeildNum.setText("0.00" + lockCoinToEarnInterestBean.getCurrency_name());
        BigDecimal multiply = income.multiply(new BigDecimal("100"));
        lockupBfb.setText(multiply.stripTrailingZeros().toPlainString() + "%");
        lockupDc.setText(lockCoinToEarnInterestBean.getTotal_investmentSum().stripTrailingZeros().toPlainString());
        bizhongmingcheng.setText(lockCoinToEarnInterestBean.getCurrency_name());

        AssetsBean select = dao.select(new AssetsBean().setSymbol(this.lockCoinToEarnInterestBean.getCurrency_name()).setWallet_name(Constants.getWalletName()));

        if (select != null) {
            dcNum.setText(select.getNum().stripTrailingZeros().toPlainString());
            dc_name.setText(this.lockCoinToEarnInterestBean.getCurrency_name());
        } else {
            dcNum.setText("0");
            dc_name.setText("");
        }

        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Other.isDouble(s.toString())) {
                    BigDecimal multiply1 = new BigDecimal(s.toString()).multiply(lockCoinToEarnInterestBean.getIncome());
                    yeildNum.setText(multiply1.stripTrailingZeros().toPlainString() + lockCoinToEarnInterestBean.getCurrency_name());
                } else {
                    yeildNum.setText("0.00" + lockCoinToEarnInterestBean.getCurrency_name());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Autowired
    Block block;

    @Autowired
    Dao dao;


    @OnClick({R.id.lockup_day, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lockup_day:
                startarea1Pop();
                break;
            case R.id.btn_ture:
                showLoading();
                String s = editNum.getText().toString();
                String s1 = dcNum.getText().toString();

                if (s.compareTo(s1) > 0) {
                    showMessage("金额不足");
                    hideLoading();
                    return;
                }

                ThreadUtil.execAsync(new Runnable() {
                    @Override
                    public void run() {
                        Message tron = block.tron(lockCoinToEarnInterestBean.getAddress(), lockCoinToEarnInterestBean.getToken(), new BigDecimal(s), Constants.getWalletName(), editPad.getText().toString());
                        if (tron.getType() == Message.SUCCESS) {
                            lockcoinsave(tron.getDate().toString());
                        } else {
                            showMessage(tron.getMsg());
                            hideLoading();
                        }

                    }
                });
                break;
        }
    }

    private void lockcoinsave(String date) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("lock_coin_to_earn_interest_id", lockCoinToEarnInterestBean.getLock_coin_to_earn_interest_id() + "");
                put("hash", date);
            }
        };
        MyApp.requestSend.sendData("lockcoin.app.method.save", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(message.getMsg());
                finish();
                hideLoading();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                hideLoading();
                return Message.ok();
            }
        });
    }

    private void startarea1Pop() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.popup_lockup_day, null);
        RecyclerView day_rec = inflate.findViewById(R.id.day_rec);

        DayItemAdapter dayItemAdapter = new DayItemAdapter(this, lockCoinToEarnInterestBeans);
        day_rec.setLayoutManager(new LinearLayoutManager(this));
        day_rec.setAdapter(dayItemAdapter);

        PopupWindow popupWindow = new PopupWindow(inflate);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(600);
        popupWindow.showAsDropDown(findViewById(R.id.lockup_main), Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        dayItemAdapter.setOnClickListener(new DayItemAdapter.OnClick() {
            @Override
            public void setOnClick(int position, View view) {
                lockCoinToEarnInterestBean = lockCoinToEarnInterestBeans.get(position);
                textDay.setText(lockCoinToEarnInterestBean.getHeaven() + getString(R.string.string_day));
                BigDecimal income = lockCoinToEarnInterestBean.getIncome();
                BigDecimal multiply = income.multiply(new BigDecimal("100"));
                lockupBfb.setText(multiply.stripTrailingZeros().toPlainString() + "%");
                lockupDc.setText(lockCoinToEarnInterestBean.getTotal_investmentSum().stripTrailingZeros().toPlainString());

                popupWindow.dismiss();
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
