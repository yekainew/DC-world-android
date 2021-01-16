package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

public class WalletLogBeans extends JsonBean {

    /**
     * log_id : 4313
     * currency_name : DC
     * log_create_time : 1609839585000
     * wallet_id : 893
     * log_type : +
     * user_id : 27783
     * log_start_usable : 10
     * log_end_frozen : 0
     * log_amount : 10
     * log_handling_fee : 0
     * log_state : 2
     * log_mode : 70
     * log_start_frozen : 0
     * log_explain : 购买邀请次数
     * log_end_usable : 20
     */

    private int log_id;
    private String currency_name;
    private long log_create_time;
    private int wallet_id;
    private String log_type;
    private int user_id;
    private String log_start_usable;
    private String log_end_frozen;
    private String log_amount;
    private String log_handling_fee;
    private int log_state;
    private int log_mode;
    private String log_start_frozen;
    private String log_explain;
    private String log_end_usable;

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public long getLog_create_time() {
        return log_create_time;
    }

    public void setLog_create_time(long log_create_time) {
        this.log_create_time = log_create_time;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLog_start_usable() {
        return log_start_usable;
    }

    public void setLog_start_usable(String log_start_usable) {
        this.log_start_usable = log_start_usable;
    }

    public String getLog_end_frozen() {
        return log_end_frozen;
    }

    public void setLog_end_frozen(String log_end_frozen) {
        this.log_end_frozen = log_end_frozen;
    }

    public String getLog_amount() {
        return log_amount;
    }

    public void setLog_amount(String log_amount) {
        this.log_amount = log_amount;
    }

    public String getLog_handling_fee() {
        return log_handling_fee;
    }

    public void setLog_handling_fee(String log_handling_fee) {
        this.log_handling_fee = log_handling_fee;
    }

    public int getLog_state() {
        return log_state;
    }

    public void setLog_state(int log_state) {
        this.log_state = log_state;
    }

    public int getLog_mode() {
        return log_mode;
    }

    public void setLog_mode(int log_mode) {
        this.log_mode = log_mode;
    }

    public String getLog_start_frozen() {
        return log_start_frozen;
    }

    public void setLog_start_frozen(String log_start_frozen) {
        this.log_start_frozen = log_start_frozen;
    }

    public String getLog_explain() {
        return log_explain;
    }

    public void setLog_explain(String log_explain) {
        this.log_explain = log_explain;
    }

    public String getLog_end_usable() {
        return log_end_usable;
    }

    public void setLog_end_usable(String log_end_usable) {
        this.log_end_usable = log_end_usable;
    }
}
