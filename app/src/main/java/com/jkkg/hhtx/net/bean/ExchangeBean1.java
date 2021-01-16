package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@SQLDB(KEY = "exchange_id",TABLE = "exchange")
public class ExchangeBean1 extends JsonBean {


	/**
	 *
	 */

	private Integer exchange_id;

	private Integer exchange_conf_id;

	private Integer user_id;

	/**
	 * 兑换花费数量
	 */
	private BigDecimal exchange_spend_sum;
	/**
	 * 兑换得到数量
	 */
	private BigDecimal exchange_get_sum;

	/**
	 * 兑换手续费
	 */
	private BigDecimal exchange_fee_sum;

	private Date exchange_create_time;


	private String from_hash;
	private String to_hash;


	/**
	 * 当前层级
	 */
	private Integer current_level;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态,1是进行中,2是已经完成,3是部分完成,4是失败'
	 */
	private Integer state;

	/**
	 * 兑换返还数量
	 */
	private BigDecimal exchange_return_num;


	/**
	 * 返回的USDThash
	 */
	private String usdt_to_hash;

}
