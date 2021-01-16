package com.jkkg.hhtx.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.AssetTransactionDetailsActivity;
import com.jkkg.hhtx.adapter.AssetsDetailAdapter;
import com.jkkg.hhtx.adapter.AssetsDetailFromAdapter;
import com.jkkg.hhtx.adapter.AssetsDetailToAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.mugui.base.base.Autowired;
import com.mugui.sql.listener.PageUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetsDetailToFragment extends BaseFragment {
    @BindView(R.id.recyclerView_to)
    RecyclerView recyclerView;
    @BindView(R.id.asset_smart)
    SmartRefreshLayout assetSmart;


    private AssetsDetailToAdapter assetsDetailAdapter;
    public AssetsDetailToFragment() {
        // Required empty public constructor
    }
    int pagenum = 1;

    @Autowired
    Dao dao;

    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_detail_to, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    public static AssetsDetailToFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        AssetsDetailToFragment fragment = new AssetsDetailToFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void lazyLoadData() {
        super.lazyLoadData();

        Bundle arguments = getArguments();

        String name = arguments.getString("name");


        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
//        Wallet selectedWallet = WalletManager.getSelectedWallet();
        String address = this_wallet_name.getAddress();
        assetsDetailAdapter = new AssetsDetailToAdapter(mContext, new ArrayList<>(), address);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(assetsDetailAdapter);
        assetSmart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pagenum++;
                query2(pagenum, name);
                assetSmart.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pagenum = 1;
                query2(pagenum, name);
                assetSmart.finishRefresh();
            }
        });
        assetSmart.autoRefresh();
        query2(pagenum, name);

        assetsDetailAdapter.setOnClickListener(new AssetsDetailToAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position, AssetsLogBean bean) {
                Intent intent = new Intent(mContext, AssetTransactionDetailsActivity.class);
                intent.putExtra("bean",bean);
                startActivity(intent);
            }
        });
    }

    private void query2(int pageNum, String currency_name) {
        assetSmart.finishRefresh();
        UserWalletBean select = dao.select(new UserWalletBean().setName(Constants.getWalletName()));
        String address = select.getAddress();
        PageUtil.offsetPage(pageNum, 20);
        List<AssetsLogBean> assetsLogBeans = dao.selectListDESC(new AssetsLogBean().setSymbol(currency_name).setWallet_name(Constants.getWalletName()).setTo(address), "time");

        Iterator<AssetsLogBean> iterator = assetsLogBeans.iterator();

        while (iterator.hasNext()) {
            AssetsLogBean x = iterator.next();
            if (x.getTo() == null) {
                iterator.remove();
            }
        }
        if (assetsLogBeans.size() != 0) {
            if (pageNum == 1) {
                assetsDetailAdapter.setData(assetsLogBeans);
            } else {
                assetsDetailAdapter.upData(assetsLogBeans);
            }
        }
    }

}
