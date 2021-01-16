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
import com.jkkg.hhtx.net.bean.WalletLog;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币日志适配器
 *
 * @author Phoenix
 * @date 2020/11/26
 */
public class CurrencyLogAdapter extends RecyclerView.Adapter {

    List<WalletLog> walletLogs;
    private Context context;

    public CurrencyLogAdapter(List<WalletLog> walletLogs, Context context) {
        this.walletLogs = walletLogs;
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

        holder.profit_tv.setText(walletLogs.get(position).getLog_explain());
        holder.profit_tv1.setText(DateFormatUtils.format(walletLogs.get(position).getLog_create_time(), Constants.TIME));
        holder.profit_tv2.setText(walletLogs.get(position).getLog_type()+walletLogs.get(position).getLog_amount().stripTrailingZeros().toPlainString());
    }

    public void setData(List<WalletLog> walletLogs){
        if (walletLogs!=null) {
            this.walletLogs=walletLogs;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return walletLogs.size();
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
