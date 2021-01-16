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
import com.jkkg.hhtx.net.bean.ExchangeRewardBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;

public class ExchangeRewardAdapter  extends RecyclerView.Adapter {
    private Context context;

    List<ExchangeRewardBean> list;

    public ExchangeRewardAdapter(Context context, List<ExchangeRewardBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExchangeRewardHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_reward,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ExchangeRewardHolder holder= (ExchangeRewardHolder) holder1;

        holder.address.setText(list.get(position).getFrom_address());
        holder.time.setText(DateFormatUtils.format(list.get(position).getExchange_reward_create_time(), Constants.TIME));
        holder.text2.setText(list.get(position).getReward_num().stripTrailingZeros().toPlainString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ExchangeRewardHolder extends RecyclerView.ViewHolder {

        private final TextView address;
        private final TextView time;
        private final TextView text1;
        private final TextView text2;

        public ExchangeRewardHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
