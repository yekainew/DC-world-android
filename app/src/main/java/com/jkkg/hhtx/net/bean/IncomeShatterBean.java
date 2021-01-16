package com.jkkg.hhtx.net.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@Getter
@Setter
@Accessors(chain = true)
public class IncomeShatterBean extends JsonBean {
	/**
	 * 收益碎片id
	 */
	private Integer income_shatter_id;

	private Integer user_id;
	/**
	 * 持币区
	 */
	private Integer hold_area_id;

	/**
	 * 持有
	 */
	public static final int SHATTER_TYPE_0 = 0;
	/**
	 * 直推
	 */
	public static final int SHATTER_TYPE_1 = 1;

	/**
	 * 碎片类型
	 */
	private Integer shatter_type;

	private BigDecimal shatter_num;

	/**
	 * 已产出
	 */
	public static final int SHATTER_STATUS_0 = 0;
	/**
	 * 已收获
	 */
	public static final int SHATTER_STATUS_1 = 1;
	/**
	 * 已过期
	 */
	public static final int SHATTER_STATUS_2 = 2;
	/**
	 * 碎片状态
	 */
	private Integer shatter_status;

	private Date shatter_create_time;

}
