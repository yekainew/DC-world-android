package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AssetsBookAddBean;

import java.util.List;

public class AssetsBookAdapter extends RecyclerView.Adapter {
    private Context context;
    List<AssetsBookAddBean> list;

    public AssetsBookAdapter(Context context, List<AssetsBookAddBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssetsBookHolder(LayoutInflater.from(context).inflate(R.layout.item_wallet_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        AssetsBookHolder holder= (AssetsBookHolder) holder1;
        holder.address.setText(list.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AssetsBookHolder extends RecyclerView.ViewHolder {

        private final TextView address;

        public AssetsBookHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);

        }
    }

    public void upDate(List<AssetsBookAddBean> list){
        if (list!=null) {
            this.list.addAll(list);
            this.notifyDataSetChanged();

        }
    }

    public void setDate(List<AssetsBookAddBean> list){
        if (list!=null) {
            this.list=list;
            this.notifyDataSetChanged();
        }
    }
}
