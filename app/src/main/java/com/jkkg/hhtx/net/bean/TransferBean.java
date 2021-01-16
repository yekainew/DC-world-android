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
public class TransferBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6905315682946513045L;

	private Integer transfer_id;
	private Integer transfer_conf_id;
	private Integer transfer_user_id;
	private Integer transfer_to_id;
	/**
	 * 转账数量
	 */
	private BigDecimal transfer_num;
	/**
	 * 转账手续费
	 */
	private BigDecimal transfer_fee;
	/**
	 * 实际到数量
	 */
	private BigDecimal transfer_actually;

	private Date transfer_conf_create_time;

}
