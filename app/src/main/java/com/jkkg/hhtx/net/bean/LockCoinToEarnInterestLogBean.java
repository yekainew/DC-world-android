package com.jkkg.hhtx.net.bean;


import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 锁币生息购买日志
 *
 * @author wangTao
 * @date 2020-11-19 19:14:23
 */
@Getter
@Setter
@Accessors(chain = true)
public class LockCoinToEarnInterestLogBean extends JsonBean implements Serializable {

    /**
     *
     */

    protected Integer lock_coin_to_earn_interest_log_id;
    /**
     *
     */
    protected Integer user_id;
    /**
     * 锁币金额
     */
    protected BigDecimal amount;
    protected BigDecimal bc_amount;
    /**
     *
     */
    protected Integer lock_coin_to_earn_interest_id;
    /**
     *
     */
    protected Date create_time;
    /**
     *
     */
    protected Date update_time;
    protected Date purchase_time;
    /**
     * 1是购买,2是已经发放奖励,3是创建
     */
    protected Integer status;
    /**
     *
     */
    protected String currency_name;
    /**
     *
     */
    protected Integer currency_id;
    /**
     * 结束时间
     */
    protected Date end_time;

    /**
     * 哈希
     */
    protected String hash;
    /**
     * 发放收益
     */
    protected BigDecimal income;
    /**
     * 天数
     */
    protected Integer heaven;

}
