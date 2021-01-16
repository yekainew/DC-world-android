package com.jkkg.hhtx.net.bean;

import com.mugui.base.base.Component;
import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;


/**
*表名：currency
* 币种类型表
* @author HayDen
*/
@Component
@Getter
@Setter
@Accessors(chain = true)
public class Currency extends JsonBean {

	/**
	 *币种id
	 */
	private Integer currency_id;

	/**
	 *币种名称
	 */
	private String currency_name;

	/**
	*0:上架中  1:已下架
	*/
	private Integer currency_type;

	/**
	*修改时间
	*/
	private Date update_time;

	/**
	 *说明
	 */
	private String currency_explain;

	/**
	 *创建时间
	 */
	private Date create_time;

}
