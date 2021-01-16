package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.ActivationNumConfBean;

import java.util.List;

public class ActivationNumAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ActivationNumConfBean> list;
    private OnClick mOnClick;

    public ActivationNumAdapter(Context context, List<ActivationNumConfBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivationNumHolder(LayoutInflater.from(context).inflate(R.layout.item_jihuonum_buy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ActivationNumHolder holder= (ActivationNumHolder) holder1;
        holder.number.setText(list.get(position).getNum());

        if (list.get(position).isCheckd()) {
            holder.number_lin.setBackgroundResource(R.mipmap.icon_num_bck);
            holder.number.setTextColor(Color.parseColor("#000000"));
        }else{
            holder.number_lin.setBackgroundResource(R.mipmap.new_button_jihuobuy);
            holder.number.setTextColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setTag(holder.number);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheckd(false);
                }

                list.get(position).setCheckd(true);
                notifyDataSetChanged();

                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ActivationNumHolder extends RecyclerView.ViewHolder {

        private final TextView number;
        private final RelativeLayout number_lin;

        public ActivationNumHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            number_lin = itemView.findViewById(R.id.number_lin);
        }
    }

    public interface OnClick{
        void setOnClick(View view,int p,ActivationNumConfBean bean);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;


    }
}

