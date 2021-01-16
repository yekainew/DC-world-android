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
public class ExchangeRewardBean extends JsonBean {

    private Integer exchange_reward_id;



    /**

     * 兑换id

     */


    private Integer exchange_id;

    /**

     * 奖励来源用户

     */


    private Integer user_id;

    /**

     * 奖励目标用户

     */


    private Integer to_user_id;

    /**

     * 奖励数量

     */


    private BigDecimal reward_num;



    /**

     * 奖励比例

     */


    private BigDecimal reward_scale;

    /**

     * 奖励hash

     */

    @SQLField(DATA_TYPE = "varchar(128)")

    private String reward_hash;



    /**

     * 奖励记录创建时间

     */

    @SQLField

    private Date exchange_reward_create_time;

    private String to_address;

    private String from_address;
}
