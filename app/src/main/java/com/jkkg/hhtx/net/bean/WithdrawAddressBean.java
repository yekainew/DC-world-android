package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户提币地址存储表
 * 
 * @author 木鬼
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class WithdrawAddressBean extends JsonBean {

	/**
	 * 		
	 */
	private static final long serialVersionUID = 4989861730935245207L;
	private Integer withdraw_address_id;
	private Integer user_id;
	/**
	 * 地址
	 */
	private String withdraw_address;
	/**
	 * 币种名称
	 */
	private String currency_name;
	/**
	 * 链上币种名称
	 */
	private String bc_type_name;
	/**
	 * 地址额外描述
	 */
	private String withdraw_address_extra;
	/**
	 * 0 可用 1不可用
	 */
	private Integer withdraw_status;

	private Date withdraw_create_time;

	private Date withdraw_update_time;

	private Date withdraw_delete_time;

}
