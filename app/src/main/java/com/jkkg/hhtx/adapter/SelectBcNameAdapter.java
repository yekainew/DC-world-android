package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.sql.bean.CoinBean;

import java.util.List;

public class SelectBcNameAdapter extends RecyclerView.Adapter {
    private Context context;

    List<CoinBean> list;
    private OnClick mOnClick;

    public SelectBcNameAdapter(Context context, List<CoinBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectBcNameHolder(LayoutInflater.from(context).inflate(R.layout.bc_name_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        SelectBcNameHolder holder= (SelectBcNameHolder) holder1;
        holder.bc_name_item_tv.setText(list.get(position).getSymbol());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,list.get(position).getSymbol());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SelectBcNameHolder extends RecyclerView.ViewHolder {

        private final TextView bc_name_item_tv;

        public SelectBcNameHolder(@NonNull View itemView) {
            super(itemView);
            bc_name_item_tv = itemView.findViewById(R.id.bc_name_item_tv);

        }
    }
    public interface OnClick{
        void setOnClick(View view,int position,String name);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;

    }

}
