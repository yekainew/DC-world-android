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
import com.jkkg.hhtx.net.bean.UserHoldIncomeBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;



public class CheckinLogProfitListAdapter extends RecyclerView.Adapter {

    JSONArray o;
    private Context context;

    public CheckinLogProfitListAdapter(JSONArray o, Context context) {
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

        CheckinLogBean checkinLogBean = CheckinLogBean.newBean(CheckinLogBean.class, o.get(position));



        holder.profit_tv1.setText(DateFormatUtils.format(checkinLogBean.getCreate_time(),"yyyy-MM-dd HH:mm:ss"));
        holder.profit_tv2.setText(checkinLogBean.getAmount().stripTrailingZeros().toPlainString());

        if (checkinLogBean.getLog_type()==1) {
            holder.profit_tv.setText(R.string.string_sign);
        }else{
            holder.profit_tv.setText(R.string.string_bu_sign);
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
