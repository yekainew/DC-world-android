package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author 木鬼 提币记录表
 */
@Getter
@Setter
@Accessors(chain = true)
public class WithdrawBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2928738695851795822L;
	/**
	 * 对外
	 */
	public static final Integer WITHDRAW_TYPE_1 = 1;
	/**
	 * 对内
	 */
	public static final Integer WITHDRAW_TYPE_2 = 2;
	/**
	 * 已创建
	 */
	public static final Integer WITHDRAW_STATUS_0 = 0;
	/**
	 * 待审核
	 */
	public static final Integer WITHDRAW_STATUS_1 = 1;
	/**
	 * 已审核
	 */
	public static final Integer WITHDRAW_STATUS_2 = 2;
	/**
	 * 已取消
	 */
	public static final Integer WITHDRAW_STATUS_3 = 3;
	/**
	 * 转出成功
	 */
	public static final Integer WITHDRAW_STATUS_4 = 4;
	/**
	 * 确认中
	 */
	public static final Integer WITHDRAW_STATUS_5 = 5;
	/**
	 * 已完成
	 */
	public static final Integer WITHDRAW_STATUS_6 = 6;

	private Integer withdraw_id;

	private Integer user_id;

	/**
	 * 提币类型 1、对外提币 2、内部提币
	 */
	private Integer withdraw_type;
	/**
	 * 去向id,当为内部提币的时候为去向用户id,否则为null
	 */
	private Integer to_id;
	/**
	 * 去向地址
	 */
	private String to_address;

	private String to_address_extra;
	/**
	 * 提币状态 0：已创建 1：待审核 2：已审核 3：已取消 4：转出成功 5：确认中 6：已完成
	 */
	private Integer withdraw_status;
	/**
	 * 提币数量
	 */
	private BigDecimal withdraw_num;

	/**
	 * 提币手续费
	 */
	private BigDecimal withdraw_fee;
	/**
	 * 提币到账数量
	 */
	private BigDecimal withdraw_arrive_num;
	/**
	 * 提币币种名称
	 */
	private String dc_type_name;

	/**
	 * 提币链上hash
	 */
	private String withdraw_hash;

	private Date withdraw_create_time;

	private Date withdraw_sucess_time;

}
