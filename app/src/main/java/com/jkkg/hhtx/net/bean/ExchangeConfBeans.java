package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交换配置bean
 *
 * @author 33
 * @date 2020/11/17
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExchangeConfBeans extends JsonBean{

    /**
     * 有效
     */
    public static final int exchange_conf_status_0 = 0;
    /**
     * 失效
     */
    public static final int exchange_conf_status_1 = 1;
    /**
     * 未开放
     */
    public static final int UNDEVELOPED = 2;
    /**
     * 已完成
     */
    public static final int COMPLETED = 3;

    /**
     * 配置id
     */

    protected Integer exchange_conf_id;
    /**
     * 兑换花费币种
     */
    protected String exchange_conf_spend;
    /**
     * 兑换得到币种
     */
    protected String exchange_conf_get;
    /**
     * 兑换比例：及 花费一个得到多少个
     */
    protected BigDecimal exchange_conf_scale;
    /**
     * 状态(0:有效，1:无效，2:未开放，3:已完成)
     */
    protected Integer exchange_conf_status;
    /**
     * 兑换手续费 百分比
     */
    protected BigDecimal exchange_conf_fee;
    /**
     * 最小兑换
     */
    protected BigDecimal exchange_conf_min;
    /**
     * 最大兑换
     */
    protected BigDecimal exchange_conf_max;
    /**
     * 兑换地址
     */
    protected String exchange_address;
    /**
     * 创建时间
     */
    protected Date exchange_conf_create_time;
    /**
     * 期数名称
     */
    protected String name;
    /**
     * 总数量
     */
    protected BigDecimal total_amount;
    /**
     * 剩余数量
     */
    protected BigDecimal the_remaining_amount;
    /**
     * 开始时间
     */
    protected Date start_time;
    /**
     * 结束时间
     */
    protected Date end_time;
    /**
     * 可兑换次数
     */
    protected Integer number_of_exchanges;
    /**
     * 兑换奖励配置(百分比)0.1则代表10%
     */
    protected BigDecimal redeem_rewards;
    /**
     * 每层入金u
     */
    protected BigDecimal deposit_amount_per_layer;
    /**
     * 下一层价格百分比涨幅 0.05  则代表是5%
     */
    protected BigDecimal next_level_of_price_increase;
    /**
     * 起始价格
     */
    protected BigDecimal starting_price;
    /**
     * 当前层级
     */
    protected Integer current_level;
    /**
     * 当前入金
     */
    protected BigDecimal current_deposit;
    /**
     * 当前价格
     */
    protected BigDecimal current_price;

    /**
     * 充值金额
     */
    protected BigDecimal bc_amount;

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
     * 已经购买次数
     */
    protected Integer numberOfPurchases;
    /**
     * 该层可购数量
     */
    protected BigDecimal thisLayerCanPurchaseQuantity;
    /**
     * 每层出币
     */
    protected BigDecimal output_curreny;

    /**
     * 剩余多少u可以充值
     *
     * @return {@link BigDecimal}
     */
    public BigDecimal remainingU() {

        return deposit_amount_per_layer.subtract(current_deposit);
    }

}
