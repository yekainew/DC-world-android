package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@Getter
@Setter
@Accessors(chain = true)
public class PoolCount extends JsonBean {
	/**
	 * 全网挖矿总算力
	 */
	private BigDecimal net_all_power=BigDecimal.ZERO;
	/**
	 * 全网推广总算力
	 */
	private BigDecimal net_push_power=BigDecimal.ZERO;

	/**
	 * 全网产出
	 */
	private BigDecimal now_output_dc=BigDecimal.ZERO;

}
