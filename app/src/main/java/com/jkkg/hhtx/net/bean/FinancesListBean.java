package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

public class FinancesListBean extends JsonBean {

    /**
     * out_multiple : 200
     * min : 1
     * max : 100
     * payment_currency_id : 1
     * name : 低收益理财
     * get_currency_name : USDT
     * payment_currency_name : USDT
     * get_currency_id : 1
     * annualized_rate : 16
     * daily_income : 0.1
     * financial_management_id : 1
     * status : 1
     */

    private String out_multiple;
    private String min;
    private String max;
    private int payment_currency_id;
    private String name;
    private String get_currency_name;
    private String payment_currency_name;
    private int get_currency_id;
    private String annualized_rate;
    private String daily_income;
    private int financial_management_id;
    private int status;
    private String financial_address;
    private String project_description;

    public String getFinancial_address() {
        return financial_address;
    }

    public void setFinancial_address(String financial_address) {
        this.financial_address = financial_address;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public String getOut_multiple() {
        return out_multiple;
    }

    public void setOut_multiple(String out_multiple) {
        this.out_multiple = out_multiple;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public int getPayment_currency_id() {
        return payment_currency_id;
    }

    public void setPayment_currency_id(int payment_currency_id) {
        this.payment_currency_id = payment_currency_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGet_currency_name() {
        return get_currency_name;
    }

    public void setGet_currency_name(String get_currency_name) {
        this.get_currency_name = get_currency_name;
    }

    public String getPayment_currency_name() {
        return payment_currency_name;
    }

    public void setPayment_currency_name(String payment_currency_name) {
        this.payment_currency_name = payment_currency_name;
    }

    public int getGet_currency_id() {
        return get_currency_id;
    }

    public void setGet_currency_id(int get_currency_id) {
        this.get_currency_id = get_currency_id;
    }

    public String getAnnualized_rate() {
        return annualized_rate;
    }

    public void setAnnualized_rate(String annualized_rate) {
        this.annualized_rate = annualized_rate;
    }

    public String getDaily_income() {
        return daily_income;
    }

    public void setDaily_income(String daily_income) {
        this.daily_income = daily_income;
    }

    public int getFinancial_management_id() {
        return financial_management_id;
    }

    public void setFinancial_management_id(int financial_management_id) {
        this.financial_management_id = financial_management_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
