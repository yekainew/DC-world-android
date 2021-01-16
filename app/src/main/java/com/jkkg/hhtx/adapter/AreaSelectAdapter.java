package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.CitiesBean;

import me.yokeyword.indexablerv.IndexableAdapter;


public class AreaSelectAdapter extends IndexableAdapter<CitiesBean> {
    private LayoutInflater mInflater;
    private int language;
    public AreaSelectAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        language= Constants.getLanguageCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.layout_area_header, parent, false);
        return new IndexVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.layout_area_item, parent, false);
        return new ContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        IndexVH vh = (IndexVH) holder;
        vh.tv.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, CitiesBean entity) {
        ContentVH vh = (ContentVH) holder;
        if(language==0){
            vh.tv.setText(entity.getZh());
        }else{
            vh.tv.setText(entity.getEn());
        }
    }

    private class IndexVH extends RecyclerView.ViewHolder {
        TextView tv;
        public IndexVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    private class ContentVH extends RecyclerView.ViewHolder {
        TextView tv;
        public ContentVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}