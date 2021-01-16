package com.jkkg.hhtx.net.bean;

public class PowerRankListBean {

    /**
     * hold_area_id : 1
     * user_hold_update_time : 2020-10-09 19:02:15
     * hold_num : 200
     * user_hold_id : 2
     * user_id : 27764
     * user_hold_status : 0
     * bc_address : 被用户隐藏
     */

    private int hold_area_id;
    private String user_hold_update_time;
    private String hold_num;
    private int user_hold_id;
    private int user_id;
    private int user_hold_status;
    private String bc_address;

    public int getHold_area_id() {
        return hold_area_id;
    }

    public void setHold_area_id(int hold_area_id) {
        this.hold_area_id = hold_area_id;
    }

    public String getUser_hold_update_time() {
        return user_hold_update_time;
    }

    public void setUser_hold_update_time(String user_hold_update_time) {
        this.user_hold_update_time = user_hold_update_time;
    }

    public String getHold_num() {
        return hold_num;
    }

    public void setHold_num(String hold_num) {
        this.hold_num = hold_num;
    }

    public int getUser_hold_id() {
        return user_hold_id;
    }

    public void setUser_hold_id(int user_hold_id) {
        this.user_hold_id = user_hold_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_hold_status() {
        return user_hold_status;
    }

    public void setUser_hold_status(int user_hold_status) {
        this.user_hold_status = user_hold_status;
    }

    public String getBc_address() {
        return bc_address;
    }

    public void setBc_address(String bc_address) {
        this.bc_address = bc_address;
    }
}
