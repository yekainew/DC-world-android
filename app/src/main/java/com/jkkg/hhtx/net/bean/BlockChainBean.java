package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户虚拟币表
 * 
 * @author 木鬼
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class BlockChainBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2681538907259823903L;

	/**
	 * 虚拟币表id
	 */
	private Integer block_chain_id;

	private Integer user_id;

	/**
	 * 链上币种名称
	 */
	private String bc_type_name;
	/**
	 * 本地币种名称
	 */
	private String currency_name;
	/**
	 * 币种地址
	 */
	private String bc_address;
	/**
	 * 币种地址额外信息
	 */
	private String bc_address_extra;

	private Date bc_create_time;

	/**
	 * 币种状态
	 */
	private Integer bc_status;
}
