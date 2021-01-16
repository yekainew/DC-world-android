package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.CitiesBean;


import org.jetbrains.annotations.NotNull;

public class AreaHeaderAdapter extends BaseQuickAdapter<CitiesBean,BaseViewHolder> {
    private int language;
    public AreaHeaderAdapter() {
        super(R.layout.layout_area_rv_item);
        language=Constants.getLanguageCode();
    }
    @Override
    protected void convert(@NotNull BaseViewHolder helper, CitiesBean item) {
        if(language==0){
            helper.setText(R.id.tv_name,item.getZh());
        }else{
            helper.setText(R.id.tv_name,item.getEn());
        }
    }
}