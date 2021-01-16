package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.ExchangeBean1;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeAssetAdapter extends RecyclerView.Adapter {
    private Context context;
    List<ExchangeBean1> exchangeBean1s;
    private OnClick mOnClick;

    public ExchangeAssetAdapter(Context context, List<ExchangeBean1> exchangeBean1s) {
        this.context = context;
        this.exchangeBean1s = exchangeBean1s;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExchangeAssetHodler(LayoutInflater.from(context).inflate(R.layout.item_exchange_asset, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ExchangeAssetHodler hodler = (ExchangeAssetHodler) holder1;

        hodler.time.setText("共振时间:" + DateFormatUtils.format(exchangeBean1s.get(position).getExchange_create_time(), Constants.TIME));
        Integer state = exchangeBean1s.get(position).getState();
        if (state == 1) {
            //进行中
            hodler.type.setText("进行中");
            hodler.type.setTextColor(Color.parseColor("#FFB873"));
        } else if (state == 2) {
            //已经完成
            hodler.type.setText("成功");
            hodler.type.setTextColor(Color.parseColor("#16DC5C"));
            BigDecimal exchange_get_sum = exchangeBean1s.get(position).getExchange_get_sum();//得到

            BigDecimal exchange_return_num = exchangeBean1s.get(position).getExchange_return_num();//退回
            BigDecimal exchange_fee_sum = exchangeBean1s.get(position).getExchange_fee_sum();//手续费
            BigDecimal exchange_spend_sum = exchangeBean1s.get(position).getExchange_spend_sum();//花费了多少
            BigDecimal add = exchange_spend_sum.add(exchange_return_num);

            BigDecimal add1 = add.add(exchange_fee_sum);
            hodler.text1.setText("-"+add1.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());//总量

            BigDecimal divide = exchange_spend_sum.divide(add1, 4, BigDecimal.ROUND_UP);

            hodler.text6.setText("+"+exchange_get_sum.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());


        } else if (state == 3) {
            //部分完成
            hodler.type.setText("成功");
            hodler.type.setTextColor(Color.parseColor("#16DC5C"));

            BigDecimal exchange_get_sum = exchangeBean1s.get(position).getExchange_get_sum();//得到

            BigDecimal exchange_return_num = exchangeBean1s.get(position).getExchange_return_num();//退回
            BigDecimal exchange_fee_sum = exchangeBean1s.get(position).getExchange_fee_sum();//手续费
            BigDecimal exchange_spend_sum = exchangeBean1s.get(position).getExchange_spend_sum();//花费了多少
            BigDecimal add = exchange_spend_sum.add(exchange_return_num);

            BigDecimal add1 = add.add(exchange_fee_sum);
            hodler.text1.setText("-"+add1.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());//总量

            BigDecimal divide = exchange_spend_sum.divide(add1, 4, BigDecimal.ROUND_UP);

            hodler.text6.setText("+"+exchange_get_sum.setScale(3,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        } else {
            //失败
            hodler.type.setText("失败");
            hodler.type.setTextColor(Color.parseColor("#FF7383"));
        }

        hodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    if (exchangeBean1s.get(position).getState()==2) {
                        mOnClick.setOnClick(v,exchangeBean1s.get(position),0);
                    }else if (exchangeBean1s.get(position).getState()==3){
                        mOnClick.setOnClick(v,exchangeBean1s.get(position),0);
                    }else if (exchangeBean1s.get(position).getState()==4){
                        mOnClick.setOnClick(v,exchangeBean1s.get(position),2);
                    }else {
                        mOnClick.setOnClick(v,exchangeBean1s.get(position),1);
                    }
                }
            }
        });


    }

    public void setDate(List<ExchangeBean1> exchangeBean1s) {
        if (exchangeBean1s != null) {
            this.exchangeBean1s.addAll(exchangeBean1s);
            this.notifyDataSetChanged();
        }
    }

    public void upDate(List<ExchangeBean1> exchangeBean1s) {
        if (exchangeBean1s != null) {
            this.exchangeBean1s = exchangeBean1s;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return exchangeBean1s.size();
    }

    class ExchangeAssetHodler extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView type;
        private final TextView text1;
        private final TextView text6;
        private final TextView usdt_to_dc;
        private final TextView bc_name_tv;
        private final TextView bc_name_tv2;

        public ExchangeAssetHodler(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            type = itemView.findViewById(R.id.type);
            text1 = itemView.findViewById(R.id.text1);
            text6 = itemView.findViewById(R.id.text6);
            usdt_to_dc = itemView.findViewById(R.id.usdt_to_dc);
            bc_name_tv = itemView.findViewById(R.id.bc_name_tv);
            bc_name_tv2 = itemView.findViewById(R.id.bc_name_tv2);
        }
    }

    public interface OnClick{
        void setOnClick(View view,ExchangeBean1 exchangeBean1,int code);
    }

    public void setOnClickListener (OnClick clickListener){
        this.mOnClick=clickListener;
    }

}
