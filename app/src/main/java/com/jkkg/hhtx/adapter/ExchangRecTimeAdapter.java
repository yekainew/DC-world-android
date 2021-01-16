package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.ExchangeGroupBean;

import java.util.List;

public class ExchangRecTimeAdapter  extends RecyclerView.Adapter {
    private Context context;
    List<ExchangeGroupBean> list;
    private OnClick mOnClick;
    int p;
    public ExchangRecTimeAdapter(Context context, List<ExchangeGroupBean> list,int p) {
        this.context = context;
        this.list = list;
        this.p=p;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExchangRecTimeHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_qiu,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ExchangRecTimeHolder holder= (ExchangRecTimeHolder) holder1;
        holder.num.setText(position+1+"");


        holder.itemView.setTag(holder.num);
        if (list.get(position).isCheck()) {
            holder.num.setTextColor(Color.parseColor("#394748"));
            holder.bg.setImageResource(R.mipmap.new_item_qiu1);
        }else{
            holder.num.setTextColor(Color.parseColor("#57888E"));
            holder.bg.setImageResource(R.mipmap.new_item_qiu2);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(false);
                }

                list.get(position).setCheck(true);

                notifyDataSetChanged();

                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,list.get(position));
                }
            }
        });




    }

    public void setData(List<ExchangeGroupBean> list,int p){
        if (list!=null) {
            this.list=list;
            this.p=p;
            for (ExchangeGroupBean exchangeGroupBean : list) {
                exchangeGroupBean.setCheck(false);
            }
            list.get(p).setCheck(true);
            this.notifyDataSetChanged();

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ExchangRecTimeHolder extends RecyclerView.ViewHolder {

        private final ImageView bg;
        private final TextView num;

        public ExchangRecTimeHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            num = itemView.findViewById(R.id.num);
        }
    }

    public  interface OnClick{
        void setOnClick(View view,int position,ExchangeGroupBean bean);
    }

    public void setOnClickListener (OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
