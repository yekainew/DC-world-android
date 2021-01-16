package com.jkkg.hhtx.net.bean;

public class TansferDetailBean {


    /**
     * transfer_conf_max_num : 500
     * transfer_conf_status : 0
     * transfer_conf_fee_scale : 0.003
     * transfer_conf_id : 1
     * transfer_conf_create_time : 2020-09-28 15:55:17
     * bc_name : TRX
     * transfer_conf_min_num : 10
     */

    private String transfer_conf_max_num;
    private int transfer_conf_status;
    private String transfer_conf_fee_scale;
    private int transfer_conf_id;
    private String transfer_conf_create_time;
    private String bc_name;
    private String transfer_conf_min_num;

    public String getTransfer_conf_max_num() {
        return transfer_conf_max_num;
    }

    public void setTransfer_conf_max_num(String transfer_conf_max_num) {
        this.transfer_conf_max_num = transfer_conf_max_num;
    }

    public int getTransfer_conf_status() {
        return transfer_conf_status;
    }

    public void setTransfer_conf_status(int transfer_conf_status) {
        this.transfer_conf_status = transfer_conf_status;
    }

    public String getTransfer_conf_fee_scale() {
        return transfer_conf_fee_scale;
    }

    public void setTransfer_conf_fee_scale(String transfer_conf_fee_scale) {
        this.transfer_conf_fee_scale = transfer_conf_fee_scale;
    }

    public int getTransfer_conf_id() {
        return transfer_conf_id;
    }

    public void setTransfer_conf_id(int transfer_conf_id) {
        this.transfer_conf_id = transfer_conf_id;
    }

    public String getTransfer_conf_create_time() {
        return transfer_conf_create_time;
    }

    public void setTransfer_conf_create_time(String transfer_conf_create_time) {
        this.transfer_conf_create_time = transfer_conf_create_time;
    }

    public String getBc_name() {
        return bc_name;
    }

    public void setBc_name(String bc_name) {
        this.bc_name = bc_name;
    }

    public String getTransfer_conf_min_num() {
        return transfer_conf_min_num;
    }

    public void setTransfer_conf_min_num(String transfer_conf_min_num) {
        this.transfer_conf_min_num = transfer_conf_min_num;
    }
}
