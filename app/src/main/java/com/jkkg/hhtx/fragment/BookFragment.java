package com.jkkg.hhtx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.MineBookDetailActivity;
import com.jkkg.hhtx.adapter.BookAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.net.bean.BlockChainTransactionBean;
import com.jkkg.hhtx.net.bean.ExchangeBean;
import com.jkkg.hhtx.net.bean.TransferBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.JsonBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.sql.loader.Select;
import com.mugui.sql.loader.Where;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.tron.walletserver.WalletManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 我的账本
 *
 * @author admin6
 */
public class BookFragment extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private View emptyView;
    private boolean isRedeem = false;
    private int type;
    @Autowired
    Dao dao;

    @Autowired
    Block block;

    BookAdapter bookAdapter;

    public static BookFragment newInstance(int type) {
        BookFragment fragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (type == 0) {
                    requestRechargeLog("recharge", type);
                } else if (type == 1) {
                    requestRechargeLog("withdrawal", type);
                } else if (type == 2) {
                    requestTransferLog();
                } else if (type == 3) {
                    requestExchangeLog();
                }
            }
        });
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        refreshLayout.setOnRefreshListener(this);
        type = getArguments().getInt("type");
        bookAdapter = new BookAdapter(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);

//        showLoading();
        //0充值 1提币 2转账 3兑换
        if (type == 0) {
            requestRechargeLog("recharge", type);
        } else if (type == 1) {
            requestRechargeLog("withdrawal", type);
        } else if (type == 2) {
            requestTransferLog();
        } else if (type == 3) {
            requestExchangeLog();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (type == 0) {
            requestRechargeLog("recharge", type);
        } else if (type == 1) {
            requestRechargeLog("withdrawal", type);
        } else if (type == 2) {
            requestTransferLog();
        } else if (type == 3) {
            requestExchangeLog();
        }
    }

    public void setAdapter(List<AssetsLogBean> assetsLogBeans,int type){
        List<JsonBean> list=new LinkedList<>();
        for (AssetsLogBean assetsLogBean : assetsLogBeans) {
            list.add(assetsLogBean);

        }
        bookAdapter.setNewInstance(list);

        bookAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(mContext, MineBookDetailActivity.class);
                intent.putExtra("list", list.get(position));
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }


    private void requestRechargeLog(String type, int type1) {
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(mContext).getString("this_wallet_name", "")));
        switch (type1) {
            case 0:
                List<AssetsLogBean> assetsLogBeans = dao.selectListDESC(new AssetsLogBean().setTo(this_wallet_name.getAddress()),"time");
                setAdapter(assetsLogBeans,0);
                break;
            case 1:
                List<AssetsLogBean> assetsLogBeans1 = dao.selectListDESC(new AssetsLogBean().setFrom(this_wallet_name.getAddress()),"time");
                setAdapter(assetsLogBeans1,1);
                break;
        }

    }

    private void requestTransferLog() {
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", "")));
        AssetsLogBean assetsLogBean = new AssetsLogBean().setFrom(this_wallet_name.getAddress());
        Select to_phone = Select.q(assetsLogBean).where(Where.q(assetsLogBean).orderByDESC("time"));
        List<AssetsLogBean> assetsLogBeans = dao.selectList(AssetsLogBean.class, to_phone);
        setAdapter(assetsLogBeans,2);
    }

    private void requestExchangeLog() {
        Map<String, Object> requestMap = new ArrayMap<>();
        MyApp.requestSend.sendData("exchange.method.log", requestMap).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
//                hideLoading();
                refreshLayout.finishRefresh();
                String data = message.getDate().toString();
                List<JsonBean> list = new LinkedList<>();
                JSONArray objects = JSONArray.parseArray(data);
                Iterator<Object> iterator = objects.iterator();
                while (iterator.hasNext()) {
                    ExchangeBean exchangeBean = ExchangeBean.newBean(ExchangeBean.class, iterator.next());
                    list.add(exchangeBean);
                }
                bookAdapter.setNewInstance(list);


                bookAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        Intent intent = new Intent(mContext, MineBookDetailActivity.class);
                        intent.putExtra("list", list.get(position));
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    }
                });
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
//                hideLoading();
                refreshLayout.finishRefresh();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
}