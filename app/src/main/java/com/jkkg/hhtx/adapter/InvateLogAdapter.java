package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.UserAssociation;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.mugui.base.bean.JsonBean;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;



public class InvateLogAdapter extends BaseQuickAdapter<UserAssociation, BaseViewHolder> {


   public InvateLogAdapter(){
       super(R.layout.item_activation_list);
   }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, UserAssociation jsonBean) {
        baseViewHolder.setText(R.id.time, DateFormatUtils.format(jsonBean.getUser_association_create_time(),"yyyy-MM-dd HH:mm:ss"))
                .setText(R.id.code,jsonBean.getAddress())
                ;

            baseViewHolder.setText(R.id.type,"已完成");
    }
}
