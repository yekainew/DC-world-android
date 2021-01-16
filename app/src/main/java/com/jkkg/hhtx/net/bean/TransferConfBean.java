package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TransferConfBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer transfer_conf_id;
	/**
	 * 币种名称
	 */
	private String bc_name;
	/**
	 * 手续费比例
	 */
	private BigDecimal transfer_conf_fee_scale;
	/**
	 * 最低转账
	 */
	private BigDecimal transfer_conf_min_num;
	/**
	 * 最高转账
	 */
	private BigDecimal transfer_conf_max_num;
	
	/**
	 * 有效
	 */
	public static final int transfer_conf_status_0=0;
	/**
	 * 失效
	 */
	public static final int transfer_conf_status_1=1;
	
	private Integer transfer_conf_status;

	private Date transfer_conf_create_time;

}
