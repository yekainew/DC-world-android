package com.jkkg.hhtx.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.utils.StringUtils;

public class MnemonicAdapter extends BaseQuickAdapter<String, MnemonicAdapter.ViewHolder> {

    public MnemonicAdapter() {
        super(R.layout.layout_mnemonic_item);
        //num=12
        for(int i=0;i<12;i++){
            getData().add("");
        }
    }
    @Override
    protected void convert(ViewHolder helper, String item) {
        if(StringUtils.isNotEmpty(item)){
            helper.layout_mnemonic_tv.setText(item);
        }
    }

    public class ViewHolder extends BaseViewHolder {
        private TextView layout_mnemonic_tv;

        public ViewHolder(View view) {
            super(view);
            layout_mnemonic_tv=view.findViewById(R.id.layout_mnemonic_tv);
        }
    }
}