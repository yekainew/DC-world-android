package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.net.bean.FeedbackBean;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;



/**
 * 我的留言列表
 * @author admin6
 */
public class MineFeedbackAdapter extends BaseQuickAdapter<FeedbackBean, BaseViewHolder> {
    public MineFeedbackAdapter() {
        super(R.layout.item_feedback);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FeedbackBean announcementBean) {
        baseViewHolder.setText(R.id.feedback_text,announcementBean.getContent())
        .setText(R.id.feedback_time, DateFormatUtils.format(announcementBean.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));

    }
}
