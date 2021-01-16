package com.jkkg.hhtx.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.DealAddFlowActivity;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.widget.SweetAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易-资金池
 * 资金池列表    item_deal_capitalpool
 * 提供   pop_deal_provide
 * 增加流动性确认    pop_deal_add_liudong_sure
 * 等待中      pop_deal_pool_wait
 * 失败       pop_deal_pool_wait_fail
 * 成功       pop_deal_pool_wait_success
 */
public class DealCapitalpoolFragment extends BaseFragment {


    @BindView(R.id.btn_ture)
    Button btnTure;
    @BindView(R.id.why_liudong)
    ImageView whyLiudong;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    private SweetAlertDialog passwordAlertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DealCapitalpoolFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deal_capitalpool, container, false);
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

    @OnClick({R.id.btn_ture, R.id.why_liudong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ture:
                //添加流动性
                startActivity(new Intent(mContext, DealAddFlowActivity.class));
                break;
            case R.id.why_liudong:
                //解释流动性
                passwordAlertDialog = null;
                passwordAlertDialog = new SweetAlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.string_sweet_title))
                        .setMessage("流动性提供者将获得每笔交易的0.3%作为协议奖励。")
                        .setPositiveButton(getString(R.string.string_text_know), new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }
}
