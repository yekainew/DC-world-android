package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.TimeDayBean;
import com.jkkg.hhtx.widget.ImageViewPlus;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;

import cn.hutool.core.date.DateTime;


public class SigninListAdapter extends RecyclerView.Adapter {
    private Context context;
    List<TimeDayBean> list;
    private OnClick mOnClick;


    public SigninListAdapter(Context context, List<TimeDayBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SigninListHolder(LayoutInflater.from(context).inflate(R.layout.item_sign,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        SigninListHolder holder= (SigninListHolder) holder1;
        holder.xingqi1.setText(list.get(position).getName());
        holder.riqi1.setText(DateFormatUtils.format(list.get(position).getId(),"MM/dd"));

        if (list.get(position).getDay() =='1') {
            holder.ture.setVisibility(View.VISIBLE);
        }else if (list.get(position).getDay()=='2'){
            holder.reissue.setVisibility(View.VISIBLE);
        }

        holder.reissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(view,position);
                }
            }
        });
    }

    public void setData(List<TimeDayBean> list){
        if (list!=null) {
            this.list=list;
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class SigninListHolder extends RecyclerView.ViewHolder {

        private final TextView xingqi1;
        private final TextView riqi1;
        private final ImageView ture;
        private final TextView reissue;

        public SigninListHolder(@NonNull View itemView) {
            super(itemView);
            xingqi1 = itemView.findViewById(R.id.xingqi1);
            riqi1 = itemView.findViewById(R.id.riqi1);
            ture = itemView.findViewById(R.id.ture);
            reissue = itemView.findViewById(R.id.reissue);
        }
    }

    public interface OnClick{
        void setOnClick(View view,int position);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
