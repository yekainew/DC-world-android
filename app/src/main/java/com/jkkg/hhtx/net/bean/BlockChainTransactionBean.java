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
public class BlockChainTransactionBean extends JsonBean {

	/**
	 *
	 */
	private static final long serialVersionUID = -4784172024576588336L;
	/**
	 * 交易id
	 */
	public Integer bc_tran_id;
	/**
	 * 币种名称
	 */
	public String bc_type_name;
	/**
	 * 币种标识
	 */
	public String bc_type_sign;

	public String from_address;

	public String to_address;

	public String real_from_address;

	public String real_hash;

	public BigDecimal bc_tran_fee;
	public BigDecimal real_fee;
	public BigDecimal bc_amount;

	/**
	 * 交易状态
	 */
	public Integer bc_tran_status;

	/**
	 * 交易确认数
	 */
	public Integer confirmed;
	public String bc_tran_detail;
	public Date bc_tran_create_time;
	public Date bc_tran_sucess_time;

}
