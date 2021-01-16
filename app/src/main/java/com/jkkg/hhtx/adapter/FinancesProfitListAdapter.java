package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.alibaba.fastjson.JSONArray;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.CheckinLogBean;
import com.jkkg.hhtx.net.bean.FinancialIncomeBean;

import org.apache.commons.lang3.time.DateFormatUtils;



public class FinancesProfitListAdapter extends RecyclerView.Adapter {

    JSONArray o;
    private Context context;

    public FinancesProfitListAdapter(JSONArray o, Context context) {
        this.o = o;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoolProfitListHolder(LayoutInflater.from(context).inflate(R.layout.item_poolprofit,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {

        PoolProfitListHolder holder= (PoolProfitListHolder) holder1;

        FinancialIncomeBean checkinLogBean = FinancialIncomeBean.newBean(FinancialIncomeBean.class, o.get(position));

        holder.profit_tv1.setText(DateFormatUtils.format(checkinLogBean.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));
        holder.profit_tv2.setText(checkinLogBean.getDay_income().stripTrailingZeros().toPlainString());

        if (checkinLogBean.getType()==1) {
            holder.profit_tv.setText(R.string.string_touzi_yeild);
        }else if (checkinLogBean.getType()==2){
            holder.profit_tv.setText(R.string.string_join_yeild);
        }else{
            holder.profit_tv.setText(R.string.string_xiaoqu_yeild);
        }
    }

    public void setData(JSONArray o){
        if (o!=null) {
            this.o=o;
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

        public PoolProfitListHolder(@NonNull View itemView) {
            super(itemView);
            profit_tv = itemView.findViewById(R.id.profit_tv);
            profit_tv1 = itemView.findViewById(R.id.profit_tv1);
            profit_tv2 = itemView.findViewById(R.id.profit_tv2);
        }
    }
}
