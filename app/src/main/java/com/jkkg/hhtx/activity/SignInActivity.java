package com.jkkg.hhtx.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.SigninListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.net.bean.CheckTheCheckInOnTheDayBean;
import com.jkkg.hhtx.net.bean.TimeDayBean;
import com.jkkg.hhtx.widget.SweetAlertDialog;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.widget.QuickPopup;

import static com.github.promeg.pinyinhelper.Pinyin.isChinese;

/**
 * 签到
 */
public class SignInActivity extends BaseActivity {
    QuickPopup quickPopup;
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
    @BindView(R.id.my_yield)
    TextView myYield;
    @BindView(R.id.day_num)
    TextView dayNum;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.relative7)
    RecyclerView relative7;

    List<TimeDayBean> list=new ArrayList<>();
    private SigninListAdapter signinListAdapter;
    private CheckTheCheckInOnTheDayBean checkTheCheckInOnTheDayBean;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_sign));


        signinListAdapter = new SigninListAdapter(SignInActivity.this,new ArrayList<>());
        relative7.setLayoutManager(new GridLayoutManager(SignInActivity.this,7));
        relative7.setAdapter(signinListAdapter);
        isSignin();


        signinListAdapter.setOnClickListener(new SigninListAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position) {
                new SweetAlertDialog.Builder(SignInActivity.this)
                        .setTitle(getString(R.string.string_bu_sign))
                        .setMessage(getString(R.string.string_buqian_need))
                        .setNegativeButton(getString(R.string.string_sweet_cancle), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.string_sweet_confirm), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                buqian(position+1);
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }

    private void buqian(int p) {
        Map<String,String> map=new HashMap<String, String>(){
            {
                put("index",p+"");
            }
        };

        MyApp.requestSend.sendData("checkin.app.method.reissue", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(getString(R.string.string_buqian_success));
                if (list!=null) {
                    list.clear();
                }
                isSignin();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }


    @OnClick({R.id.btn_ture,R.id.my_yield})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_yield:
                Intent intent = new Intent(this, SignlnIncomeActivity.class);
                if (checkTheCheckInOnTheDayBean!=null) {
                    intent.putExtra("all",checkTheCheckInOnTheDayBean.getTotal_revenue().stripTrailingZeros().toPlainString());
                    startActivity(intent);
                }
                break;
            case R.id.btn_ture:
                qiandao();
                break;
        }

    }

    private void qiandao() {
        MyApp.requestSend.sendData("checkin.app.method.signIn").main(new NetCall.Call() {

            private TextView tv_pop_coin;

            @Override
            public Message ok(Message message) {
                showMessage(getString(R.string.string_sign_success));
                String s = message.getDate().toString();
                if (list!=null) {
                    list.clear();
                }
                isSignin();
                quickPopup = QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.layout_signin_pop)
                        .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                                .backpressEnable(true)
                                .outSideTouchable(false)
                                .withShowAnimation(AnimationHelper.asAnimation()
                                        .withAlpha(AlphaConfig.IN)
                                        .toShow())
                                .withDismissAnimation(AnimationHelper.asAnimation()
                                        .withAlpha(AlphaConfig.OUT)
                                        .toDismiss())
                                .withClick(R.id.bt_pop, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        quickPopup.dismiss();
                                    }
                                })).show();
                tv_pop_coin = quickPopup.findViewById(R.id.tv_pop_coin);
                tv_pop_coin.setText(s);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void isSignin() {
        MyApp.requestSend.sendData("checkin.app.method.checkTheCheckInOnTheDay").main(new NetCall.Call() {

            @Override
            public Message ok(Message message) {
                checkTheCheckInOnTheDayBean = CheckTheCheckInOnTheDayBean.newBean(CheckTheCheckInOnTheDayBean.class, message.getDate().toString());
                String check_in_statistics = checkTheCheckInOnTheDayBean.getCheck_in_statistics();
                char[] chars = check_in_statistics.toCharArray();
                for (int i = 1; i < chars.length; i++) {
                    Log.d("sssss",chars[i]+"");
                    // TODO: 2020/11/16 未更改

                    DateTime offset = DateUtil.offset(checkTheCheckInOnTheDayBean.getStartTime(), DateField.DAY_OF_YEAR, i-1);
                    String week = DateUtil.dayOfWeekEnum(offset).toChinese(getString(R.string.string_zhou));
                    list.add(new TimeDayBean(week,offset,chars[i]));
                }

                dayNum.setText(checkTheCheckInOnTheDayBean.getContinuous_check_in()+"");

                signinListAdapter.setData(list);
                Integer status = checkTheCheckInOnTheDayBean.getStatus();
                if (status==1) {//已签到
                    btnTure.setBackgroundResource(R.drawable.signin_shape);
                    btnTure.setEnabled(false);
                    btnTure.setText(getString(R.string.string_sign_day));
                }else{//未签到立即签到
                    btnTure.setBackgroundResource(R.drawable.signin_shape1);
                    btnTure.setEnabled(true);
                    btnTure.setText(getString(R.string.string_sign_liji));
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

}
