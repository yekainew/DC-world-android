package com.jkkg.hhtx.adapter;

import android.content.Context;
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
import com.jkkg.hhtx.net.bean.MyTramListBean;
import com.jkkg.hhtx.utils.CHSUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 我的团队
 */
public class MineTeamAdapter extends RecyclerView.Adapter {
    List<MyTramListBean> beans;
    private Context context;

    public MineTeamAdapter(List<MyTramListBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MineTeamHolder(LayoutInflater.from(context).inflate(R.layout.item_team, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        MineTeamHolder holder= (MineTeamHolder) holder1;
        String ghs = CHSUtils.ghs(new BigDecimal(beans.get(position).getSelf_hold_num()));
        holder.user_num.setText(ghs);
        holder.user_uid.setText(beans.get(position).getAddress());
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new CircleCrop());

        if (beans.get(position).getUser_img()!=null) {
            Glide.with(context).load(beans.get(position).getUser_img()).apply(requestOptions).into(holder.user_head);
        }else{
            Glide.with(context).load(R.mipmap.icon_user_head1).apply(requestOptions).into(holder.user_head);
        }

    }

    public void setData(List<MyTramListBean> beans){
        if (beans!=null) {
            this.beans=beans;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<MyTramListBean> beans){
        if (beans!=null) {
            this.beans.addAll(beans);
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return beans.size();
    }
    static class MineTeamHolder extends RecyclerView.ViewHolder {

        private final ImageView user_head;
        private final TextView user_name;
        private final TextView user_uid;
        private final TextView user_num;

        public MineTeamHolder(@NonNull View itemView) {
            super(itemView);
            user_head = itemView.findViewById(R.id.user_head);
            user_name = itemView.findViewById(R.id.user_name);
            user_uid = itemView.findViewById(R.id.user_uid);
            user_num = itemView.findViewById(R.id.user_num);
        }
    }
}
