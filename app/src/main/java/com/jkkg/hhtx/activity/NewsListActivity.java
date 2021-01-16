package com.jkkg.hhtx.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.NewListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.net.bean.NewListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.ImageUtils;
import com.jkkg.hhtx.utils.PageUtils;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.king.zxing.util.CodeUtils;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import cn.hutool.json.JSONUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * 首页 快讯
 */
public class NewsListActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_rec)
    RecyclerView noticeRec;
    @BindView(R.id.notice_smart)
    SmartRefreshLayout noticeSmart;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    private PageUtils pageUtils;
    private NewListAdapter newListAdapter;
    private List<NewListBean> o;
    private Disposable disposable;
    BasePopupWindow InvitePopupWindow;
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_news;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_home_tab1));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText(getString(R.string.string_right_new));
        pageUtils = new PageUtils(noticeSmart);
        newListAdapter = new NewListAdapter(new ArrayList<>(), this);
        noticeRec.setLayoutManager(new LinearLayoutManager(this));
        noticeRec.setAdapter(newListAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
                newsList();
            }
        });


        toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean islogin = SpUtil.getInstance(NewsListActivity.this).getBoolean("islogin", false);
                if (!islogin) {
                    showMessage(getString(R.string.string_useer_login));
                    startActivity(new Intent(NewsListActivity.this, MemberActivity.class));
                    return;
                }
                startActivity(new Intent(NewsListActivity.this, NewsActivity.class));
            }
        });

        newListAdapter.setOnDownClickListener(new NewListAdapter.OnDownClick() {
            @Override
            public void setOnDownClick(View view, int position) {
                //利空
                int information_id = o.get(position).getInformation_id();
                like(information_id,2);
            }
        });

        newListAdapter.setOnUpClickListener(new NewListAdapter.OnUpClick() {
            @Override
            public void setOnUpClick(View view, int position) {
                //利好
                int information_id = o.get(position).getInformation_id();
                like(information_id,1);
            }
        });

        newListAdapter.setOnShareClickListener(new NewListAdapter.OnShareClick() {
            @Override
            public void setOnShareClick(View view, int position) {

                if (o!=null){
                    NewListBean bean = o.get(position);
                    if (bean!=null){
                        //分享
                        RxPermissions rxPermissions = new RxPermissions(NewsListActivity.this);
                        disposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            //表示用户同意权限
                                            showPop(bean);
                                            //requestWebRegUrl();
                                        } else {
                                            //表示用户不同意权限
                                            showMessage(getString(R.string.string_user_refuse_usser));
                                        }
                                    }
                                });
                    }

                }

            }
        });
        newsList();
    }

    private void showPop(NewListBean bean) {
        InvitePopupWindow = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.pop_news_join)
                .config(new QuickPopupConfig().gravity(Gravity.BOTTOM)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.FROM_BOTTOM)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.TO_BOTTOM)
                                .toDismiss())
                        .withClick(R.id.dialog_left_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InvitePopupWindow.dismiss();
                            }
                        })
                        .withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
                                    LinearLayout lin_xicun = InvitePopupWindow.findViewById(R.id.lin_xicun);
                                    //保存这个视图就可以
                                    Bitmap bitmap = ImageUtils.screenShotView(lin_xicun);
                                    ImageUtils.savePicture(getContext(), bitmap);
                                    InvitePopupWindow.dismiss();
                                }
                            }
                        })).show();

        try {
            TextView text_title = InvitePopupWindow.findViewById(R.id.text_title);
            TextView text_time = InvitePopupWindow.findViewById(R.id.text_time);
            TextView text_content = InvitePopupWindow.findViewById(R.id.text_content);
           text_title.setText(bean.getTitle());
           text_time.setText(DateFormatUtils.format(bean.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));
           text_content.setText(bean.getContent());
            requestWebDownUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestWebDownUrl() {
        MyApp.requestSend.sendData("app.info.webDownUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();

                if (InvitePopupWindow != null) {
                    ImageView download_app = InvitePopupWindow.findViewById(R.id.img_code);
                    Bitmap qr = CodeUtils.createQRCode(data, ScreenSizeUtil.dp2px(getContext(), 60));
//                    GlideApp.with(getContext()).load(qr).into(download_app);
                    Glide.with(getContext()).load(qr).into(download_app);
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }


    public void like(int id,int type){
        Map<String,String> map=new HashedMap<String ,String>(){
            {
                put("information_id",id+"");
                put("type",type+"");
            }
        };

        MyApp.requestSend.sendData("information.app.method.like", map).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                showMessage(getString(R.string.string_success));
                newsList();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(getString(R.string.string_fail));
                return Message.ok();
            }
        });
    }
    private void newsList() {
        Map<String,String> map=new ArrayMap<>();
        map.put("pageNum",pageUtils.getPagNumber()+"");
        map.put("pageSize",pageUtils.getPagSize()+"");

        MyApp.requestSend.sendData("information.app.method.newsList", GsonUtil.toJsonString(map)).main(new NetCall.Call() {



            @Override
            public Message ok(Message message) {
                String s = message.getDate().toString();
             /*   Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm")
                        .create();
*/
                o = JSONArray.parseArray(s, NewListBean.class);
                Log.d("NewsListActivity", "o:" + o);

                if (o.size()>0) {
                    if (pageUtils.isRefreshType()) {
                        newListAdapter.setData(o);
                    }else{
                        newListAdapter.upData(o);
                    }
                }
                return Message.ok();
            }
            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventImpl.RefreshInformation refreshInformation){
        newsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
            InvitePopupWindow.onDestroy();
            InvitePopupWindow.dismiss();
        }
        InvitePopupWindow = null;
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
