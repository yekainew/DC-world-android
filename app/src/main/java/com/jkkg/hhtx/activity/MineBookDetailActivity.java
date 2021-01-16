package com.jkkg.hhtx.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeBean;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 区块交易详情
 */
public class MineBookDetailActivity extends BaseActivity {
    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bookdetail_title)
    TextView bookdetailTitle;
    @BindView(R.id.bookdetail_lx)
    TextView bookdetailLx;
    @BindView(R.id.bookdetail_dizhi1)
    TextView bookdetailDizhi1;
    @BindView(R.id.bookdetail_mydizhi1)
    TextView bookdetailMydizhi1;
    @BindView(R.id.bookdetail_kgf)
    TextView bookdetailKgf;
    @BindView(R.id.bookdetail_zc)
    TextView bookdetailZc;
    @BindView(R.id.bookdetail_timer)
    TextView bookdetailTimer;
    @BindView(R.id.bookdetail_todizhi1)
    TextView bookdetail_todizhi1;
    @BindView(R.id.bookdetail_hash)
    TextView bookdetail_hash;
    @BindView(R.id.bookdetail_tophone1)
    TextView bookdetail_tophone1;
    @BindView(R.id.exchange_amount_num)
    TextView exchange_amount_num;
    @BindView(R.id.exchange_huoqu)
    RelativeLayout exchange_huoqu;
    @BindView(R.id.exchange_huoqu_num)
    TextView exchange_huoqu_num;
    @BindView(R.id.bookdetail_hash1)
    TextView bookdetail_hash1;
    @BindView(R.id.ru_hash1)
    TextView ru_hash1;
    @BindView(R.id.tophone_lin)
    RelativeLayout tophone_lin;
    @BindView(R.id.exchange_amount)
    RelativeLayout exchange_amount;
    @BindView(R.id.ru_hash)
    RelativeLayout ru_hash;
    @BindView(R.id.quxian_address)
    RelativeLayout quxian_address;
    @BindView(R.id.laiyuan_address)
    RelativeLayout laiyuan_address;
    @BindView(R.id.heyue_adderss)
    RelativeLayout heyue_adderss;
    @BindView(R.id.bookdetail_phone_view)
    View bookdetail_phone_view;
    @BindView(R.id.view_huoqu)
    View view_huoqu;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.heyue_address_view)
    View heyue_address_view;
    @BindView(R.id.laiyuan_address_view)
    View laiyuan_address_view;
    @BindView(R.id.quxiang_address_view)
    View quxiang_address_view;
    @BindView(R.id.ru_hash_view)
    View ru_hash_view;
    @BindView(R.id.exchange_amount_text)
    TextView exchangeAmountText;
    @BindView(R.id.exchange_huoqu_text)
    TextView exchangeHuoquText;
    @BindView(R.id.bookdetail_dizhi)
    TextView bookdetailDizhi;
    @BindView(R.id.bookdetail_dizhi1_copy)
    ImageView bookdetailDizhi1Copy;
    @BindView(R.id.bookdetail_mydizhi)
    TextView bookdetailMydizhi;
    @BindView(R.id.bookdetail_mydizhi1_copy)
    ImageView bookdetailMydizhi1Copy;
    @BindView(R.id.bookdetail_tophone)
    TextView bookdetailTophone;
    @BindView(R.id.bookdetail_todizhi)
    TextView bookdetailTodizhi;
    @BindView(R.id.bookdetail_todizhi1_copy)
    ImageView bookdetailTodizhi1Copy;
    @BindView(R.id.bookdetail_hash_copy)
    ImageView bookdetailHashCopy;
    @BindView(R.id.ru_hash2)
    TextView ruHash2;

    private ClipboardManager cm;
    private ClipData mClipData;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {

        return R.layout.activity_mine_book_detail;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_ransaction_etails));


        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case 0:
                AssetsLogBean tbcb = (AssetsLogBean) intent.getSerializableExtra("list");
                bookdetailLx.setText(getString(R.string.string_mine_charge_money));
                bookdetailTitle.setText("+" + tbcb.getNum() + tbcb.getSymbol());
                bookdetailTimer.setText(DateFormatUtils.format(tbcb.getTime(), "yyyy-MM-dd HH:mm:ss"));
                bookdetailDizhi1.setText(tbcb.getContract());
                bookdetailMydizhi1.setText(tbcb.getFrom());
                if (tbcb.getDetail() != null) {
                    bookdetailKgf.setText(tbcb.getDetail());
                }
                bookdetailZc.setText(tbcb.getNum().stripTrailingZeros().toPlainString());
                bookdetail_todizhi1.setText(tbcb.getTo());
                bookdetail_hash.setText(tbcb.getHash());
                break;
            case 1:
                AssetsLogBean tbcb1 = (AssetsLogBean) intent.getSerializableExtra("list");
                bookdetailLx.setText(getString(R.string.string_mine_mention_money));
                bookdetailTitle.setText("-" + tbcb1.getNum() + tbcb1.getSymbol());
                bookdetailTimer.setText(DateFormatUtils.format(tbcb1.getTime(), "yyyy-MM-dd HH:mm:ss"));
                bookdetailDizhi1.setText(tbcb1.getContract());
                bookdetailMydizhi1.setText(tbcb1.getFrom());
                if (tbcb1.getDetail() != null) {
                    bookdetailKgf.setText(tbcb1.getDetail());
                }
                bookdetailZc.setText(tbcb1.getNum().stripTrailingZeros().toPlainString());
                bookdetail_todizhi1.setText(tbcb1.getTo());
                bookdetail_hash.setText(tbcb1.getHash());
                break;
            case 2:
                bookdetailLx.setText(getString(R.string.string_mine_transfer));
                AssetsLogBean zz = (AssetsLogBean) intent.getSerializableExtra("list");
                bookdetailTitle.setText("-" + zz.getNum() + zz.getSymbol());
                bookdetailTimer.setText(DateFormatUtils.format(zz.getTime(), "yyyy-MM-dd HH:mm:ss"));
                bookdetailDizhi1.setText(zz.getContract());
                bookdetailMydizhi1.setText(zz.getFrom());
                if (zz.getDetail() != null) {
                    bookdetailKgf.setText(zz.getDetail());
                }
                bookdetailZc.setText(zz.getNum().stripTrailingZeros().toPlainString());
                bookdetail_todizhi1.setText(zz.getTo());
                bookdetail_hash.setText(zz.getHash());
                tophone_lin.setVisibility(View.VISIBLE);
                bookdetail_phone_view.setVisibility(View.VISIBLE);
                bookdetail_tophone1.setText(zz.getTo_phone());

                break;
            case 3:
                bookdetailLx.setText(getString(R.string.string_mine_exchange));
                bookdetail_hash1.setText(getString(R.string.string_book_hash));
                ExchangeBean dh = (ExchangeBean) intent.getSerializableExtra("list");
                bookdetailTitle.setText(dh.getExchange_get_sum() + "" + dh.getExchange_conf_get());
                exchange_amount_num.setText(dh.getExchange_spend_sum() + "");
                ru_hash1.setText(dh.getTo_hash());
                bookdetail_hash.setText(dh.getFrom_hash());
                bookdetailKgf.setText(dh.getExchange_conf_fee() + "");
                bookdetailTimer.setText(dh.getExchange_create_time());

                quxian_address.setVisibility(View.GONE);
                laiyuan_address.setVisibility(View.GONE);
                heyue_adderss.setVisibility(View.GONE);
                heyue_address_view.setVisibility(View.GONE);
                laiyuan_address_view.setVisibility(View.GONE);
                quxiang_address_view.setVisibility(View.GONE);

                ru_hash_view.setVisibility(View.VISIBLE);
                ru_hash.setVisibility(View.VISIBLE);
                view_huoqu.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                exchange_huoqu.setVisibility(View.VISIBLE);
                exchange_amount.setVisibility(View.VISIBLE);

                exchange_huoqu_num.setText(dh.getExchange_get_sum() + "");

                break;
            default:
                break;
        }
    }

    /**
     * \复制地址
     *
     * @param view
     */
    @OnClick({R.id.bookdetail_dizhi1_copy, R.id.bookdetail_mydizhi1_copy, R.id.bookdetail_todizhi1_copy, R.id.bookdetail_hash_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookdetail_dizhi1_copy:
                //copy合约地址
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                mClipData = ClipData.newPlainText("bookdetailDizhi1", bookdetailDizhi1.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getString(R.string.string_text_copy_seccess));
                break;
            case R.id.bookdetail_mydizhi1_copy:
                //copy来源地址
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                mClipData = ClipData.newPlainText("bookdetailMydizhi1", bookdetailMydizhi1.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getString(R.string.string_text_copy_seccess));
                break;
            case R.id.bookdetail_todizhi1_copy:
                //copy去向地址
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                mClipData = ClipData.newPlainText("bookdetail_todizhi1", bookdetail_todizhi1.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getString(R.string.string_text_copy_seccess));
                break;
            case R.id.bookdetail_hash_copy:
                //copy交易哈希
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                mClipData = ClipData.newPlainText("bookdetail_hash", bookdetail_hash.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getString(R.string.string_text_copy_seccess));
                break;
            default:
                break;
        }
    }
}