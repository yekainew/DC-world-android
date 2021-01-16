package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;


public class UserHoldIncomeBean extends JsonBean {

    /**
     * income_reason : 持币收益
     * income_id : 12
     * user_hold_id : 1
     * user_id : 27779
     * income_type : 1
     * income_create_time : 2020-10-23 18:37:28
     * income_num : 0.60000000
     */

    private String income_reason;
    private String income_id;
    private String user_hold_id;
    private String user_id;
    private String income_type;
    private Date income_create_time;
    private String income_num;

    public String getIncome_reason() {
        return income_reason;
    }

    public void setIncome_reason(String income_reason) {
        this.income_reason = income_reason;
    }

    public String getIncome_id() {
        return income_id;
    }

    public void setIncome_id(String income_id) {
        this.income_id = income_id;
    }

    public String getUser_hold_id() {
        return user_hold_id;
    }

    public void setUser_hold_id(String user_hold_id) {
        this.user_hold_id = user_hold_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIncome_type() {
        return income_type;
    }

    public void setIncome_type(String income_type) {
        this.income_type = income_type;
    }

    public Date getIncome_create_time() {
        return income_create_time;
    }

    public void setIncome_create_time(Date income_create_time) {
        this.income_create_time = income_create_time;
    }

    public String getIncome_num() {
        return income_num;
    }

    public void setIncome_num(String income_num) {
        this.income_num = income_num;
    }
}
