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
import com.jkkg.hhtx.net.bean.NodeEmancipationBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 奖励记录日志适配器
 *
 * @author Phoenix
 * @date 2020/11/26
 */
@Getter
@Setter
@Accessors(chain = true)
public class RewardLogAdapter extends RecyclerView.Adapter {
    private Context context;
    List<NodeEmancipationBean> nodeEmancipationBeans;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardLogHolder(LayoutInflater.from(context).inflate(R.layout.item_reward_record,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        RewardLogHolder holder= (RewardLogHolder) holder1;
        if (nodeEmancipationBeans.get(position).getType()==1) {
            holder.record_name.setText("共振奖励");
        }else{
            holder.record_name.setText("社区奖励");
        }

        holder.time.setText(DateFormatUtils.format(nodeEmancipationBeans.get(position).getCreate_time(), Constants.TIME));
        holder.number.setText(nodeEmancipationBeans.get(position).getAmount().setScale(4, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
    }

    @Override
    public int getItemCount() {
        return nodeEmancipationBeans.size();
    }

    class RewardLogHolder extends RecyclerView.ViewHolder {

        private final TextView record_name;
        private final TextView time;
        private final TextView number;

        public RewardLogHolder(@NonNull View itemView) {
            super(itemView);
            record_name = itemView.findViewById(R.id.record_name);
            time = itemView.findViewById(R.id.time);
            number = itemView.findViewById(R.id.number);
        }
    }
}
