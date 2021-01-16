package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.HelpBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的-帮助中心
 */
public class MineHelpActivity extends BaseActivity {

    @BindView(R.id.exchange_fanhui)
    ImageView exchangeFanhui;
    @BindView(R.id.mine_help_tab1)
    TextView mineHelpTab1;
    @BindView(R.id.mine_help_tab2)
    TextView mineHelpTab2;
    @BindView(R.id.mine_help_tab3)
    TextView mineHelpTab3;
    @BindView(R.id.mine_help_tab4)
    TextView mineHelpTab4;
    @BindView(R.id.mine_help_tab5)
    TextView mineHelpTab5;
    @BindView(R.id.mine_help_tab6)
    TextView mineHelpTab6;
    @BindView(R.id.mine_help_tab8)
    TextView mineHelpTab8;
    @BindView(R.id.mine_help_tab9)
    TextView mineHelpTab9;
    @BindView(R.id.mine_help_tab10)
    TextView mineHelpTab10;
    @BindView(R.id.mine_help_tab11)
    TextView mineHelpTab11;
    @BindView(R.id.mine_help_tab12)
    TextView mineHelpTab12;
    @BindView(R.id.mine_help_tab13)
    TextView mineHelpTab13;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_help;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mineHelpTab1.setEnabled(false);
        mineHelpTab2.setEnabled(false);
        mineHelpTab3.setEnabled(false);
        mineHelpTab4.setEnabled(false);
        mineHelpTab5.setEnabled(false);
        mineHelpTab6.setEnabled(false);
        mineHelpTab8.setEnabled(false);
        mineHelpTab9.setEnabled(false);
        mineHelpTab10.setEnabled(false);
        mineHelpTab11.setEnabled(false);
        mineHelpTab12.setEnabled(false);
        mineHelpTab13.setEnabled(false);
        help();
        exchangeFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private List<HelpBean> o;
    private void help() {
        MyApp.requestSend.sendData("app.info.help").main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                o = gson.fromJson(message.getDate().toString(), new TypeToken<List<HelpBean>>() {
                }.getType());

                mineHelpTab1.setEnabled(true);
                mineHelpTab2.setEnabled(true);
                mineHelpTab3.setEnabled(true);
                mineHelpTab4.setEnabled(true);
                mineHelpTab5.setEnabled(true);
                mineHelpTab6.setEnabled(true);
                mineHelpTab8.setEnabled(true);
                mineHelpTab9.setEnabled(true);
                mineHelpTab10.setEnabled(true);
                mineHelpTab11.setEnabled(true);
                mineHelpTab12.setEnabled(true);
                mineHelpTab13.setEnabled(true);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @OnClick({R.id.mine_help_tab1, R.id.mine_help_tab2, R.id.mine_help_tab3, R.id.mine_help_tab4, R.id.mine_help_tab5, R.id.mine_help_tab6, R.id.mine_help_tab8, R.id.mine_help_tab9, R.id.mine_help_tab10, R.id.mine_help_tab11, R.id.mine_help_tab12, R.id.mine_help_tab13})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_help_tab1:

                break;
            case R.id.mine_help_tab2:
                break;
            case R.id.mine_help_tab3:
                break;
            case R.id.mine_help_tab4:
                forListType("mnemoc_description","助记词使用说明");
                break;
            case R.id.mine_help_tab5:
                forListType("private_key","私钥使用说明");
                break;
            case R.id.mine_help_tab6:
                forListType("activation","激活说明");
                break;
            case R.id.mine_help_tab8:
                forListType("recharge","收款说明");
                break;
            case R.id.mine_help_tab9:
                break;
            case R.id.mine_help_tab10:
                forListType("transfer","转账说明");
                break;
            case R.id.mine_help_tab11:
                forListType("resonance","共振说明");
                break;
            case R.id.mine_help_tab12:
                forListType("assets","资产查询说明");
                break;
            case R.id.mine_help_tab13:
                forListType("dc_chain_browser","区块链浏览器使用说明");
                break;
            default:
                break;
        }
    }

    public void forListType(String key,String title){
        for (HelpBean helpBean : o) {
            if (helpBean.getSystem_extra().equals(key)) {
                Intent intent = new Intent(getContext(),HelpWebActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("url",helpBean.getSystem_value());
                startActivity(intent);
            }
        }
    }
}
