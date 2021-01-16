package com.jkkg.hhtx.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.MnemonicConfirmAdapter;
import com.jkkg.hhtx.adapter.MnemonicSelectAdapter;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Description:
 * Created by ccw on 09/18/2020 11:17
 * Email:chencw0715@163.com
 * //录入助记词
 *
 * @author admin6
 */
public class CreatSelectHelpAckActivity extends BaseActivity {

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
    @BindView(R.id.copy_mnemonic_rv1)
    RecyclerView mRecyclerView1;
    @BindView(R.id.copy_mnemonic_rv2)
    RecyclerView mRecyclerView2;
    WalletBean string_zjc;
    @BindView(R.id.ctrl)
    TextView ctrl;
    @BindView(R.id.btn_true)
    Button btnTrue;

    MnemonicConfirmAdapter confirmAdapter;
    MnemonicSelectAdapter selectAdapter;
    @BindView(R.id.edit_text)
    EditText editText;
    private ArrayList<String> wordsList;
    private ArrayList<String> confirmList;
    private ArrayList<String> selectList;
    private ArrayList<Integer> confirmIndexList;
    private ArrayList<Integer> selectIndexList;
    private int currentIndex = -1;
    private boolean zjc_isTrue = false;
    String action="";
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_creat_select_help_ack;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.string_toolbar_select_true);
        Intent intent = getIntent();
        string_zjc = (WalletBean) intent.getSerializableExtra("data");

        if (intent.getAction()!=null) {
            if (intent.getAction().equals("1")) {
                action="1";
            }
        }
        wordsList = new ArrayList<>();
        confirmList = new ArrayList<>();
        selectList = new ArrayList<>();
        confirmIndexList = new ArrayList<>();
        selectIndexList = new ArrayList<>();
        mRecyclerView1.setLayoutManager(new GridLayoutManager(this, 4));

        mRecyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

        if (!TextUtils.isEmpty(string_zjc.getRecoveryPhrase())) {
            showMyWord(string_zjc.getRecoveryPhrase());
        }
    }

    private void showMyWord(String myWallet) {
        String[] mnemonicStrings = myWallet.split("\\s");
        wordsList.clear();
        for (int i = 0; i < mnemonicStrings.length; i++) {
            wordsList.add(mnemonicStrings[i]);
        }
        List<Integer> integerList = getRandomList(24);
        if (integerList.size() >= mnemonicStrings.length && wordsList.size() > 0) {
            selectIndexList.addAll(integerList);
            for (int i = 0; i < wordsList.size(); i++) {
                confirmList.add("");
                selectList.add(wordsList.get(integerList.get(i)));
            }
        }
    }

    //获取0-size之间随机数不重复
    private static List<Integer> getRandomList(int size) {
        List<Integer> key = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            key.add(i);
        }
        for (int i = 0; i < size; i++) {
            boolean flag = true;
            while (flag) {
                int randmonnum = (int) Math.round(Math.random() * (size - 1));
                int index = key.indexOf(randmonnum);
                if (index >= 0) {
                    flag = false;
                    key.set(index, size);
                    result.add(randmonnum);
                    break;
                }
            }
        }

        return result;
    }

    @OnClick({R.id.ctrl, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctrl:
                //粘贴
                copeWord();
                break;
            case R.id.btn_true:
                String s = editText.getText().toString();
                String recoveryPhrase = string_zjc.getRecoveryPhrase();

                if (recoveryPhrase.equals(s)) {
                    Intent intent = new Intent(getContext(), CreateSelectSiAckActivity.class);
                    intent.putExtra("data", string_zjc);
                    intent.putExtra("type", "2");
                    intent.setAction(action);
                    startActivity(intent);
                    finish();
                }else{
                    showMessage(getString(R.string.string_mnemonic_verify_failed));
                }

              /*  if (zjc_isTrue) {

//            request();
                    Intent intent = new Intent(getContext(), CreateSelectSiAckActivity.class);
                    intent.putExtra("data", string_zjc);
                    startActivity(intent);
                } else {
                    showMessage(getString(R.string.string_mnemonic_verify_failed));
                }*/
                break;
            default:
                break;
        }
    }

    private void copeWord() {
        //读取剪切板
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        if (data != null && data.getItemCount() > 0) {
            ClipData.Item item = data.getItemAt(0);
            if (item != null) {
                String content = item.getText().toString();
                editText.setText(content);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordsList = null;
        confirmList = null;
        selectList = null;
        confirmIndexList = null;
        selectIndexList = null;
    }

}
