package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeBannerAdapter extends BannerAdapter<String, HomeBannerAdapter.LineHolder> {
    private Context mContext;
    public HomeBannerAdapter(List<String> mDatas,Context context) {
        super(mDatas);
        mContext = context;
    }

    @Override
    public LineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new LineHolder(BannerUtils.getView(parent, R.layout.layout_home_banner));
    }

    @Override
    public void onBindView(LineHolder holder, String data, int position, int size) {

        Glide.with(mContext)
                .load(data)
                .into(holder.img_banner);
    }

    class LineHolder extends RecyclerView.ViewHolder {
        public ImageView img_banner;

        public LineHolder(@NonNull View view) {
            super(view);
            img_banner=view.findViewById(R.id.img_banner);
        }
    }
}
