package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.WeekCountBean;

import java.util.List;

public class PoolAllAdapter extends RecyclerView.Adapter {
    private Context context;

    List<WeekCountBean.DataBean> dataBeans;

    public PoolAllAdapter(Context context, List<WeekCountBean.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoolAllHodler(LayoutInflater.from(context).inflate(R.layout.item_pool_reward,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        PoolAllHodler hodler= (PoolAllHodler) holder1;
        hodler.pool_all_num.setText(dataBeans.get(position).getDaily_count_release());
        hodler.pool_reward_num1.setText(dataBeans.get(position).getDaily_keep_release());
        hodler.pool_reward_num2.setText(dataBeans.get(position).getDaily_push_release());

    }

    public void setDate(  List<WeekCountBean.DataBean> dataBeans){
        if (dataBeans!=null) {
            this.dataBeans=dataBeans;
            this.notifyDataSetChanged();
        }
    }

    public void upDate(List<WeekCountBean.DataBean> dataBeans){
        if (dataBeans!=null) {
            this.dataBeans.addAll(dataBeans);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }
    class PoolAllHodler extends RecyclerView.ViewHolder {

        private final TextView pool_all_num;
        private final TextView pool_reward_num1;
        private final TextView pool_reward_num2;

        public PoolAllHodler(@NonNull View itemView) {
            super(itemView);
            pool_all_num = itemView.findViewById(R.id.pool_all_num);
            pool_reward_num1 = itemView.findViewById(R.id.pool_reward_num1);
            pool_reward_num2 = itemView.findViewById(R.id.pool_reward_num2);
        }
    }
}
