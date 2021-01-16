package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.PowerRankListBean;
import com.jkkg.hhtx.utils.CHSUtils;

import java.math.BigDecimal;
import java.util.List;

public class PowerRankListAdapter extends RecyclerView.Adapter {
    private Context context;
    List<PowerRankListBean> o;

    public PowerRankListAdapter(Context context, List<PowerRankListBean> o) {
        this.context = context;
        this.o = o;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PowerRankListHolder(LayoutInflater.from(context).inflate(R.layout.item_chibi_paiming,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        PowerRankListHolder holder= (PowerRankListHolder) holder1;

//        holder.ranking.setText((position + 1) + "");
        holder.address.setText(o.get(position).getBc_address()+"");
        String ghs = CHSUtils.ghs(new BigDecimal(o.get(position).getHold_num()));
        holder.num.setText(ghs+"");
    }

    public void setData( List<PowerRankListBean> o){
        if (o!=null) {
            this.o=o;
            this.notifyDataSetChanged();
        }
    }

    public void upData( List<PowerRankListBean> o){
        if (o!=null) {
            this.o.addAll(o);
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return o.size();
    }

    class PowerRankListHolder extends RecyclerView.ViewHolder {

        private final TextView ranking;
        private final TextView address;
        private final TextView num;

        public PowerRankListHolder(@NonNull View itemView) {
            super(itemView);
            ranking = itemView.findViewById(R.id.ranking);
            address = itemView.findViewById(R.id.address);
            num = itemView.findViewById(R.id.num);
        }
    }
}
