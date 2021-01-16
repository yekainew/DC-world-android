package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.sql.bean.AddressBookBean;

import java.util.List;

public class MineAddressBookAdapter extends RecyclerView.Adapter {
    private Context context;
    List<AddressBookBean> addressBookBeans;
    private OnClick mOnClick;

    public MineAddressBookAdapter(Context context, List<AddressBookBean> addressBookBeans) {
        this.context = context;
        this.addressBookBeans = addressBookBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MineAddressBookHolder(LayoutInflater.from(context).inflate(R.layout.item_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        MineAddressBookHolder holder= (MineAddressBookHolder) holder1;

        holder.name.setText(addressBookBeans.get(position).getContacts());
        holder.beizhu.setText(addressBookBeans.get(position).getBei_zhu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,addressBookBeans.get(position));
                }
            }
        });

    }

    public void setDate(List<AddressBookBean> addressBookBeans){
        if (addressBookBeans!=null) {
            this.addressBookBeans=addressBookBeans;
            this.notifyDataSetChanged();
        }
    }

    public void upDate(List<AddressBookBean> addressBookBeans){
        if (addressBookBeans!=null) {
            this.addressBookBeans.addAll(addressBookBeans);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return addressBookBeans.size();
    }

    class MineAddressBookHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView beizhu;

        public MineAddressBookHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            beizhu = itemView.findViewById(R.id.beizhu);
        }
    }

    public interface OnClick{
        void setOnClick(View view,int position,AddressBookBean bookBean);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
