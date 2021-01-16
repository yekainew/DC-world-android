package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.GlideApp;
import com.jkkg.hhtx.net.bean.OtcWalletBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.mugui.base.base.Autowired;
import com.mugui.block.sql.BlockCoinBean;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.List;

public class AssetsAdapter extends RecyclerView.Adapter {
    private Context context;
    List<OtcWalletBean.ListBean> list;
    private OnClick mOnClick;
    List<BlockCoinBean> beans;
    String big;
    public AssetsAdapter(Context context, List<OtcWalletBean.ListBean> list,  List<BlockCoinBean> beans,String big) {
        this.context = context;
        this.list = list;
        this.beans=beans;
        this.big=big;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssetsHodler(LayoutInflater.from(context).inflate(R.layout.item_asset,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        AssetsHodler hodler= (AssetsHodler) holder1;
        hodler.asset_name.setText(list.get(position).getCurrency_name());
        hodler.asset_usable.setText(list.get(position).getUsable()+"");
        hodler.asset_freeze.setText(list.get(position).getFrozen()+"");
        hodler.asset_equivalent.setText("≈$ "+list.get(position).getUSDT());
        String currency_name = list.get(position).getCurrency_name();
        for (BlockCoinBean bean : beans) {
             if (bean.getSymbol().equals(currency_name)) {
                GlideApp.with(context).load(bean.getIcon_url()).into(hodler.asset_item_iv);
                hodler.asset_item_tv.setText(bean.getName());
                if (bean.getSymbol().equals("DC")) {
                    hodler.asset_item_tv.setText("Digital Circulation");
                    String usable = list.get(position).getUsable();

                    BigDecimal multiply = new BigDecimal(usable).multiply(new BigDecimal("0.3"));

                    hodler.asset_equivalent.setText("≈$ "+multiply.stripTrailingZeros().toPlainString());
                }
            }
        }
        hodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(view,position,list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AssetsHodler extends RecyclerView.ViewHolder{

        private final TextView asset_name;
        private final TextView asset_usable;
        private final TextView asset_freeze;
        private final TextView asset_equivalent;
        private final TextView asset_item_tv;
        private final ImageView asset_item_iv;

        public AssetsHodler(@NonNull View itemView) {
            super(itemView);
            //比重名称
            asset_name = itemView.findViewById(R.id.asset_name);
            //可用
            asset_usable = itemView.findViewById(R.id.asset_usable);
            //冻结
            asset_freeze = itemView.findViewById(R.id.asset_freeze);
            //折合
            asset_equivalent = itemView.findViewById(R.id.asset_equivalent);
            //图标
            asset_item_iv = itemView.findViewById(R.id.asset_item_iv);
            //全称
            asset_item_tv = itemView.findViewById(R.id.asset_item_tv);
        }
    }

    public void setDate(List<OtcWalletBean.ListBean> list, List<BlockCoinBean> beans){
        if (list!=null&&beans!=null) {
            this.list=list;
            this.beans=beans;
            this.notifyDataSetChanged();
        }
    }
    public interface OnClick {
        void setOnClick(View view,int position,OtcWalletBean.ListBean listBean);
    }
    public void setOnClickListener (OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
