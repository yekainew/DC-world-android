package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@Getter
@Setter
@Accessors(chain = true)
public class OptimalKeepBean extends JsonBean {

	private Integer optimal_keep_id;

	/**
	 * 持币区
	 */
	private Integer hold_area_id;
	/**
	 * 最佳持币量
	 */
	private BigDecimal optimal_keep;

	/**
	 * 时间
	 */
	private Date date;

}
