package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FinancialIncomeBean extends JsonBean {

    /**
     * 理财收益id
     */

    protected Integer financial_income_id;
    /**
     * 理财表id
     */
    protected Integer financial_management_id;
    /**
     * 理财购买记录id
     */
    protected Integer financial_purchase_log_id;
    /**
     *
     */
    protected Integer user_id;
    /**
     * 当日收益
     */
    protected BigDecimal day_income;
    /**
     * 总收益
     */
    protected BigDecimal total_revenue;
    /**
     *
     */
    protected Date create_time;
    /**
     * 当日收益利率
     */
    protected BigDecimal yield_rate;
    /**
     * 收益类型,1是投资收益,2是分享收益  ,3是小区算力
     */
    protected Integer type;
}
