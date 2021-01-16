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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jkkg.hhtx.R;
//import com.jkkg.hhtx.app.GlideApp;
import com.jkkg.hhtx.net.bean.BlockChainBean;
import com.jkkg.hhtx.net.bean.QuotesBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.DecimalFormatUtils;
import com.jkkg.hhtx.utils.TryParse;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.sql.SqlModel;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;

/**
 * 首页币种详情
 */
public class HomeItemAdapter extends RecyclerView.Adapter {
    private Context context;
    List<QuotesBean> beans;
    public HomeItemAdapter(Context context, List<QuotesBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeItemHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }
    private SqlModel sqlModel=null;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        HomeItemHolder holder = (HomeItemHolder) holder1;
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new CircleCrop());
        Glide.with(context).load(beans.get(position).getUrl()).error(R.mipmap.ic_coin).apply(requestOptions).into(holder.imgHead);
//        GlideApp.with(context).load(beans.get(position).getUrl()).error(R.mipmap.ic_coin).placeholder(R.mipmap.ic_coin).apply(requestOptions).into(holder.imgHead);
        String q_market = beans.get(position).getQ_market();
        String[] s = q_market.split("_");

        holder.textName.setText(s[0]);
        holder.upNum.setText(beans.get(position).getPrice()+"");
        double decimal = TryParse.getDouble(beans.get(position).getChange());
        BigDecimal bigDecimal = new BigDecimal(beans.get(position).getPrice_rmb()).setScale(3,BigDecimal.ROUND_UP);;
//        bigDecimal = bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP);
        holder.downNum.setText(String.format("≈￥%s", bigDecimal.stripTrailingZeros().toPlainString()));
        if(sqlModel==null){
            sqlModel = DataSave.context.getBean(Dao.class);
        }
        List<BlockCoinBean> select = sqlModel.selectList(new BlockCoinBean().setSymbol(beans.get(position).getTransaction_start()));
        if(!select.isEmpty()){
            for(BlockCoinBean coinBean:select) {
                coinBean.setPrice(new BigDecimal(beans.get(position).getPrice()));
                coinBean.setPrice_cny(bigDecimal);
                sqlModel.updata(coinBean);
            }
        }
        if(position==0) {
            BlockCoinBean usdt = sqlModel.select(new BlockCoinBean().setSymbol("USDT"));
            if (usdt != null) {
                usdt.setPrice(BigDecimal.ONE);
                usdt.setPrice_cny(bigDecimal.divide(new BigDecimal(beans.get(position).getPrice()), 4, BigDecimal.ROUND_HALF_UP));
                sqlModel.updata(usdt);
            }
        }
        String number = DecimalFormatUtils.formatNumber(decimal, "0.00");
        if (decimal >= 0) {
//            holder.tolerance.setBackgroundResource(R.color.green);
            holder.tolerance.setTextColor(Color.parseColor("#37B68A"));
            holder.tolerance.setText("+" + number + "%");
        } else {
//            holder.tolerance.setBackgroundResource(R.color.font_color_rose);
            holder.tolerance.setTextColor(Color.parseColor("#C83A4C"));
            holder.tolerance.setText(number + "%");
        }
    }

    public void setData(List<QuotesBean> beans) {
        if (beans != null) {
            this.beans = beans;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<QuotesBean> beans) {
        if (beans != null) {
            this.beans.addAll(beans);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }


    class HomeItemHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView upNum;
        private final TextView downNum;
        private final TextView tolerance;
        private final ImageView imgHead;

        public HomeItemHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            upNum = itemView.findViewById(R.id.up_num);
            downNum = itemView.findViewById(R.id.down_num);
            tolerance = itemView.findViewById(R.id.tolerance);
            imgHead = itemView.findViewById(R.id.img_head);
        }
    }
}
