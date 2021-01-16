package com.jkkg.hhtx.fragment;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.NewsListActivity;
import com.jkkg.hhtx.adapter.NewListAdapter;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.net.bean.NewListBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.ImageUtils;
import com.jkkg.hhtx.utils.PageUtils;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * 资讯Fragment
 */
public class NewListFragment extends BaseFragment {
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

    public NewListFragment() {
        // Required empty public constructor
    }

    public static NewListFragment newInstance() {

        Bundle args = new Bundle();

        NewListFragment fragment = new NewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        pageUtils = new PageUtils(noticeSmart);
        newListAdapter = new NewListAdapter(new ArrayList<>(), mContext);
        noticeRec.setLayoutManager(new LinearLayoutManager(mContext));
        noticeRec.setAdapter(newListAdapter);
        pageUtils.setOnPageRefresh(new PageUtils.OnPageRefresh() {
            @Override
            public void onRefresh() {
                newsList();
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
                        RxPermissions rxPermissions = new RxPermissions(getActivity());
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

    @Override
    public void onResume() {
        super.onResume();
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
    public void onStop() {
        super.onStop();
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
