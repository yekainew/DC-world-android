package com.jkkg.hhtx.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.CunbiActivity;
import com.jkkg.hhtx.activity.DAppActivity;
import com.jkkg.hhtx.activity.ExchangeActivity;
import com.jkkg.hhtx.activity.MemberActivity;
import com.jkkg.hhtx.activity.MineFeedbackListActivity;
import com.jkkg.hhtx.activity.MineSystemNoticeActivity;
import com.jkkg.hhtx.activity.NewsListActivity;
import com.jkkg.hhtx.activity.PublicChainActivity;
import com.jkkg.hhtx.activity.RewardActivity;
import com.jkkg.hhtx.activity.SignInActivity;
import com.jkkg.hhtx.activity.ZxingActivity;
import com.jkkg.hhtx.adapter.HomeBannerAdapter;
import com.jkkg.hhtx.adapter.HomeItemAdapter;
import com.jkkg.hhtx.adapter.NoticeLineAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.net.bean.CarouselmapBean;
import com.jkkg.hhtx.net.bean.QuotesBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.jkkg.hhtx.widget.tablayout.CommonTabLayout;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Description:
 * Created by oby on 09/08/2020 15:17
 * 主界面
 * Email:oubaoyi@qq.com
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.notice_all)
    TextView noticeAll;
    @BindView(R.id.home_tab1)
    LinearLayout homeTab1;
    @BindView(R.id.home_tab2)
    LinearLayout homeTab2;
    @BindView(R.id.home_tab3)
    LinearLayout homeTab3;
    @BindView(R.id.home_tab4)
    LinearLayout homeTab4;
    @BindView(R.id.home_tab5)
    LinearLayout homeTab5;
    @BindView(R.id.scrollerLayout)
    NestedScrollView scrollerLayout;
    @BindView(R.id.sign)
    ImageView sign;
    @BindView(R.id.saomiao)
    ImageView saomiao;
    @BindView(R.id.bn_notice)
    Banner bnNotice;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.sousuo)
    ImageView sousuo;
    @BindView(R.id.sousuo_edit)
    ClearEditText sousuoEdit;
    @BindView(R.id.find_tab1)
    LinearLayout findTab1;
    @BindView(R.id.find_tab2)
    LinearLayout findTab2;
    @BindView(R.id.find_tab3)
    LinearLayout findTab3;
    @BindView(R.id.find_tab4)
    LinearLayout findTab4;
    @BindView(R.id.find_tab5)
    LinearLayout findTab5;
    @BindView(R.id.find_tab6)
    LinearLayout findTab6;
    @BindView(R.id.find_tab7)
    LinearLayout findTab7;
    @BindView(R.id.commontab_layout)
    CommonTabLayout commontabLayout;
    private HomeItemAdapter homeItemAdapter;
    Disposable disposable;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        banner.addBannerLifecycleObserver(this);
        bnNotice.addBannerLifecycleObserver(this);
        List<Integer> img_banner = new ArrayList<>();
        img_banner.add(R.mipmap.bg_home_banner1);
        img_banner.add(R.mipmap.bg_home_banner2);
        img_banner.add(R.mipmap.bg_home_banner3);

        homeItemAdapter = new HomeItemAdapter(mContext, new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(homeItemAdapter);
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
        requestNotice();
        carouselmap();
    }

    public void carouselmap() {
        MyApp.requestSend.sendData("carouselmap.get.base").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                List<CarouselmapBean> carouselmapBeans = JSONArray.parseArray(message.getDate().toString(), CarouselmapBean.class);
                List<String> list = new ArrayList<>();
                for (CarouselmapBean carouselmapBean : carouselmapBeans) {
                    if (carouselmapBean.getWheel_planting_delete() == 0) {
                        list.add(carouselmapBean.getRotary_planting_map());
                    }
                }
                banner.setAdapter(new HomeBannerAdapter(list, mContext));
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
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
            requestNotice();
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

    @OnClick({R.id.notice_all, R.id.home_tab1, R.id.home_tab2, R.id.home_tab3, R.id.home_tab4, R.id.home_tab5, R.id.saomiao, R.id.sign, R.id.find_tab1, R.id.find_tab2, R.id.find_tab3, R.id.find_tab4, R.id.find_tab5, R.id.find_tab6, R.id.find_tab7})
    public void onViewClicked(View view) {
        boolean islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
        switch (view.getId()) {
            case R.id.notice_all:
                //全部进入系统公告
                startActivity(new Intent(mContext, MineSystemNoticeActivity.class));
                break;
            case R.id.home_tab1:
                //理财
                if (!islogin) {
                    showMessage(getString(R.string.string_useer_login));
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, CunbiActivity.class));

                break;
            case R.id.home_tab2:
                //奖励
                startActivity(new Intent(mContext, RewardActivity.class));
                break;
            case R.id.home_tab3:
                //留言
                if (!islogin) {
                    showMessage(getString(R.string.string_useer_login));
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, MineFeedbackListActivity.class));
                break;
            case R.id.home_tab4:
                //资讯
                startActivity(new Intent(mContext, NewsListActivity.class));
                break;
            case R.id.home_tab5:
                //公链
              /*  Uri uri = Uri.parse("https://blockchaindc.io");
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
                // 官方解释 : Name of the component implementing an activity that can display the intent
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    final ComponentName componentName = intent.resolveActivity(getContext().getPackageManager());
                    LogUtil.d("suyan = " + componentName.getClassName());
                    getContext().startActivity(Intent.createChooser(intent, "请选择浏览器"));
                } else {
                  showMessage("链接错误或无浏览器");
                }*/
                startActivity(new Intent(mContext, PublicChainActivity.class));
                break;
            case R.id.sign:
                if (!islogin) {
                    showMessage(getString(R.string.string_useer_login));
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                //签到
                startActivity(new Intent(mContext, SignInActivity.class));
                break;
            case R.id.saomiao:
                //扫描
                RxPermissions rxPermissions = new RxPermissions(HomeFragment.this);
                disposable = rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_failed_to_get_permission));
                                } else {
                                    Intent intent = new Intent(mContext, ZxingActivity.class);
                                    intent.setAction("3");
                                    startActivityForResult(intent, 303);
                                }
                            }
                        });
                break;

            case R.id.find_tab1:

                showMessage("暂未开放");
                startActivity(new Intent(mContext, DAppActivity.class));
                break;
            case R.id.find_tab2:
                startActivity(new Intent(getContext(),PublicChainActivity.class));
                break;
            case R.id.find_tab3:
                showMessage("暂未开放");
                break;
            case R.id.find_tab4:
                islogin = SpUtil.getInstance(mContext).getBoolean("islogin", false);
                if (!islogin) {
                    startActivity(new Intent(mContext, MemberActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), ExchangeActivity.class));
                break;
            case R.id.find_tab5:
                showMessage("暂未开放");
                break;
            case R.id.find_tab6:
                showMessage("暂未开放");
                break;
            case R.id.find_tab7:
                showMessage("暂未开放");
                break;
            default:
                break;
        }
    }

    private void requestNotice() {
        Map<String, String> map = new ArrayMap<>();
        MyApp.requestSend.sendData("announcement.app.method.list", GsonUtil.toJsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data = message.getDate().toString();
                List<AnnouncementBean> myTramListBeans = JSONArray.parseArray(data, AnnouncementBean.class);
                if (myTramListBeans.size() > 0) {
                    bnNotice.setAdapter(new NoticeLineAdapter(myTramListBeans));
                }
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
}
