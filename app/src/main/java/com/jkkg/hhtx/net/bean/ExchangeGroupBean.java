package com.jkkg.hhtx.net.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 兑换单轮配置
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExchangeGroupBean extends JsonBean {

	private Integer exchange_group_id;

	private Integer exchange_conf_id;


	/**
	 * 总量
	 */
	private BigDecimal tatol_num;

	/**
	 * 已兑换
	 */
	private BigDecimal redeemed_num;

	/**
	 * 剩余数量
	 */
	private BigDecimal lave_num;

	/**
	 * 未开始
	 */
	public static final int group_status_0 = 0;

	/**
	 * 进行中
	 */
	public static final int group_status_1 = 1;

	/**
	 * 已结束
	 */
	public static final int group_status_2 = 2;

	private Integer group_status;

	private Date start_time;

	private Date end_time;

	private boolean isCheck;
}
