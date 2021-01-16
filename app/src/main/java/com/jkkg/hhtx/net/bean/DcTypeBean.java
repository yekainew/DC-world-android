package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


public class DcTypeBean {

    private String update_time;
    private String currency_name;
    private String create_time;
    private String currency_type;
    private String currency_explain;
    private String currency_id;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getCurrency_explain() {
        return currency_explain;
    }

    public void setCurrency_explain(String currency_explain) {
        this.currency_explain = currency_explain;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }
}
