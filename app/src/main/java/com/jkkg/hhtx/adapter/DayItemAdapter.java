package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.LockCoinToEarnInterestBean;

import java.util.List;

public class DayItemAdapter extends RecyclerView.Adapter {
    private Context context;
    List<LockCoinToEarnInterestBean> list;
    private OnClick mOnClick;

    public DayItemAdapter(Context context, List<LockCoinToEarnInterestBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayItemHodler(LayoutInflater.from(context).inflate(R.layout.item_day,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        DayItemHodler hodler= (DayItemHodler) holder1;
        hodler.item_day_tv.setText(list.get(position).getHeaven()+context.getString(R.string.string_day));

        hodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(position,v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DayItemHodler extends RecyclerView.ViewHolder {
        private final TextView item_day_tv;
        public DayItemHodler(@NonNull View itemView) {
            super(itemView);
            item_day_tv = itemView.findViewById(R.id.item_day_tv);
        }
    }

    public interface OnClick{
        void setOnClick(int position,View view);
    }

    public void setOnClickListener (OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
