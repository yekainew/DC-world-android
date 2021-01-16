package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.ExchangeBean1;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeAssetsDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.detail_timer)
    TextView detailTimer;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.detail_to_tv)
    TextView detailToTv;
    @BindView(R.id.detail_to_fenxiang)
    ImageView detailToFenxiang;
    @BindView(R.id.detail_to_open)
    ImageView detailToOpen;
    @BindView(R.id.detail_from_tv)
    TextView detailFromTv;
    @BindView(R.id.detail_from_fenxiang)
    ImageView detailFromFenxiang;
    @BindView(R.id.detail_from_open)
    ImageView detailFromOpen;
    @BindView(R.id.detail_return_tv)
    TextView detailReturnTv;
    @BindView(R.id.detail_return_fenxiang)
    ImageView detailReturnFenxiang;
    @BindView(R.id.detail_return_open)
    ImageView detailReturnOpen;
    private String to_hash;
    private String from_hash;
    private String usdt_to_hash;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exchange_assets_detail;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText("共振详情");
        Intent intent = getIntent();
        ExchangeBean1 exchangeBean1 = (ExchangeBean1) intent.getSerializableExtra("exchangeBean1");



        BigDecimal exchange_get_sum = exchangeBean1.getExchange_get_sum();
        BigDecimal exchange_return_num = exchangeBean1.getExchange_return_num();
        BigDecimal exchange_fee_sum = exchangeBean1.getExchange_fee_sum();
        Log.d("ExchangeAssetsDetailAct", exchange_get_sum.toPlainString()+"----" + exchange_return_num.toPlainString()+"----" + exchange_fee_sum.toPlainString());
        BigDecimal exchange_spend_sum = exchangeBean1.getExchange_spend_sum();//花费了多少


        BigDecimal add = exchange_spend_sum.add(exchange_return_num);


        BigDecimal divide = exchange_get_sum.divide(add.add(exchange_fee_sum), 4, BigDecimal.ROUND_UP);


        BigDecimal add1 = add.add(exchange_fee_sum);
        BigDecimal divide1 = exchange_spend_sum.divide(add1, 4, BigDecimal.ROUND_UP);

        tv1.setText("-" + add1.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());//总量
        tv6.setText("+" + exchange_get_sum.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        tv2.setText(divide1.multiply(new BigDecimal("100")).setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + "%");
        tv3.setText(exchange_spend_sum.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());//实际得到/花费了多少
        tv4.setText(exchange_return_num.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        tv5.setText(exchange_fee_sum.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        detailTimer.setText("共振时间:" + DateFormatUtils.format(exchangeBean1.getExchange_create_time(), Constants.TIME));

        to_hash = exchangeBean1.getTo_hash();

        from_hash = exchangeBean1.getFrom_hash();

        usdt_to_hash = exchangeBean1.getUsdt_to_hash();

        detailToTv.setText(to_hash);

        detailFromTv.setText(from_hash);

        detailReturnTv.setText(usdt_to_hash);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.detail_to_fenxiang, R.id.detail_to_open, R.id.detail_from_fenxiang, R.id.detail_from_open, R.id.detail_return_fenxiang, R.id.detail_return_open})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), DetailHashActivity.class);
        switch (view.getId()) {
            case R.id.detail_to_fenxiang:
                copyData2Clipboard(to_hash);
                break;
            case R.id.detail_to_open:
                intent.putExtra("hash",to_hash);
                break;
            case R.id.detail_from_fenxiang:
                copyData2Clipboard(from_hash);
                break;
            case R.id.detail_from_open:
                intent.putExtra("hash",from_hash);
                break;
            case R.id.detail_return_fenxiang:
                copyData2Clipboard(usdt_to_hash);
                break;
            case R.id.detail_return_open:
                intent.putExtra("hash",usdt_to_hash);
                break;

        }
        startActivity(intent);
    }
}
