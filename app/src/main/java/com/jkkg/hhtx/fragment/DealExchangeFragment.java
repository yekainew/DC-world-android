package com.jkkg.hhtx.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.PoolListActivity;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.SweetAlertDialog;
import com.mugui.base.client.net.bean.Message;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.widget.QuickPopup;

/**
 * 交易-兑换
 * 高级设置    pop_deal_gaojiset
 * 点击兑换    pop_deal_true_exchange
 * 选择代币    pop_deal_choose_daibi
 * 高级设置 滑点容忍度 item_deal_gaojiset_hdrrd
 *         点击情况下 背景@mipmap/new_icon_shou_text_hdrrd 字体@color/bg_pic
 *         无事件     背景@mipmap/new_icon_text_hdrrd 字体@color/white
 */
public class DealExchangeFragment extends BaseFragment {

    @BindView(R.id.edit_num)
    ClearEditText editNum;
    @BindView(R.id.choose_bi)
    TextView chooseBi;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.gaoji_set)
    TextView gaojiSet;
    @BindView(R.id.edit_num2)
    ClearEditText editNum2;
    @BindView(R.id.choose_daibi)
    TextView chooseDaibi;
    @BindView(R.id.add_jieshou)
    TextView addJieshou;
    @BindView(R.id.delete_jieshou)
    TextView deleteJieshou;
    @BindView(R.id.jieshou)
    ClearEditText jieshou;
    @BindView(R.id.tongzheng)
    LinearLayout tongzheng;
    @BindView(R.id.show_daibi)
    TextView showDaibi;
    @BindView(R.id.img_zhe)
    ImageView imgZhe;
    @BindView(R.id.why_min)
    ImageView whyMin;
    @BindView(R.id.min_jieshou)
    TextView minJieshou;
    @BindView(R.id.why_price)
    ImageView whyPrice;
    @BindView(R.id.yingxiang_price)
    TextView yingxiangPrice;
    @BindView(R.id.why_fee)
    ImageView whyFee;
    @BindView(R.id.fee_trx)
    TextView feeTrx;
    @BindView(R.id.show_tongzheng)
    LinearLayout showTongzheng;
    @BindView(R.id.btn_choose_daibi)
    Button btnChooseDaibi;
    @BindView(R.id.btn_jieshou)
    Button btnJieshou;
    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.relative_add)
    RelativeLayout relativeAdd;
    @BindView(R.id.relative_delete)
    RelativeLayout relativeDelete;

    private SweetAlertDialog passwordAlertDialog;
    QuickPopup quickPopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deal_exchange, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        initDate();
    }

    private void initDate() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick({R.id.choose_bi, R.id.gaoji_set, R.id.choose_daibi, R.id.add_jieshou, R.id.delete_jieshou, R.id.why_min, R.id.why_price, R.id.why_fee, R.id.btn_choose_daibi, R.id.btn_jieshou, R.id.btn_ture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_bi:
                //选择币种
                setChooseDaibi();
                break;
            case R.id.gaoji_set:
                //高级设置
                setGaoji();
                break;
            case R.id.choose_daibi:
                //选择代币
                setChooseDaibi();
                break;
            case R.id.add_jieshou:
                //添加接收方
                relativeAdd.setVisibility(View.GONE);
                relativeDelete.setVisibility(View.VISIBLE);
                break;
            case R.id.delete_jieshou:
                //移除接收方
                relativeAdd.setVisibility(View.VISIBLE);
                relativeDelete.setVisibility(View.GONE);
                break;
            case R.id.why_min:
                //解释最小接收额
                passwordAlertDialog = null;
                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.string_sweet_title))
                        .setMessage("如果交易确认之前出现了不利的大幅价格变动，您的交易将退回。")
                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.why_price:
                //解释价格
                passwordAlertDialog = null;
                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.string_sweet_title))
                        .setMessage("受交易规模影响，市价与预期价格之间的差值。")
                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.why_fee:
                //解释费用
                passwordAlertDialog = null;
                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.string_sweet_title))
                        .setMessage("流动性提供者将获得每笔交易的0.0%作为协议奖励。")
                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_choose_daibi:
                //选择代币
                break;
            case R.id.btn_jieshou:
                //填入接收方
                break;
            case R.id.btn_ture:
                //兑换
                trueExchange();
                break;
            default:
                break;
        }
    }

    private void setGaoji(){
        quickPopup = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.pop_deal_gaojiset)
                .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.IN)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.OUT)
                                .toDismiss())
                        .withClick(R.id.why_huadian, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //解释容忍度
                                passwordAlertDialog = null;
                                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                                        .setTitle(getString(R.string.string_sweet_title))
                                        .setMessage("如果价格变动幅度超过此百分比，您的交易将退回。")
                                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                                            @Override
                                            public void onClick(Dialog dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();

                            }
                        }).withClick(R.id.why_time, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //解释截止时间
                                passwordAlertDialog = null;
                                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                                        .setTitle(getString(R.string.string_sweet_title))
                                        .setMessage("若您的交易超过此时间仍处于待确认状态，您的交易将退回。")
                                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                                            @Override
                                            public void onClick(Dialog dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();

                            }
                        }).withClick(R.id.deal_record, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //取消
                                quickPopup.dismiss();

                            }
                        }).withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //确定
                                quickPopup.dismiss();

                            }
                        })
                ).show();
    }


    private void trueExchange() {
        quickPopup = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.pop_deal_true_exchange)
                .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.IN)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.OUT)
                                .toDismiss())
                        .withClick(R.id.deal_record, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //取消
                                quickPopup.dismiss();
                            }
                        }).withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //确定
                                quickPopup.dismiss();
                            }
                        })
                ).show();
    }

    private void setChooseDaibi() {
        quickPopup = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.pop_deal_choose_daibi)
                .config(new QuickPopupConfig().gravity(Gravity.CENTER)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.IN)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withAlpha(AlphaConfig.OUT)
                                .toDismiss())
                        .withClick(R.id.deal_record, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //取消
                                quickPopup.dismiss();
                            }
                        }).withClick(R.id.img_search, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //搜索

                            }
                        })
                ).show();
    }

}
