package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.ExchangeConfBeans;
import com.liys.view.LineBottomProView;
import java.util.List;

/**
 * 兑换列表
 */
public class ExchangeListAdapter extends RecyclerView.Adapter {

    List<ExchangeConfBeans> exchangeConfBeans;
    private Context context;
//    int TYPE_ONE=0;
    int TYPE_TWO=1;
    public ExchangeListAdapter(List<ExchangeConfBeans> exchangeConfBeans, Context context) {
        this.exchangeConfBeans = exchangeConfBeans;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType==TYPE_ONE) {
//            return new ExchangeListHeadHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_progress_bar,null,false));
//        }else{
            return new ExchangeListHodler(LayoutInflater.from(context).inflate(R.layout.item_exchange,null,false));
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
//        int itemViewType = getItemViewType(position);
//        if (itemViewType==TYPE_ONE) {
//            ExchangeListHeadHolder hodler= (ExchangeListHeadHolder) holder1;
//            hodler.one_bili.setText(exchangeConfBeans.get(position).getExchange_conf_scale().stripTrailingZeros().toPlainString());
//            hodler.one_dc.setText(exchangeConfBeans.get(position).getThe_remaining_amount().stripTrailingZeros().toPlainString());
//        }else{
            ExchangeListHodler hodler= (ExchangeListHodler) holder1;
            hodler.one_bili.setText("1:"+exchangeConfBeans.get(position).getExchange_conf_scale().stripTrailingZeros().toPlainString());
            hodler.one_dc.setText(exchangeConfBeans.get(position).getThe_remaining_amount().stripTrailingZeros().toPlainString());
//        }
    }

    @Override
    public int getItemCount() {
        return exchangeConfBeans.size();
    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position==0) {
//            return TYPE_ONE;
//        }else{
//            return TYPE_TWO;
//        }
//    }

    class ExchangeListHodler extends RecyclerView.ViewHolder {

        private final TextView one_bili;
        private final TextView one_dc;

        public ExchangeListHodler(@NonNull View itemView) {
            super(itemView);
            one_bili = itemView.findViewById(R.id.one_bili);
            one_dc = itemView.findViewById(R.id.one_dc);
        }
    }
//
//    class ExchangeListHeadHolder extends RecyclerView.ViewHolder {
//
//        private final LineBottomProView bottom_pro_view;
//        private final TextView one_bili;
//        private final TextView one_dc;
//        public ExchangeListHeadHolder(@NonNull View itemView) {
//            super(itemView);
//            bottom_pro_view = itemView.findViewById(R.id.bottom_pro_view);
//            one_bili = itemView.findViewById(R.id.one_bili);
//            one_dc = itemView.findViewById(R.id.one_dc);
//        }
//    }
}
