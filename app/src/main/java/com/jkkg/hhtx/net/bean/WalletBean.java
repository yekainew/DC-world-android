package com.jkkg.hhtx.net.bean;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.mugui.base.bean.JsonBean;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WalletBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6072130334156449042L;
	/**
	 * 币种工厂名称
	 */
	private String name;
	/**
	 * 操作函数
	 */
	private String method;

	/**
	 * 来源地址
	 */
	private String from;
	/**
	 * 去向地址
	 */
	private String to;
	/**
	 * 去向地址额外描述
	 */
	private String to_extra;
	/**
	 * 哈希
	 */
	private String hash;
	/**
	 * 真实的交易哈希
	 */
	private String realHash;
	/**
	 * 数量
	 */
	private BigDecimal amount;
	/**
	 * 手续费
	 */
	private BigDecimal fee;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 增量ID
	 */
	private String index;
	/**
	 * 关于index之后多少
	 */
	private String index_size;

	private Integer confirmed;

	/**
	 * 交易状态
	 */
	private Integer isConfirmed;

	/**
	 * JSON数据
	 * 
	 * @return
	 */
	private JSONArray arr;

}
