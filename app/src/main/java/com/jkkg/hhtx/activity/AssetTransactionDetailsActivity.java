package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.king.zxing.util.CodeUtils;
import com.mugui.base.base.Autowired;

import org.apache.commons.lang3.time.DateFormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资产-币种-交易详情
 */
public class AssetTransactionDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.img_type)
    ImageView imgType;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.asset_name1)
    TextView assetName1;
    @BindView(R.id.copy_address1)
    ImageView copyAddress1;
    @BindView(R.id.asset_name2)
    TextView assetName2;
    @BindView(R.id.copy_address2)
    ImageView copyAddress2;
    @BindView(R.id.asset_name3)
    TextView assetName3;
    @BindView(R.id.copy_address3)
    ImageView copyAddress3;
    @BindView(R.id.geight)
    TextView geight;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.code_tu)
    ImageView codeTu;
    private AssetsLogBean bean;
    private String url;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_asset_transaction_details;
    }

    @Autowired
    Dao dao;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        bean = (AssetsLogBean) intent.getSerializableExtra("bean");
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));

        if (select != null) {
            String address = select.getAddress();
            if (bean.getTo().equals(address)) {
                price.setText("+" + bean.getNum().stripTrailingZeros().toPlainString());
            } else {
                price.setText("-" + bean.getNum().stripTrailingZeros().toPlainString());
            }
        }
        assetName2.setText(bean.getTo());
        assetName1.setText(bean.getFrom());
        assetName3.setText(bean.getHash());
        time.setText(DateFormatUtils.format(bean.getTime(), Constants.TIME));

        url = "https://blockchaindc.io?hash=" + bean.getHash();


        Bitmap qr = CodeUtils.createQRCode(url, ScreenSizeUtil.dp2px(getContext(), 90));
        Glide.with(getContext()).load(qr).into(codeTu);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.copy_address1, R.id.copy_address2, R.id.copy_address3, R.id.detail_from_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copy_address1:
                copyData2Clipboard(bean.getFrom());
                break;
            case R.id.copy_address2:

                copyData2Clipboard(bean.getTo());
                break;
            case R.id.copy_address3:

                copyData2Clipboard(bean.getHash());
                break;
            case R.id.detail_from_open:
                Intent intent = new Intent(this, PublicChainActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
                break;
        }
    }

}