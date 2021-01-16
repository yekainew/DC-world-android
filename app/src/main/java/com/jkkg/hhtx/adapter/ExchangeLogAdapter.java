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
import com.jkkg.hhtx.net.bean.ExchangeBean1;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeLogAdapter  extends RecyclerView.Adapter {
    private Context context;
    List<ExchangeBean1> exchangeBean1s;

    public ExchangeLogAdapter(Context context, List<ExchangeBean1> exchangeBean1s) {
        this.context = context;
        this.exchangeBean1s = exchangeBean1s;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExchangeLogHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_record,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        try {
            ExchangeLogHolder holder= (ExchangeLogHolder) holder1;
            holder.type.setText(exchangeBean1s.get(position).getRemark());
            holder.time.setText(DateFormatUtils.format(exchangeBean1s.get(position).getExchange_create_time(), Constants.TIME));
            holder.code.setText(exchangeBean1s.get(position).getExchange_get_sum().stripTrailingZeros().toPlainString());
        }catch (Exception e){
            e.printStackTrace();
        }
      /*  if (state==1) {
            holder.type.setText("进行中");
        }else if (state==2){
            holder.type.setText("已经完成");
        }else if (state==3){
            holder.type.setText("部分完成");
        }else{
            holder.type.setText("失败");
        }*/
    }

    @Override
    public int getItemCount() {
        return exchangeBean1s.size();
    }

    class ExchangeLogHolder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView code;
        private final TextView type;

        public ExchangeLogHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            code = itemView.findViewById(R.id.code);
            type = itemView.findViewById(R.id.type);
        }
    }
}
