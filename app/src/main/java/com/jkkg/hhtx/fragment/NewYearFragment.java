package com.jkkg.hhtx.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.base.BaseFragment;

/**
 * 新闻
 */
public class NewYearFragment extends BaseFragment {
    private View emptyView;
    public NewYearFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_empty,container,false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    public static NewYearFragment newInstance() {

        Bundle args = new Bundle();

        NewYearFragment fragment = new NewYearFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
