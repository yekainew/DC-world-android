package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.UserHoldIncomeBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PoolProfitListAdapter extends RecyclerView.Adapter {

    List<UserHoldIncomeBean> o;
    private Context context;
    public String bc_name;

    public PoolProfitListAdapter(List<UserHoldIncomeBean> o, Context context,String bc_name) {
        this.o = o;
        this.context = context;
        this.bc_name=bc_name;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoolProfitListHolder(LayoutInflater.from(context).inflate(R.layout.item_newpool_yeild,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        PoolProfitListHolder holder= (PoolProfitListHolder) holder1;

        holder.profit_tv.setText(o.get(position).getIncome_reason());
        holder.wallet_bb_name.setText(bc_name);
        holder.profit_tv1.setText(DateFormatUtils.format(o.get(position).getIncome_create_time(),Constants.TIME));
        holder.profit_tv2.setText(new BigDecimal(o.get(position).getIncome_num()).stripTrailingZeros().toPlainString());
    }

    public void setData(List<UserHoldIncomeBean> o,String dc_name){
        if (o!=null) {
            this.o=o;
            this.bc_name=dc_name;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<UserHoldIncomeBean> o,String dc_name){
        if (o!=null) {

            this.o.addAll(o);
            this.bc_name=dc_name;
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return o.size();
    }

    class PoolProfitListHolder extends RecyclerView.ViewHolder {

        private final TextView profit_tv;
        private final TextView profit_tv1;
        private final TextView profit_tv2;
        private final TextView wallet_bb_name;

        public PoolProfitListHolder(@NonNull View itemView) {
            super(itemView);
            profit_tv = itemView.findViewById(R.id.profit_tv);
            profit_tv1 = itemView.findViewById(R.id.profit_tv1);
            profit_tv2 = itemView.findViewById(R.id.profit_tv2);
            wallet_bb_name = itemView.findViewById(R.id.wallet_bb_name);
        }
    }
}
