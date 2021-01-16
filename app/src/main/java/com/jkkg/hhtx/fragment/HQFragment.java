package com.jkkg.hhtx.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HQFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private HomeItemAdapter homeItemAdapter;
    public HQFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_h_q, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        homeItemAdapter = new HomeItemAdapter(mContext, new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(homeItemAdapter);
    }

    public static HQFragment newInstance() {

        Bundle args = new Bundle();

        HQFragment fragment = new HQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        showLoading();
        MyApp.requestSend.sendData("bc.ws.quotes").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data = message.getDate().toString();
                List<QuotesBean> list = JSONArray.parseArray(data, QuotesBean.class);
                homeItemAdapter.setData(list);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
        MyApp.requestSend.subWs("bc.ws.quotes").main(call);
       /* requestNotice();
        carouselmap();*/
    }

    NetCall.Call call = new NetCall.Call() {
        @Override
        public Message ok(Message message) {
            List<QuotesBean> data = JSONArray.parseArray(message.getDate().toString(), QuotesBean.class);
            homeItemAdapter.setData(data);
            return Message.ok();
        }

        @Override
        public Message err(Message message) {
            return null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (rootView != null) {
            MyApp.requestSend.subWs("bc.ws.quotes").main(call);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApp.requestSend.unsubWs("bc.ws.quotes");
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApp.requestSend.unsubWs("bc.ws.quotes");
    }

}
