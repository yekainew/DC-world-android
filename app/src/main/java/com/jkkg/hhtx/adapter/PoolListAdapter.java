package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.PoolListBean;
import com.jkkg.hhtx.widget.ImageViewPlus;

import java.math.BigDecimal;
import java.util.List;

public class PoolListAdapter extends RecyclerView.Adapter {
    private Context context;
    List<PoolListBean> poolListBeans;
    private OnAddClick mOnAddClick;
    private OnClick mOnClick;

    public PoolListAdapter(Context context, List<PoolListBean> poolListBeans) {
        this.context = context;
        this.poolListBeans = poolListBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoolListHolder(LayoutInflater.from(context).inflate(R.layout.item_pool,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        PoolListHolder holder= (PoolListHolder) holder1;
        holder.self_informationCenter.setText(poolListBeans.get(position).getHold_area_name());
        holder.pool_yield_all.setText(poolListBeans.get(position).getIncome());
        String hold_num = poolListBeans.get(position).getHold_num();

        BigDecimal bigDecimal = new BigDecimal(hold_num);
        if (bigDecimal.compareTo(new BigDecimal("10000"))>0) {
            String s = bigDecimal.subtract(new BigDecimal("10000")).multiply(new BigDecimal("10")).add(new BigDecimal("10000")).stripTrailingZeros().toPlainString();
            holder.pool_hashrate.setText(s);
        }else{
            String s = bigDecimal.multiply(new BigDecimal("10")).stripTrailingZeros().toPlainString();
            holder.pool_hashrate.setText(s);
        }

        String min_invest_num = poolListBeans.get(position).getHoldAreaConfBean().getMin_invest_num();
        BigDecimal bigDecimal1 = new BigDecimal(min_invest_num);

        if (bigDecimal.compareTo(bigDecimal1)<0) {
            holder.message_dot.setImageResource(R.mipmap.icon_dot_red);
        }else{
            holder.message_dot.setImageResource(R.mipmap.icon_dot_green);
        }

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnAddClick!=null) {
                    mOnAddClick.setOnAddClick(view,position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(view,position);
                }
            }
        });
    }

    public void setData(List<PoolListBean> poolListBeans){
        if (poolListBeans!=null) {
            this.poolListBeans=poolListBeans;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<PoolListBean> poolListBeans){
        if (poolListBeans!=null) {
            this.poolListBeans.addAll(poolListBeans);
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return poolListBeans.size();
    }

    class PoolListHolder extends RecyclerView.ViewHolder {

        private final TextView self_informationCenter;
        private final TextView pool_yield_all;
        private final TextView pool_hashrate;
        private final Button btn_add;
        private final ImageView message_dot;

        public PoolListHolder(@NonNull View itemView) {
            super(itemView);
            self_informationCenter = itemView.findViewById(R.id.self_informationCenter);
            pool_yield_all = itemView.findViewById(R.id.pool_yield_all);
            pool_hashrate = itemView.findViewById(R.id.pool_hashrate);
            btn_add = itemView.findViewById(R.id.btn_add);
            message_dot = itemView.findViewById(R.id.message_dot);
        }
    }

    public interface OnAddClick {
        void setOnAddClick(View v,int position);
    }

    public void setOnAddClickListener(OnAddClick clickListener){
        this.mOnAddClick=clickListener;
    }

    public interface OnClick{
        void setOnClick(View v,int position);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
