package com.jkkg.hhtx.net.bean;

import com.mugui.base.base.Component;
import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;


/**
*表名：wallet
 * 钱包表
 * HayDen
*/
@Component
@Getter
@Setter
@Accessors(chain = true)
public class Wallet extends JsonBean {

	//币币钱包
	public static final Integer bbWallet = 1;

	//法币钱包
	public static final Integer otcWallet = 2;

	//锁仓钱包
	public static final Integer lockWallet = 3;

	/**
	*钱包id
	*/
	private Integer wallet_id;

	/**
	*钱包类型 1:币币钱包  2:法币钱包  3:锁仓钱包
	*/
	private Integer wallet_type;

	/**
	*币种类型id
	*/
	private Integer currency_id;

	/**
	*用户ID
	*/
	private Integer user_id;

	/**
	*冻结金额
	*/
	private BigDecimal frozen;

	/**
	*可用金额
	*/
	private BigDecimal usable;

	/**
	*版本号,每次操作钱包都需要版本号
	*/
	private String version;

	/**
	 * 创建时间
	 */
	private Date create_time;

	/**
	 * 更新时间
	 */
	private Date update_time;

	/**
	 * 币种简码
	 */
	private String currencyName;

	//-----------------------下面是钱包账单相关-------------------------
	/**
	 * 状态  需要和钱包日志表里面的log_mode对应
	 */
	private Integer walletLogType;

	/**
	 * 说明
	 */
	private String explanation;

}
