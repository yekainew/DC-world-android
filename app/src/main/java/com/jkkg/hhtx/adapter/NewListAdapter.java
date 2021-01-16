package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.NewListBean;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.widget.VerticalDashView;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;

import cn.hutool.core.util.StrUtil;

public class NewListAdapter extends RecyclerView.Adapter {
    List<NewListBean> o;
    private Context context;
    private OnUpClick mOnUpClick;
    private OnDownClick mOnDownClick;
    private OnShareClick mOnShareClick;

    public NewListAdapter(List<NewListBean> o, Context context) {
        this.o = o;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewListHodler(LayoutInflater.from(context).inflate(R.layout.item_news,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        NewListHodler hodler= (NewListHodler) holder1;
        hodler.new_text.setText(o.get(position).getContent());
        hodler.new_title.setText(o.get(position).getTitle());
        hodler.new_time.setText(context.getString(R.string.string_fayuyu) + DateFormatUtils.format( o.get(position).getCreate_time(),"yyyy-MM-dd HH:mm:ss"));
        hodler.user_phone.setText(o.get(position).getUserAddress());
        if (o.get(position).getHeadPortrait()!=null){
            //从后台拿
            Glide.with(context).load(o.get(position).getHeadPortrait()).into(hodler.user_head);
        }else {
            //头像为空，默认
            Glide.with(context).load(R.mipmap.icon_user_head1).into(hodler.user_head);
        }
        hodler.new_up_num.setText(o.get(position).getBullish()+"");
        hodler.new_down_num.setText(o.get(position).getBearish()+"");

        String imgs = o.get(position).getImgs();
        if (StrUtil.isNotBlank(imgs)) {
            JSONArray objects = JSONArray.parseArray(imgs);
            if (objects.size()>0) {
                String string = objects.getString(0);
                if (StrUtil.isNotBlank(string)) {
                    Glide.with(context).load(string).into(hodler.add_img1);
                    hodler.add_img1.setVisibility(View.VISIBLE);
                }
            }
            if (objects.size()>1) {
                String string1 = objects.getString(1);
                if (StrUtil.isNotBlank(string1)) {
                    Glide.with(context).load(string1).into(hodler.add_img2);
                    hodler.add_img2.setVisibility(View.VISIBLE);
                }
            }
            if (objects.size()>2) {
                String string2 = objects.getString(2);
                if (StrUtil.isNotBlank(string2)) {
                    Glide.with(context).load(string2).into(hodler.add_img3);
                    hodler.add_img3.setVisibility(View.VISIBLE);
                }
            }
            if (objects.size()>3) {
                String string3 = objects.getString(3);
                if (StrUtil.isNotBlank(string3)) {
                    Glide.with(context).load(string3).into(hodler.add_img4);
                    hodler.add_img4.setVisibility(View.VISIBLE);
                }
            }

            if (objects.size()==0){
                hodler.add_lin.setVisibility(View.GONE);
            }
        }


        hodler.new_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnUpClick!=null) {
                    mOnUpClick.setOnUpClick(v,position);
                }
            }
        });

        hodler.new_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDownClick!=null) {
                    mOnDownClick.setOnDownClick(v,position);
                }
            }
        });
        hodler.new_jion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnShareClick!=null) {
                    mOnShareClick.setOnShareClick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return o.size();
    }

    public void setData(List<NewListBean> o ){
        if (o!=null) {
            this.o=o;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<NewListBean> o){
        if (o!=null) {
            this.o.addAll(o);
            this.notifyDataSetChanged();
        }
    }
    class NewListHodler extends RecyclerView.ViewHolder {

        private final TextView new_time;
        private final TextView new_title;
        private final TextView new_text;
        private final TextView new_up_num;
        private final TextView new_down_num;
        private final TextView user_phone;
        private final LinearLayout new_up;
        private final LinearLayout new_down;
        private final LinearLayout new_jion;
        private VerticalDashView vertical_line;
        private ImageView user_head;
        private ImageView add_img1;
        private ImageView add_img2;
        private ImageView add_img3;
        private ImageView add_img4;
        private LinearLayout add_lin;

        public NewListHodler(@NonNull View itemView) {
            super(itemView);
            new_time = itemView.findViewById(R.id.new_time);
            new_title = itemView.findViewById(R.id.new_title);
            new_text = itemView.findViewById(R.id.new_text);
            new_up_num = itemView.findViewById(R.id.new_up_num);
            new_down_num = itemView.findViewById(R.id.new_down_num);
            new_up = itemView.findViewById(R.id.new_up);
            new_down = itemView.findViewById(R.id.new_down);
            vertical_line=itemView.findViewById(R.id.vertical_line);
            new_jion=itemView.findViewById(R.id.new_jion);
            user_head=itemView.findViewById(R.id.user_head);
            user_phone=itemView.findViewById(R.id.user_phone);
            add_img1=itemView.findViewById(R.id.add_img1);
            add_img2=itemView.findViewById(R.id.add_img2);
            add_img3=itemView.findViewById(R.id.add_img3);
            add_img4=itemView.findViewById(R.id.add_img4);
            add_lin=itemView.findViewById(R.id.add_lin);
        }
    }

    public interface OnUpClick{
        void setOnUpClick(View view,int position);
    }

    public interface OnDownClick{
        void setOnDownClick(View view,int position);
    }

    public interface OnShareClick{
        void setOnShareClick(View view,int position);
    }

    public  void setOnUpClickListener(OnUpClick click){
        this.mOnUpClick=click;
    }

    public void setOnDownClickListener(OnDownClick clickListener){
        this.mOnDownClick=clickListener;
    }

    public void setOnShareClickListener(OnShareClick clickListener){
        this.mOnShareClick=clickListener;
    }
}
