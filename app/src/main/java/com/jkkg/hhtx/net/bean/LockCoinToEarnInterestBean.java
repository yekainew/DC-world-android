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
 * 锁币生息
 *
 * @author wangTao
 * @date 2020-11-19 19:14:23
 */
@Getter
@Setter
@Accessors(chain = true)
public class LockCoinToEarnInterestBean extends JsonBean {

    /**
     * 锁币生息
     */

    protected Integer lock_coin_to_earn_interest_id;
    /**
     * 天数
     */
    protected Integer heaven;
    /**
     * 收益(0.5就是百分之50)
     */
    protected BigDecimal income;
    /**
     *
     */
    protected String currency_name;
    /**
     * 币种名称
     */
    protected Integer currency_id;
    /**
     * 最小
     */
    protected BigDecimal min;
    /**
     * 最大
     */
    protected BigDecimal max;


    /**
     * 令牌
     */
    protected String token;

    /**
     * 地址
     */
    protected String address;


    /**
     * 充值金额
     */
    protected BigDecimal bc_amount;
    /**
     * 服务总额度
     */
    protected BigDecimal total_investmentSum;

    /**
     * 来源地址
     */
    protected String from_address;
    /**
     * 交易hash
     */
    protected String real_hash;
    /**
     * 用户id
     */
    protected Integer user_id;


    protected Integer bc_tran_id;

    /**
     * 结束时间
     */
    protected Date end_time;
    /**
     * 购买时间
     */
    protected Date purchase_time;


    /**
     * 用户的电话
     */
    protected String user_phone;


    protected Integer log_id;

}
