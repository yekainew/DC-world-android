package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;

import org.jetbrains.annotations.NotNull;

/**
 * 我的公告
 * @author admin6
 */
public class MineNoticeAdapter extends BaseQuickAdapter<AnnouncementBean, BaseViewHolder> {
    public MineNoticeAdapter() {
        super(R.layout.item_notice);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AnnouncementBean announcementBean) {
        baseViewHolder.setText(R.id.notice_title,announcementBean.getTitle())
        .setText(R.id.notice_text,announcementBean.getContent());
    }
}
