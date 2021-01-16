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
import com.jkkg.hhtx.net.bean.WalletLog;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class RewardMoreLogAdapter extends RecyclerView.Adapter {
    private Context context;
    List<WalletLog> walletLogs;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardMoreLogHolder(LayoutInflater.from(context).inflate(R.layout.item_reward_more,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        RewardMoreLogHolder holder = (RewardMoreLogHolder) holder1;
        holder.reward_community.setText(walletLogs.get(position).getLog_explain());
        holder.reward_community_time.setText(DateFormatUtils.format(walletLogs.get(position).getLog_create_time(), Constants.TIME));
        holder.reward_community_num.setText(walletLogs.get(position).getLog_amount().stripTrailingZeros().toPlainString());
    }

    @Override
    public int getItemCount() {
        return walletLogs.size();
    }
    class RewardMoreLogHolder extends RecyclerView.ViewHolder {

        private final TextView reward_community;
        private final TextView reward_community_time;
        private final TextView reward_community_num;

        public RewardMoreLogHolder(@NonNull View itemView) {
            super(itemView);
            reward_community = itemView.findViewById(R.id.reward_community);
            reward_community_time = itemView.findViewById(R.id.reward_community_time);
            reward_community_num = itemView.findViewById(R.id.reward_community_num);
        }
    }
}
