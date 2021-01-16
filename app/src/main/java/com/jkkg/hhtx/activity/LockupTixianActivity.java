package com.jkkg.hhtx.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.BadParcelableException;
import android.os.Bundle;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 锁仓 收益明细
 */
public class LockupTixianActivity extends BaseActivity {

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_lockup_tixian;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    /*private void yesti() {
        Map<String, String> map = new HashedMap<String, String>() {
            {
                put("wallet_type", "4");
            }
        };
        MyApp.requestSend.sendData("app.wallet.wallet", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                WalletDCBean walletDCBean = WalletDCBean.newBean(WalletDCBean.class, message.getDate());
                usable = walletDCBean.getUsable();
                yesTi.setText(getString(R.string.string_can_mention) + usable);
                String s = new BigDecimal(all).subtract(new BigDecimal(usable)).stripTrailingZeros().toPlainString();
                yesYi.setText(getString(R.string.string_mentioned) + s);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }*/

}
