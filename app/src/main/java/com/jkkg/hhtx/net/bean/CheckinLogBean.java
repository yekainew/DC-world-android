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
public class CheckinLogBean extends JsonBean {

    /**
     * 签到日志
     */

    protected Integer checkin_log_id;
    /**
     *
     */
    protected Integer user_id;
    /**
     *
     */
    protected String name;
    /**
     * 1是签到发放收益,2是补签
     */
    protected Integer log_type;
    /**
     * 金额, 正数为加,负数为减
     */
    protected BigDecimal amount;
    /**
     * 币种id
     */
    protected Integer currency_id;
    /**
     * 币种名称
     */
    protected String currency_name;
    /**
     *
     */
    protected Date create_time;
}
