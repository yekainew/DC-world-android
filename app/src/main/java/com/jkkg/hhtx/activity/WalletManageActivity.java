package com.jkkg.hhtx.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AddressBookBean;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.widget.ExchangeNameDialog;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.classutil.DataSave;

import org.tron.walletserver.DuplicateNameException;
import org.tron.walletserver.WalletManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包管理
 */
public class WalletManageActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.safe_certification_type)
    TextView safeCertificationType;
    @BindView(R.id.safe_certification_img)
    ImageView safeCertificationImg;
    @BindView(R.id.safe_certification)
    LinearLayout safeCertification;
    @BindView(R.id.wallet_paw)
    TextView walletPaw;
    @BindView(R.id.safe_wallet)
    TextView safeWallet;
    @BindView(R.id.chongzhi_paw)
    TextView chongzhi_paw;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wallet_manage;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_wellte_manage);
        safeCertificationType.setText(Constants.getWalletName());
       /* safeCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "暂时无法修改名称", Toast.LENGTH_SHORT).show();
            }
        });

        walletPaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ModifyWalletPasswordActivity.class));
            }
        });

        safeWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreateSelectActivity.class));
            }
        });

        chongzhi_paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResetWalletPasswordActivity.class));
            }
        });*/

    }

    @Autowired
    Block block;
    @Autowired
    Dao dao;
    @OnClick({R.id.safe_certification, R.id.wallet_paw, R.id.safe_wallet, R.id.chongzhi_paw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.safe_certification:
                //修改名称

                ExchangeNameDialog exchangeNameDialog = new ExchangeNameDialog(getContext());

                exchangeNameDialog.setOnTrueClickListener(new ExchangeNameDialog.OnTrueClick() {
                    @Override
                    public void setOnTrueClick(String wallet_name) {
                        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
                        select.setName(wallet_name);
                        dao.updata(select);

                        AddressBookBean select1 = dao.select(new AddressBookBean().setWallet_name(Constants.getWalletName()));
                        if (select1!=null) {
                            select1.setWallet_name(wallet_name);
                            dao.updata(select1);
                        }


                        AssetsBean select2 = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()));
                        select2.setWallet_name(wallet_name);
                        dao.updata(select2);

                        AssetsLogBean select3 = dao.select(new AssetsLogBean().setWallet_name(Constants.getWalletName()));
                        select3.setWallet_name(wallet_name);
                        dao.updata(select3);

                        Context context = DataSave.app.getApplicationContext();
                        SharedPreferences walletPref = context.getSharedPreferences(Constants.getWalletName(), Context.MODE_PRIVATE);
                        Map<String, ?> all = walletPref.getAll();


                        SharedPreferences sharedPreferences = context.getSharedPreferences(wallet_name, Context.MODE_PRIVATE);

                        SharedPreferences.Editor edit1 = sharedPreferences.edit();
                        Iterator<? extends Map.Entry<String, ?>> iterator = all.entrySet().iterator();
                        while(iterator.hasNext()){
                            Map.Entry<String, ?> next = iterator.next();
                            if(next.getValue() instanceof  Boolean){
                                edit1.putBoolean(next.getKey(),(Boolean)next.getValue());
                            }else
                                edit1.putString(next.getKey(),next.getValue().toString());
                        }
                        edit1.commit();
                        SharedPreferences.Editor edit2 = walletPref.edit();
                        edit2.clear();
                        edit2.commit();

                        edit1.putString("wallet_name_key", wallet_name);
                        edit1.commit();


                        SharedPreferences sharedPreferences1 = context.getSharedPreferences(context.getString(com.mugui.robot.mugui_block.R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedEditor = sharedPreferences1.edit();

                        Set<String> walletNames = new HashSet<>(sharedPreferences1.getStringSet(context.getString(com.mugui.robot.mugui_block.R.string.wallets_key), new HashSet<>()));

                        if (!walletNames.contains(wallet_name)) {
                            walletNames.add(wallet_name);
                            sharedEditor.putStringSet(context.getString(com.mugui.robot.mugui_block.R.string.wallets_key), walletNames);
                            sharedEditor.commit();
                        }
                        WalletManager.selectWallet(wallet_name);
                        Constants.saveWalletName(wallet_name);
                        startActivity(new Intent(getContext(),MemberActivity.class));
                        exchangeNameDialog.dismiss();
                        finish();

                    }
                });

                exchangeNameDialog.OnFalseClickListener(new ExchangeNameDialog.OnFalseClick() {
                    @Override
                    public void setOnFalseClick() {
                        exchangeNameDialog.dismiss();
                    }
                });

                exchangeNameDialog.show();

                break;
            case R.id.wallet_paw:
                //修改密码
                startActivity(new Intent(this,ModifyWalletPasswordActivity.class));
                break;
            case R.id.safe_wallet:
                //备份钱包
                startActivity(new Intent(this,CreateSelectActivity.class));
                break;
            case R.id.chongzhi_paw:
                //重置密码 ResetWalletPasswordActivity
                startActivity(new Intent(this,ResetWalletPasswordActivity.class));
                break;
            default:
                break;
        }
    }
}
