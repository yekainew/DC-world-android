package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;


public class AssetsDetailFromAdapter extends RecyclerView.Adapter {
    private Context context;
    List<AssetsLogBean> o;
    String addressself;
    public AssetsDetailFromAdapter(Context context, List<AssetsLogBean> o, String addressself) {
        this.context = context;
        this.o = o;
        this.addressself=addressself;
    }


    private OnClick mOnClick;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssetsDetailFromHolder(LayoutInflater.from(context).inflate(R.layout.item_asset_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        AssetsDetailFromHolder holder= (AssetsDetailFromHolder) holder1;


        if (o.get(position).getTo()!=null) {
            if (o.get(position).getTo().equals(addressself)) {
                BigDecimal log_amount = o.get(position).getNum();
                String s = log_amount.stripTrailingZeros().toPlainString();
                holder.asset_usable1.setText("+"+s+"");
                holder.asset_usable1.setTextColor(Color.parseColor("#73F4FF"));
                holder.asset_item_iv.setImageResource(R.mipmap.icon_ron);
                holder.asset_name.setText(o.get(position).getFrom());
            }else{
                BigDecimal log_amount = o.get(position).getNum();
                String s = log_amount.stripTrailingZeros().toPlainString();
                holder.asset_usable1.setText("-"+s+"");
                holder.asset_usable1.setTextColor(Color.parseColor("#16DC5C"));
                holder.asset_item_iv.setImageResource(R.mipmap.new_icon_asset_zhuanru);

                holder.asset_name.setText(o.get(position).getTo());
            }
        }
        holder.asset_equivalent1.setText(DateFormatUtils.format(o.get(position).getTime(),"yyyy-MM-dd HH:mm:ss"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position,o.get(position));
                }
            }
        });
    }

    public void setData(List<AssetsLogBean> o){
        if (o!=null) {
            this.o=o;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<AssetsLogBean> o){
        if (o!=null) {
            this.o.addAll(o);
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return o.size();
    }
    class AssetsDetailFromHolder extends RecyclerView.ViewHolder {

        private final TextView asset_usable1;
//        private final TextView asset_freeze1;
        private final TextView asset_equivalent1;
        private final TextView asset_name;
        private final ImageView asset_item_iv;

        public AssetsDetailFromHolder(@NonNull View itemView) {
            super(itemView);
            //数量
            asset_usable1 = itemView.findViewById(R.id.asset_usable);
            //状态
//            asset_freeze1 = itemView.findViewById(R.id.asset_freeze1);
            //时间
            asset_equivalent1 = itemView.findViewById(R.id.asset_item_tv);
            //状态
            asset_name = itemView.findViewById(R.id.asset_name);
            asset_item_iv = itemView.findViewById(R.id.asset_item_iv);
        }
    }

    public interface OnClick{
        void setOnClick(View view,int position,AssetsLogBean bean);
    }

    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
