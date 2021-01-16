package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.NewListBean;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

public class NewListManageAdapter extends RecyclerView.Adapter {
    List<NewListBean> o;

    private Context context;
    private OnClick mOnClick;

    public NewListManageAdapter(List<NewListBean> o, Context context) {
        this.o = o;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewListManageHodler(LayoutInflater.from(context).inflate(R.layout.item_news_management, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        NewListManageHodler hodler = (NewListManageHodler) holder1;
        int approval_status = o.get(position).getApproval_status();
        if (approval_status==1) {
            hodler.type.setText(context.getString(R.string.string_swit_shenhe));
            hodler.btn.setText(context.getString(R.string.string_cexiao));
            hodler.btn.setBackgroundResource(R.drawable.bg_sift_corner_blue);
            hodler.btn.setTextColor(context.getResources().getColor(R.color.font_color_blue));

        }else if(approval_status==2){
            hodler.type.setText(context.getString(R.string.string_review));
            hodler.btn.setText(context.getString(R.string.string_delate));
            hodler.btn.setBackgroundResource(R.drawable.bg_sift_corner_red);
            hodler.btn.setTextColor(context.getResources().getColor(R.color.font_color_red));
        }else if (approval_status==3){
            hodler.type.setText(context.getString(R.string.string_bietmshenhel));
            hodler.btn.setText(context.getString(R.string.string_delate));
            hodler.btn.setBackgroundResource(R.drawable.bg_sift_corner_red);
            hodler.btn.setTextColor(context.getResources().getColor(R.color.font_color_red));
        }

        hodler.new_title.setText(o.get(position).getTitle());
        hodler.new_text.setText(o.get(position).getContent());

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
        }

        hodler.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClick!=null) {
                    mOnClick.setOnClick(v,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return o.size();
    }

    public void setData(List<NewListBean> o) {
        if (o != null) {
            this.o = o;
            this.notifyDataSetChanged();
        }
    }

    public void upData(List<NewListBean> o) {
        if (o != null) {
            this.o.addAll(o);
            this.notifyDataSetChanged();
        }
    }

    class NewListManageHodler extends RecyclerView.ViewHolder {

        private final ImageView head_img;
        private final TextView name;
        private final TextView type;
        private final Button btn;
        private final TextView new_title;
        private final TextView new_text;
        private final ImageView add_img1;
        private final ImageView add_img2;
        private final ImageView add_img3;
        private final ImageView add_img4;

        public NewListManageHodler(@NonNull View itemView) {
            super(itemView);

            head_img = itemView.findViewById(R.id.head_img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            btn = itemView.findViewById(R.id.btn);
            new_title = itemView.findViewById(R.id.new_title);
            new_text = itemView.findViewById(R.id.new_text);
            add_img1 = itemView.findViewById(R.id.add_img1);
            add_img2 = itemView.findViewById(R.id.add_img2);
            add_img3 = itemView.findViewById(R.id.add_img3);
            add_img4 = itemView.findViewById(R.id.add_img4);
        }
    }

    public  interface OnClick {
        void setOnClick(View view,int position);
    }
    public void setOnClickListener(OnClick clickListener){
        this.mOnClick=clickListener;
    }
}
