package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;

import org.jetbrains.annotations.NotNull;

/**
 * 收益列表
 * @author admin6
 */
public class PoolYieldListAdapter extends BaseQuickAdapter<AnnouncementBean, BaseViewHolder> {
    public PoolYieldListAdapter() {
        super(R.layout.item_yield_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AnnouncementBean announcementBean) {
        baseViewHolder.setText(R.id.yield_time,announcementBean.getTitle())
        .setText(R.id.yield_number,announcementBean.getContent());
    }
}
