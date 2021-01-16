package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 表名：wallet_log 钱包日志 Hayden
 */
@Getter
@Setter
@Accessors(chain = true)
public class WalletLog extends JsonBean {

	// 加钱
	public static final String add = "+";

	// 减钱
	public static final String cut = "-";

	// 恢复 冻结转可用
	public static final String locking = "+=";

	// 锁定金额 可用转冻结
	public static final String resume = "=+";

	public static Map<Integer, String> map = new HashMap<>();

	static {
		map.put(1, "注册");
		map.put(2, "邀请");
		map.put(3, "交易释放");
		map.put(4, "充值");
		map.put(5, "提币");
		map.put(6, "法币卖出");
		map.put(7, "法币买入");
		map.put(8, "法币下单冻结");
		map.put(9, "承兑发布冻结");
		map.put(10, "承兑撤单解冻");
		map.put(11, "下单取消解冻");
		map.put(12, "用户撤单解冻返还");
		map.put(13, "OTC划转到币币");
		map.put(14, "币币划转到OTC");
		map.put(40, "提币");
		map.put(41, "内部提币");
		map.put(50, "取消委托单");
		map.put(51, "单笔撮合");
	}

	/**
	 * 钱包操作日志
	 */
	private Integer log_id;

	/**
	 * 钱包ID
	 */
	private Integer wallet_id;

	/**
	 * 用户ID
	 */
	private Integer user_id;

	/**
	 * 币种简码
	 */
	private String currency_name;

	/**
	 * 操作说明
	 */
	private String log_explain;

	/**
	 * 操作金额
	 */
	private BigDecimal log_amount;

	/**
	 * 操作类型(+是增加钱,-是减少钱,+=可用转冻结,=+冻结转可用)
	 */
	private String log_type;

	/**
	 * 状态(1:冻结 2:可用)
	 */
	private Integer log_state;

	/**
	 * 1:注册 2: 邀请 3:交易释放 4:充值 5:提币 6:法币卖出 7:法币买入 8:法币下单冻结 9:承兑发布冻结 10:承兑撤单解冻
	 * 11:下单取消解冻
	 */
	private Integer log_mode;

	/**
	 * 操作前冻结金额
	 */
	private BigDecimal log_start_frozen;

	/**
	 * 操作后冻结金额
	 */
	private BigDecimal log_end_frozen;

	/**
	 * 操作前可用金额
	 */
	private BigDecimal log_start_usable;

	/**
	 * 操作后可用金额
	 */
	private BigDecimal log_end_usable;

	/**
	 * 手续费
	 */
	private BigDecimal log_handling_fee;

	/**
	 * 创建时间
	 */
	private Date log_create_time;

	/**
	 * 备注
	 */
	private String log_remark;

	/**
	 * 关联信息,可以存json
	 */
	private String log_relation;

}
