package com.jkkg.hhtx.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;
public class NoticeLineAdapter extends BannerAdapter<AnnouncementBean, NoticeLineAdapter.LineHolder> {

    public NoticeLineAdapter(List<AnnouncementBean> mDatas) {
        super(mDatas);
    }

    @Override
    public LineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new LineHolder(BannerUtils.getView(parent, R.layout.layout_notice_line));
    }

    @Override
    public void onBindView(LineHolder holder, AnnouncementBean data, int position, int size) {
        holder.tv_notice.setText(data.getTitle());
    }

    class LineHolder extends RecyclerView.ViewHolder {
        public TextView tv_notice;

        public LineHolder(@NonNull View view) {
            super(view);
            tv_notice=view.findViewById(R.id.tv_notice);
        }
    }
}
