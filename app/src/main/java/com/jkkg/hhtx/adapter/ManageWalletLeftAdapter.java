package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.GlideApp;
import com.mugui.block.sql.BlockChainBean;

import java.util.List;

public class ManageWalletLeftAdapter extends RecyclerView.Adapter {
    private Context context;
    List<BlockChainBean> blockChainBeans;
    public ManageWalletLeftAdapter(Context context,List<BlockChainBean> blockChainBeans) {
        this.context = context;
        this.blockChainBeans=blockChainBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManageWalletLeftHolder(LayoutInflater.from(context).inflate(R.layout.item_mannage_wallet_left_choose,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ManageWalletLeftHolder holder= (ManageWalletLeftHolder) holder1;

        if (blockChainBeans.get(position).getIs_open()) {
            GlideApp.with(context).load(Integer.parseInt(blockChainBeans.get(position).getBc_down_icon())).into(holder.bi_img_show);
        }else{
            GlideApp.with(context).load(Integer.parseInt(blockChainBeans.get(position).getBc_up_icon())).into(holder.bi_img_show);
        }

    }

    @Override
    public int getItemCount() {
        return blockChainBeans.size();
    }

    class ManageWalletLeftHolder extends RecyclerView.ViewHolder {

        private final ImageView bi_img_show;

        public ManageWalletLeftHolder(@NonNull View itemView) {
            super(itemView);
            bi_img_show = itemView.findViewById(R.id.bi_img_show);
        }
    }
}
