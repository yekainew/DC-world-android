package com.jkkg.hhtx.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.MnemonicAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.WalletBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/15/2020 11:40
 * Email:chencw0715@163.com
 * 助记词备份
 */
public class CreateSelectHelpActivity extends BaseActivity {

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
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    @BindView(R.id.import_mnemonic_rv)
    RecyclerView importMnemonicRv;

    MnemonicAdapter mnemonicAdapter;
    @BindView(R.id.copy)
    TextView copy;
    @BindView(R.id.btn_true)
    Button btnTrue;

    private ArrayList<String> wordsList;
    private String mnemonic;
    private String recoveryPhrase;
    private WalletBean qianbao;
    private String type;
    String action="";
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_create_select_help;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_create_selecthelp);
        Intent intent = getIntent();
        qianbao = (WalletBean) intent.getSerializableExtra("qianbao");
        type = intent.getStringExtra("type");
        recoveryPhrase = qianbao.getRecoveryPhrase();
        if (intent.getAction()!=null) {
            if (intent.getAction().equals("1")) {
                action="1";
            }
        }
        toolbarImageLeft.setVisibility(View.VISIBLE);
        wordsList = new ArrayList<>();
        mnemonicAdapter = new MnemonicAdapter();
        showMyWord(recoveryPhrase);
        importMnemonicRv.setLayoutManager(new GridLayoutManager(this, 4));
        importMnemonicRv.setAdapter(mnemonicAdapter);
//        request();
    }

    private void request() {
        showLoading();
        MyApp.requestSend.sendData("bc.method.getMnemonic").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                mnemonic = message.getDate().toString();
                showMyWord(mnemonic);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }


    private void showMyWord(String myWallet) {
        String[] mnemonicStrings = myWallet.split("\\s");
        wordsList.clear();
        for (int i = 0; i < mnemonicStrings.length; i++) {
            wordsList.add(mnemonicStrings[i]);
        }
        ((BaseQuickAdapter) mnemonicAdapter).setList(wordsList);
    }


    @OnClick({R.id.copy, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copy:
                copyWords();
                break;
            case R.id.btn_true:
                if (type.equals("1")) {
                    finish();
                }else{
                    if (!TextUtils.isEmpty(recoveryPhrase)) {
                        Intent intent = new Intent(this, CreatSelectHelpAckActivity.class);
                        intent.putExtra("data", qianbao);
                        intent.putExtra("type","2");
                        intent.setAction(action);
                        startActivity(intent);
                        finish();
                    } else {
                        showMessage(getString(R.string.string_mnemonic_get_fail));
                    }
                }

                break;
            default:
                break;
        }
    }

    private void copyWords() {
        //这里是复制 复制就比较简单，就是遍历，获取到值拼接的
        //和你说下 ，你们的复制粘贴按钮是放哪儿的？没有就用我们这里得就行
        String words = "";
        List<String> data = mnemonicAdapter.getData();
        for (int i = 0; i <data.size() ; i++) {
            words=words+" "+data.get(i);
        }
        String substring = words.substring(1);
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("words", substring);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        showMessage(getString(R.string.string_text_copy_seccess));
        words="";
    }


}
