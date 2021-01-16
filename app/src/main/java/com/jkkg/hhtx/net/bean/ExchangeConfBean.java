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
public class ExchangeConfBean extends JsonBean {

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
	 * 有效
	 */
	public static final int exchange_conf_status_0 = 0;
	/**
	 * 失效
	 */
	public static final int exchange_conf_status_1 = 1;

	/**
	 * 0，有效 1失效
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

	protected Date exchange_conf_create_time;


	/**
	 * 名字 第几期
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
	 * 结束时间
	 */
	protected Date end_time;
	/**
	 * 兑换次数
	 */
	protected Integer number_of_exchanges;


	/**
	 * 充值金额
	 */
	protected BigDecimal bc_amount;

	/**
	 * 来源地址
	 */
	protected String from_address;
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



}
