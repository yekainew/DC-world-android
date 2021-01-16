package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.MyFinancesListBean;

import java.math.BigDecimal;
import java.util.List;

public class MyFinancesListAdapter extends RecyclerView.Adapter {
    private Context context;
    List<MyFinancesListBean> listBeans;
    private OnClick mOnClick;

    public MyFinancesListAdapter(Context context, List<MyFinancesListBean> listBeans) {
        this.context = context;
        this.listBeans = listBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFinancesListHolder(LayoutInflater.from(context).inflate(R.layout.item_manage_finances_my,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        MyFinancesListHolder holder= (MyFinancesListHolder) holder1;

        holder.buy_money.setText(new BigDecimal(listBeans.get(position).getAmount()).stripTrailingZeros().toPlainString()+listBeans.get(position).getPayment_currency_name());
        holder.name.setText(listBeans.get(position).getName());
        holder.time.setText(listBeans.get(position).getCreate_time());
        String annualized_rate = listBeans.get(position).getAnnualized_rate();
        String s = new BigDecimal(annualized_rate).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        holder.yeild_num.setText(s+"%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position);
                }
            }
        });
    }

    public void setData(List<MyFinancesListBean> beans){
        if (beans!=null) {
            this.listBeans=beans;
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return listBeans.size();
    }
    class MyFinancesListHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView buy_money;
        private final TextView yeild_num;
        private final TextView time;

        public MyFinancesListHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            buy_money = itemView.findViewById(R.id.buy_money);
            yeild_num = itemView.findViewById(R.id.yeild_num);
            time = itemView.findViewById(R.id.time);
        }
    }

    public interface OnClick{
        void setOnClick(View view,int position);
    }

    public void setOnClickListener (OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
