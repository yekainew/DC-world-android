package com.jkkg.hhtx.block;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ExchangeBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5693771932060845286L;

	private Integer id;
	/**
	 * 花费的币种
	 */
	private String spend_name;
	/**
	 * 花费的相关合约
	 */
	private String spend_contract;

	/**
	 * 兑换花费数量
	 */
	private BigDecimal spend_sum;

	/**
	 * 兑换转出hash
	 */
	private String spend_hash;
	/**
	 * 向那个地址兑换
	 */
	private String to_address;


	/**
	 * 兑换得到币种
	 */
	private BigDecimal get_name;

	/**
	 * 花费的相关合约
	 */
	private String get_contract;


	/**
	 * 兑换得到数量
	 */
	private BigDecimal get_sum;

	/**
	 * 兑换转入hash
	 */
	private BigDecimal get_hash;

	/**
	 * 兑换手续费
	 */
	private BigDecimal fee_sum;
	/**
	 * 兑换比例
	 */
	private BigDecimal scale;
	

	private Date create_time;
	
}
