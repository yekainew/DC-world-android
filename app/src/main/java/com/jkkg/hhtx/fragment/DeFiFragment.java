package com.jkkg.hhtx.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.HomeItemAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.net.bean.QuotesBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeFiFragment extends BaseFragment {

    public DeFiFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_empty, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
    }


    public static DeFiFragment newInstance() {

        Bundle args = new Bundle();

        DeFiFragment fragment = new DeFiFragment();
        fragment.setArguments(args);
        return fragment;
    }




}
