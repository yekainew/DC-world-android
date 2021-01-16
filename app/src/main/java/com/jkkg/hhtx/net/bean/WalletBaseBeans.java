package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 表名：wallet 钱包表 HayDen
 */
@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "wallet", KEY = "wallet_id")
public class WalletBaseBeans extends JsonBean {

	// 币币钱包
	public static final Integer bbWallet = 1;

	// 邀请次数钱包
	public static final Integer otcWallet = 2;

	// 锁仓钱包
	public static final Integer lockWallet = 3;
	/**
	 * 理财钱包
	 */
	public static final Integer FINANCIAL_WALLET = 5;

	public static final Integer CheckinWallet = 4;

	/**
	 * 孵化器的钱包
	 */
	public static final Integer INCUBATOR_WALLET = 6;

	/**
	 * 社区奖励钱包
	 */
	public static final Integer COMMUNITY_REWARD_WALLET = 7;
	/**
	 * 认购释放钱包
	 */
	public static final Integer SUBSCRIBE_TO_RELEASE_WALLET = 8;
	/**
	 * 锁币生息钱包
	 */
	public static final Integer COIN_LOCKED_INTEREST_BEARING_WALLET = 8;

	public static enum TYPE {
		BB_WALLET(bbWallet), CHECKIN_WALLET(CheckinWallet), FINANCIAL_WALLET(WalletBaseBeans.FINANCIAL_WALLET),
		COIN_LOCKED_INTEREST_BEARING_WALLET(WalletBaseBeans.COIN_LOCKED_INTEREST_BEARING_WALLET),

		SUBSCRIBE_TO_RELEASE_WALLET(WalletBaseBeans.SUBSCRIBE_TO_RELEASE_WALLET),


		COMMUNITY_REWARD_WALLET(WalletBaseBeans.COMMUNITY_REWARD_WALLET),

		OTC_WALLET(otcWallet)
		;
		@Getter
		int type;

		TYPE(int type) {
			this.type = type;
		}

	}

	/**
	 * 钱包id
	 */
	@SQLField(PRIMARY_KEY = true)
	private Integer wallet_id;

	/**
	 * 钱包类型 1:币币钱包 2:法币钱包 3:锁仓钱包 4:签到钱包 5:理财钱包
	 */
	@SQLField
	private Integer wallet_type;

	/**
	 * 币种类型id
	 */
	@SQLField
	private Integer currency_id;

	/**
	 * 用户ID
	 */
	@SQLField
	private Integer user_id;

	/**
	 * 冻结金额
	 */
	@SQLField
	private BigDecimal frozen;

	/**
	 * 可用金额
	 */
	@SQLField
	private BigDecimal usable;

	/**
	 * 版本号,每次操作钱包都需要版本号
	 */
	@SQLField
	private String version;

	/**
	 * 创建时间
	 */
	@SQLField
	private Date create_time;

	/**
	 * 更新时间
	 */
	@SQLField
	private Date update_time;

	/**
	 * 币种简码
	 */
	@SQLField
	private String currencyName;

	// -----------------------下面是钱包账单相关-------------------------
	/**
	 * 状态 需要和钱包日志表里面的log_mode对应
	 */
	private Integer walletLogType;

	/**
	 * 说明
	 */
	private String explanation;

}
