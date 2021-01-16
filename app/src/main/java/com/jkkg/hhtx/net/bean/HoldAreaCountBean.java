package com.jkkg.hhtx.net.bean;
import java.math.BigDecimal;
import java.util.Date;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HoldAreaCountBean extends JsonBean {

    private Integer count_id;

    private Integer hold_area_id;

    /**
     * 算力消耗
     */
    private BigDecimal power_consume;

    /**
     * 投入币种消耗
     */
    private BigDecimal invest_consume;

    /**
     * 释放总量
     */
    private BigDecimal release_total;
    /**
     * 最低有效算力
     */
    private BigDecimal min_power;

    /**
     * 创建时间
     */
    private Date count_create_time;

    private BigDecimal optimal_keep;
    private BigDecimal circulation;
}