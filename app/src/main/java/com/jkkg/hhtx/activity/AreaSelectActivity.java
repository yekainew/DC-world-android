package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.adapter.AreaHeaderAdapter;
import com.jkkg.hhtx.adapter.AreaSelectAdapter;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.net.bean.CitiesBean;
import com.jkkg.hhtx.utils.AppUtil;
import com.jkkg.hhtx.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.indexablerv.EntityWrapper;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;


/**
 * Description:
 * Created by oby on 07/30/2020 21:03
 * Email:oubaoyi@qq.com
 * 地区选择
 */
public class AreaSelectActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_tv_left)
    TextView toolbarTvLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.indexableLayout)
    IndexableLayout indexableLayout;

    private AreaSelectAdapter adapter;
    private List<CitiesBean> mData = new ArrayList<>();
    private List<CitiesBean> mDataCommon = new ArrayList<>();

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_area_select; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarTitle.setText(getString(R.string.string_select_country));
        indexableLayout = (IndexableLayout) findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
        // 前者Material Design风格右侧气泡 ， 后者 居中 IOS风格气泡
        // indexableLayout.setOverlayStyle_MaterialDesign(int Color) & setOverlayStyle_Center()
        // 多音字处理
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(this)));
        //indexableLayout.setComparator(null);
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        adapter = new AreaSelectAdapter(this);
        indexableLayout.setAdapter(adapter);
        mData = getmData();
        mDataCommon = getmCommonData();

        adapter.setDatas(mData, new IndexableAdapter.IndexCallback<CitiesBean>() {
            @Override
            public void onFinished(List<EntityWrapper<CitiesBean>> datas) {

            }
        });
        // set Center OverlayView
        indexableLayout.setOverlayStyle_Center();

        adapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CitiesBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CitiesBean entity) {
                switchArea(entity.getZh());
            }
        });
        //添加 HeaderView(常用)
        // impleHeaderAdapter mHeaderAdapter = new SimpleHeaderAdapter<>(adapter, "☆", "热门", mDataCommon);
        List<String> grideList = new ArrayList<>();
        grideList.add("");
        indexableLayout.addHeaderAdapter(new IndexableHeaderAdapter<String>("☆", getString(R.string.string_city), grideList) {
            @Override
            public int getItemViewType() {
                return 1;
            }

            @Override
            public Holder onCreateContentViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_area_header_rv, parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindContentViewHolder(RecyclerView.ViewHolder viewHolder, String s) {

            }

            class Holder extends RecyclerView.ViewHolder {
                RecyclerView rv_index;
                public Holder(View itemView) {
                    super(itemView);
                    rv_index = (RecyclerView) itemView.findViewById(R.id.rv_index);
                    rv_index.setNestedScrollingEnabled(false);
                    rv_index.setLayoutManager(new GridLayoutManager(getBaseContext(),3));
                    AreaHeaderAdapter areaHeaderAdapter=  new AreaHeaderAdapter();
                    rv_index.setAdapter(areaHeaderAdapter);
                    areaHeaderAdapter.setList(mDataCommon);
                    areaHeaderAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                            switchArea(mDataCommon.get(position).getZh());
                        }
                    });
                }
            }
        });
        // 显示真实索引
        //indexableLayout.showAllLetter(false);
    }



    private List<CitiesBean> getmData() {
        String jsonData = AppUtil.getJson(this, "country.json");
        List<CitiesBean> countryBeans= GsonUtil.jsonToList(jsonData, CitiesBean.class);
        return countryBeans;
    }

    private List<CitiesBean> getmCommonData() {
        String jsonData = AppUtil.getJson(this, "country_md.json");
        return GsonUtil.jsonToList(jsonData, CitiesBean.class);
    }

    private void switchArea(String name) {
        Intent intent=new Intent();
        intent.putExtra("name",name);
        setResult(RESULT_OK,intent);
        finish();
    }
}
