package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.io.Serializable;


public class ExchangeBean extends JsonBean {
	/**
	 * exchange_conf_status : 0
	 * exchange_conf_spend : USDT
	 * exchange_get_sum : 0.02991
	 * from_hash : 99e2370c9a7b23ea80014bd946fc029e0274b1e72b8fddb378c1da17da9fca67
	 * exchange_conf_get : DC
	 * exchange_create_time : 2020-10-19 18:30:02
	 * exchange_conf_create_time : 2020-09-28 16:09:25
	 * exchange_conf_min : 0
	 * exchange_spend_sum : 0.01
	 * exchange_fee_sum : 0.00003
	 * exchange_id : 60
	 * exchange_conf_scale : 3
	 * user_id : 27779
	 * exchange_conf_max : 5000
	 * to_hash : b782dcc7a51a2b0b3fb836585ca54ff0877fcbf51bbbcc8eb45322cde7c80d77
	 * exchange_conf_fee : 0.003
	 * exchange_conf_id : 2
	 */
	private int exchange_conf_status;
	private String exchange_conf_spend;
	private String exchange_get_sum;
	private String from_hash;
	private String exchange_conf_get;
	private String exchange_create_time;
	private String exchange_conf_create_time;
	private String exchange_conf_min;
	private String exchange_spend_sum;
	private String exchange_fee_sum;
	private int exchange_id;
	private String exchange_conf_scale;
	private int user_id;
	private String exchange_conf_max;
	private String to_hash;
	private String exchange_conf_fee;
	private int exchange_conf_id;

	public int getExchange_conf_status() {
		return exchange_conf_status;
	}

	public void setExchange_conf_status(int exchange_conf_status) {
		this.exchange_conf_status = exchange_conf_status;
	}

	public String getExchange_conf_spend() {
		return exchange_conf_spend;
	}

	public void setExchange_conf_spend(String exchange_conf_spend) {
		this.exchange_conf_spend = exchange_conf_spend;
	}

	public String getExchange_get_sum() {
		return exchange_get_sum;
	}

	public void setExchange_get_sum(String exchange_get_sum) {
		this.exchange_get_sum = exchange_get_sum;
	}

	public String getFrom_hash() {
		return from_hash;
	}

	public void setFrom_hash(String from_hash) {
		this.from_hash = from_hash;
	}

	public String getExchange_conf_get() {
		return exchange_conf_get;
	}

	public void setExchange_conf_get(String exchange_conf_get) {
		this.exchange_conf_get = exchange_conf_get;
	}

	public String getExchange_create_time() {
		return exchange_create_time;
	}

	public void setExchange_create_time(String exchange_create_time) {
		this.exchange_create_time = exchange_create_time;
	}

	public String getExchange_conf_create_time() {
		return exchange_conf_create_time;
	}

	public void setExchange_conf_create_time(String exchange_conf_create_time) {
		this.exchange_conf_create_time = exchange_conf_create_time;
	}

	public String getExchange_conf_min() {
		return exchange_conf_min;
	}

	public void setExchange_conf_min(String exchange_conf_min) {
		this.exchange_conf_min = exchange_conf_min;
	}

	public String getExchange_spend_sum() {
		return exchange_spend_sum;
	}

	public void setExchange_spend_sum(String exchange_spend_sum) {
		this.exchange_spend_sum = exchange_spend_sum;
	}

	public String getExchange_fee_sum() {
		return exchange_fee_sum;
	}

	public void setExchange_fee_sum(String exchange_fee_sum) {
		this.exchange_fee_sum = exchange_fee_sum;
	}

	public int getExchange_id() {
		return exchange_id;
	}

	public void setExchange_id(int exchange_id) {
		this.exchange_id = exchange_id;
	}

	public String getExchange_conf_scale() {
		return exchange_conf_scale;
	}

	public void setExchange_conf_scale(String exchange_conf_scale) {
		this.exchange_conf_scale = exchange_conf_scale;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getExchange_conf_max() {
		return exchange_conf_max;
	}

	public void setExchange_conf_max(String exchange_conf_max) {
		this.exchange_conf_max = exchange_conf_max;
	}

	public String getTo_hash() {
		return to_hash;
	}

	public void setTo_hash(String to_hash) {
		this.to_hash = to_hash;
	}

	public String getExchange_conf_fee() {
		return exchange_conf_fee;
	}

	public void setExchange_conf_fee(String exchange_conf_fee) {
		this.exchange_conf_fee = exchange_conf_fee;
	}

	public int getExchange_conf_id() {
		return exchange_conf_id;
	}

	public void setExchange_conf_id(int exchange_conf_id) {
		this.exchange_conf_id = exchange_conf_id;
	}
}
