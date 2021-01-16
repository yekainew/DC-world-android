package com.jkkg.hhtx.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AnnouncementBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.hutool.core.util.StrUtil;

/**
 * q钱包管理列表
 */
public class WalletManagementListAdapter extends BaseQuickAdapter<UserWalletBean , BaseViewHolder> {
    String name;
    private OnStartClick mOnStartClick;

    public WalletManagementListAdapter(String name) {
        super(R.layout.item_mannage_wallet_right);
        this.name=name;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, UserWalletBean  string) {
        baseViewHolder.setText(R.id.name,string.getName())
                .setText(R.id.address,string.getAddress());

        TextView bangding = baseViewHolder.findView(R.id.bangding);
        TextView view = baseViewHolder.findView(R.id.wallet);
        LinearLayout main_lov = baseViewHolder.findView(R.id.main_lov);
        if (string.getName().equals(name)) {
            bangding.setText("主钱包");
            main_lov.setBackgroundResource(R.mipmap.icon_managebao);
            bangding.setVisibility(View.VISIBLE);
            baseViewHolder.itemView.setEnabled(false);

        }else{
            bangding.setVisibility(View.GONE);
            baseViewHolder.itemView.setEnabled(true);

            main_lov.setBackgroundResource(R.mipmap.new_bg_wallet_right_item);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnStartClick!=null) {
                    mOnStartClick.setOnStartClick(string.getName());
                }
            }
        });
       /* baseViewHolder.setText(R.id.multichain_name,string)
        .setText(R.id.multichain_money,string)
        .setText(R.id.multichain_usdt,string);*/
        //multichain_choose 选择钱包状态
    }

    public void upDate(String name){
        if (StrUtil.isNotBlank(name)) {
            this.name=name;
            this.notifyDataSetChanged();
        }
    }

    public interface OnStartClick{
        void setOnStartClick(String name);
    }

    public void setOnStartClickListener (OnStartClick clickListener){
        this.mOnStartClick=clickListener;
    }


}
