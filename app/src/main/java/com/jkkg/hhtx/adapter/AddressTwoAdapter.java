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
import com.jkkg.hhtx.sql.bean.AddressBookBean;
import com.jkkg.hhtx.sql.bean.CoinBean;

import java.util.List;

public class AddressTwoAdapter extends RecyclerView.Adapter {
    private Context context;

    List<AssetsBookAddBean> list;
    private OnClick mOnClick;

    public AddressTwoAdapter(Context context,  List<AssetsBookAddBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressTwoHolder(LayoutInflater.from(context).inflate(R.layout.bc_name_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        AddressTwoHolder holder= (AddressTwoHolder) holder1;
        holder.bc_name_item_tv.setText(list.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,list.get(position).getAddress());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AddressTwoHolder extends RecyclerView.ViewHolder {

        private final TextView bc_name_item_tv;

        public AddressTwoHolder(@NonNull View itemView) {
            super(itemView);
            bc_name_item_tv = itemView.findViewById(R.id.bc_name_item_tv);

        }
    }
    public interface OnClick{
        void setOnClick(View view, int position, String name);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;

    }

}
