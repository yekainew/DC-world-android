package com.jkkg.hhtx.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.BlockChainTransactionBean;
import com.jkkg.hhtx.net.bean.ExchangeBean;
import com.jkkg.hhtx.net.bean.TransferBean;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.mugui.base.bean.JsonBean;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * 账本条目
 */
public class BookAdapter extends BaseQuickAdapter<JsonBean, BaseViewHolder> {
    //0充值 1提币 2转账 3兑换
    private int type;

    public BookAdapter(int type) {
        super(R.layout.item_book);
        this.type=type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, JsonBean baseItem) {
        if(type==0){
            AssetsLogBean item= (AssetsLogBean) baseItem;
            baseViewHolder.setText(R.id.tv_coin_num,item.getNum().stripTrailingZeros().toPlainString());
            baseViewHolder.setText(R.id.tv_count,item.getFrom());
            baseViewHolder.setText(R.id.book_status, R.string.string_shou);
            baseViewHolder.setText(R.id.tv_time, DateFormatUtils.format(item.getTime(),"yyyy-MM-dd HH:mm:ss"));
            baseViewHolder.setText(R.id.tv_coin,item.getSymbol());
        }else if(type==1){
            AssetsLogBean item= (AssetsLogBean) baseItem;
            baseViewHolder.setText(R.id.tv_coin_num,item.getNum().stripTrailingZeros().toPlainString());
            baseViewHolder.setText(R.id.tv_count,item.getTo());
            baseViewHolder.setText(R.id.tv_time, DateFormatUtils.format(item.getTime(),"yyyy-MM-dd HH:mm:ss"));
            baseViewHolder.setText(R.id.tv_coin,item.getSymbol());
        }else if(type==2){
            AssetsLogBean item= (AssetsLogBean) baseItem;
            baseViewHolder.setText(R.id.tv_coin_num,item.getNum().stripTrailingZeros().toPlainString());
            baseViewHolder.setText(R.id.tv_count,item.getTo_phone());
            baseViewHolder.setText(R.id.tv_time, DateFormatUtils.format(item.getTime(),"yyyy-MM-dd HH:mm:ss"));
            baseViewHolder.setText(R.id.tv_coin,item.getSymbol());
        }else{
            ExchangeBean item= (ExchangeBean) baseItem;
            baseViewHolder.setText(R.id.tv_coin_num,new BigDecimal(item.getExchange_get_sum()).stripTrailingZeros().toPlainString());
            baseViewHolder.setText(R.id.tv_count, Constants.getUserPhone());
            baseViewHolder.setText(R.id.book_status, R.string.string_mine_exchange);
            baseViewHolder.setText(R.id.tv_time, item.getExchange_create_time());
            baseViewHolder.setText(R.id.tv_coin,item.get().getString("exchange_conf_get"));
        }
    }
}
