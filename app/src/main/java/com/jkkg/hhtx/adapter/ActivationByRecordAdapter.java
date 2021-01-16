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
import com.jkkg.hhtx.net.bean.WalletLogBeans;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

public class ActivationByRecordAdapter extends RecyclerView.Adapter {
    List<WalletLogBeans> logBeans;
    private Context context;

    public ActivationByRecordAdapter(List<WalletLogBeans> logBeans, Context context) {
        this.logBeans = logBeans;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivationByRecordHolder(LayoutInflater.from(context).inflate(R.layout.item_jihuo_buynum,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ActivationByRecordHolder holder= (ActivationByRecordHolder) holder1;
        long log_create_time = logBeans.get(position).getLog_create_time();
        Date date=new Date(log_create_time);
        holder.time_day.setText(DateFormatUtils.format(date, Constants.TIME));
        holder.jihuo_num.setText(logBeans.get(position).getLog_amount());

    }

    @Override
    public int getItemCount() {
        return logBeans.size();
    }

    class ActivationByRecordHolder extends RecyclerView.ViewHolder {

        private final TextView time_day;
        private final TextView jihuo_num;
        private final TextView jihuo_xiaohao;
        private final TextView jihuo_type;

        public ActivationByRecordHolder(@NonNull View itemView) {
            super(itemView);
            time_day = itemView.findViewById(R.id.time_day);
            jihuo_num = itemView.findViewById(R.id.jihuo_num);
            jihuo_xiaohao = itemView.findViewById(R.id.jihuo_xiaohao);
            jihuo_type = itemView.findViewById(R.id.jihuo_type);

        }
    }
}
