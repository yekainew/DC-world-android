package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.FinancesListAdapter;
import com.jkkg.hhtx.adapter.MyFinancesListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.CumulativeIncomeBean;
import com.jkkg.hhtx.net.bean.FinancesListBean;
import com.jkkg.hhtx.net.bean.MyFinancesListBean;
import com.jkkg.hhtx.net.bean.WalletDCBean;
import com.jkkg.hhtx.widget.TabEntity;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.jkkg.hhtx.widget.tablayout.listener.CustomTabEntity;
import com.jkkg.hhtx.widget.tablayout.listener.OnTabSelectListener;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.apache.commons.collections4.map.HashedMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.json.JSONUtil;

/**
 * 理财
 */
public class ManageFinancesActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_earnings_num)
    TextView addEarningsNum;
    @BindView(R.id.yesterday_earnings_num)
    TextView yesterdayEarningsNum;

    @BindView(R.id.finances_layout)
    CommonTabLayout finances_layout;

    @BindView(R.id.finances_list)
    RecyclerView finances_list; //投资列表

    @BindView(R.id.my_finances_list)
    RecyclerView my_finances_list;//我的投资
    @BindView(R.id.yield)
    TextView yield;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private FinancesListAdapter financesListAdapter;

    private List<FinancesListBean> o;
    private List<MyFinancesListBean> o1;
    private MyFinancesListAdapter myFinancesListAdapter;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_manage_finances;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_home_tab7));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText("规则");


        cumulativeIncome();

        financesListAdapter = new FinancesListAdapter(new ArrayList<>(), this);
        finances_list.setLayoutManager(new LinearLayoutManager(this));
        finances_list.setAdapter(financesListAdapter);


        myFinancesListAdapter = new MyFinancesListAdapter(this, new ArrayList<>());
        my_finances_list.setLayoutManager(new LinearLayoutManager(this));
        my_finances_list.setAdapter(myFinancesListAdapter);
        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageFinancesActivity.this, FinancialManagementRuleActivity.class));
//                startActivity(new Intent(ManageFinancesActivity.this, ManageLockActivity.class));
            }
        });
        setTab();

        financesListAdapter.setOnClickListener(new FinancesListAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position) {
                FinancesListBean financesListBean = o.get(position);
                Intent intent = new Intent(ManageFinancesActivity.this, ManageFinancesUSDTActivity.class);
                intent.putExtra("financesListBean", financesListBean);
                startActivity(intent);
            }
        });

        myFinancesListAdapter.setOnClickListener(new MyFinancesListAdapter.OnClick() {
            @Override
            public void setOnClick(View view, int position) {
                Intent intent = new Intent(ManageFinancesActivity.this, ManageFinancesBuyActivity.class);
                MyFinancesListBean myFinancesListBean = o1.get(position);
                intent.putExtra("myFinancesListBean", myFinancesListBean);
                startActivity(intent);
            }
        });


    }
    private CumulativeIncomeBean cumulativeIncomeBean;

    private void cumulativeIncome() {
        MyApp.requestSend.sendData("manageFinances.app.method.cumulativeIncome").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();

                cumulativeIncomeBean = gson.fromJson(message.getDate().toString(), CumulativeIncomeBean.class);
                addEarningsNum.setText(cumulativeIncomeBean.getTotal_revenue() + "");
                yesterdayEarningsNum.setText(cumulativeIncomeBean.getDay_income() + "");
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }

    private void setTab() {
        mTabEntities.add(new TabEntity(getString(R.string.string_investment_list), 0, 0));
        mTabEntities.add(new TabEntity(getString(R.string.string_my_investment), 0, 0));
        finances_layout.setTabData(mTabEntities);
        financesList();

        finances_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (mTabEntities.get(position).getTabTitle().equals(getString(R.string.string_investment_list))) {
                    finances_list.setVisibility(View.VISIBLE);
                    my_finances_list.setVisibility(View.GONE);
                    financesList();
                } else {
                    finances_list.setVisibility(View.GONE);
                    my_finances_list.setVisibility(View.VISIBLE);
                    myFinancesList();
                }
            }


            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    public void financesList() {
        MyApp.requestSend.sendData("manageFinances.app.method.list").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                o = gson.fromJson(message.getDate().toString(), new TypeToken<List<FinancesListBean>>() {
                }.getType());
                financesListAdapter.setData(o);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {

                return Message.ok();
            }
        });
    }


    public void myFinancesList() {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("pageNum", "1");
                put("pageSize", "100");

            }
        };
        MyApp.requestSend.sendData("manageFinances.app.method.myFinance", JSONUtil.toJsonStr(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                Gson gson = new Gson();
                o1 = gson.fromJson(message.getDate().toString(), new TypeToken<List<MyFinancesListBean>>() {
                }.getType());
                myFinancesListAdapter.setData(o1);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }



    @OnClick(R.id.yield)
    public void onViewClicked() {
        //收益列表
        Intent intent = new Intent(ManageFinancesActivity.this, ManageFinancesYeildActivity.class);
        if (cumulativeIncomeBean!=null) {
            intent.putExtra("all",cumulativeIncomeBean.getTotal_revenue());
        }
        startActivity(intent);
    }
}
