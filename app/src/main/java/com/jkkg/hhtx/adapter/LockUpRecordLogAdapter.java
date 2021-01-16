package com.jkkg.hhtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.net.bean.LockCoinToEarnInterestLogBean;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

public class LockUpRecordLogAdapter  extends RecyclerView.Adapter {
    private Context context;
    List<LockCoinToEarnInterestLogBean> lockCoinToEarnInterestLogBeans;

    public LockUpRecordLogAdapter(Context context, List<LockCoinToEarnInterestLogBean> lockCoinToEarnInterestLogBeans) {
        this.context = context;
        this.lockCoinToEarnInterestLogBeans = lockCoinToEarnInterestLogBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LockUpRecordLogHodler(LayoutInflater.from(context).inflate(R.layout.item_lockup_record,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        LockUpRecordLogHodler hodler= (LockUpRecordLogHodler) holder1;
        BigDecimal income = lockCoinToEarnInterestLogBeans.get(position).getIncome();
        BigDecimal bc_amount = lockCoinToEarnInterestLogBeans.get(position).getBc_amount();
        if(bc_amount==null){
            bc_amount=BigDecimal.ZERO;
        }
        BigDecimal multiply1 = income.multiply(bc_amount);

        BigDecimal multiply = income.multiply(new BigDecimal("100"));
        hodler.record_yeild.setText(multiply.stripTrailingZeros().toPlainString()+"%");

        hodler.yuqi_yeild.setText(multiply1.stripTrailingZeros().toPlainString());

        hodler.lockup_yi.setText(bc_amount.stripTrailingZeros().toPlainString());
        hodler.day.setText(lockCoinToEarnInterestLogBeans.get(position).getHeaven()+"天");
        if(lockCoinToEarnInterestLogBeans.get(position).getCreate_time()!=null)
        hodler.start_day.setText(DateFormatUtils.format(lockCoinToEarnInterestLogBeans.get(position).getPurchase_time(), Constants.TIME));

        if(lockCoinToEarnInterestLogBeans.get(position).getEnd_time()!=null)
        hodler.end_day.setText(DateFormatUtils.format(lockCoinToEarnInterestLogBeans.get(position).getEnd_time(), Constants.TIME));
    }

    @Override
    public int getItemCount() {
        return lockCoinToEarnInterestLogBeans.size();
    }

    class LockUpRecordLogHodler extends RecyclerView.ViewHolder {

        private final TextView record_yeild;
        private final TextView yuqi_yeild;
        private final TextView lockup_yi;
        private final TextView day;
        private final TextView start_day;
        private final TextView end_day;

        public LockUpRecordLogHodler(@NonNull View itemView) {
            super(itemView);
            record_yeild = itemView.findViewById(R.id.record_yeild);
            yuqi_yeild = itemView.findViewById(R.id.yuqi_yeild);
            lockup_yi = itemView.findViewById(R.id.lockup_yi);
            day = itemView.findViewById(R.id.day);
            start_day = itemView.findViewById(R.id.start_day);
            end_day = itemView.findViewById(R.id.end_day);

        }
    }
}
