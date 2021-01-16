package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
/**
 * 币种类型
 * @author 木鬼
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class BlockChainTypeBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024795529841935824L;
	/**
	 * 币种id
	 */
	public Integer bc_type_id;
	/**
	 * 币种名称
	 */
	public String bc_type_name;
	/**
	 * 币种标识
	 */
	public String bc_type_sign;

	public Date bc_type_create_time;

	/**
	 * 币种状态
	 */
	public Integer bc_type_status;

}
