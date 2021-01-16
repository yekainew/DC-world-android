package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

public class PoolConfigBean extends JsonBean {

    /**
     * optimal_keep : 0
     * invest_type_id : 13
     * team_people_num : 5
     * invest_address : TU8yDTWmNg69CUS6PesMPvUL1YwEs2K8uW
     * HG_scale : 1
     * team_push_num : 1000
     * freed_type_name : DC
     * invest_type_name : DC
     * freed_type_id : 13
     * hold_area_id : 1
     * hold_area_name : DC矿机
     * holdAreaConfBean : {"hold_area_id":1,"freed_status":1,"freed_start_time":"2020-10-09 17:58:00","conf_id":1,"freed_now_time":"2020-10-09 17:58:03","min_invest_num":"100","max_invest_num":"50000","release_total":"20","released_total_days":1}
     * freed_status : 0
     */

    private String optimal_keep;
    private int invest_type_id;
    private int team_people_num;
    private String invest_address;
    private String HG_scale;
    private String team_push_num;
    private String freed_type_name;
    private String invest_type_name;
    private int freed_type_id;
    private int hold_area_id;
    private String hold_area_name;
    private HoldAreaConfBeanBean holdAreaConfBean;
    private int freed_status;

    public String getOptimal_keep() {
        return optimal_keep;
    }

    public void setOptimal_keep(String optimal_keep) {
        this.optimal_keep = optimal_keep;
    }

    public int getInvest_type_id() {
        return invest_type_id;
    }

    public void setInvest_type_id(int invest_type_id) {
        this.invest_type_id = invest_type_id;
    }

    public int getTeam_people_num() {
        return team_people_num;
    }

    public void setTeam_people_num(int team_people_num) {
        this.team_people_num = team_people_num;
    }

    public String getInvest_address() {
        return invest_address;
    }

    public void setInvest_address(String invest_address) {
        this.invest_address = invest_address;
    }

    public String getHG_scale() {
        return HG_scale;
    }

    public void setHG_scale(String HG_scale) {
        this.HG_scale = HG_scale;
    }

    public String getTeam_push_num() {
        return team_push_num;
    }

    public void setTeam_push_num(String team_push_num) {
        this.team_push_num = team_push_num;
    }

    public String getFreed_type_name() {
        return freed_type_name;
    }

    public void setFreed_type_name(String freed_type_name) {
        this.freed_type_name = freed_type_name;
    }

    public String getInvest_type_name() {
        return invest_type_name;
    }

    public void setInvest_type_name(String invest_type_name) {
        this.invest_type_name = invest_type_name;
    }

    public int getFreed_type_id() {
        return freed_type_id;
    }

    public void setFreed_type_id(int freed_type_id) {
        this.freed_type_id = freed_type_id;
    }

    public int getHold_area_id() {
        return hold_area_id;
    }

    public void setHold_area_id(int hold_area_id) {
        this.hold_area_id = hold_area_id;
    }

    public String getHold_area_name() {
        return hold_area_name;
    }

    public void setHold_area_name(String hold_area_name) {
        this.hold_area_name = hold_area_name;
    }

    public HoldAreaConfBeanBean getHoldAreaConfBean() {
        return holdAreaConfBean;
    }

    public void setHoldAreaConfBean(HoldAreaConfBeanBean holdAreaConfBean) {
        this.holdAreaConfBean = holdAreaConfBean;
    }

    public int getFreed_status() {
        return freed_status;
    }

    public void setFreed_status(int freed_status) {
        this.freed_status = freed_status;
    }

    public static class HoldAreaConfBeanBean extends JsonBean{
        /**
         * hold_area_id : 1
         * freed_status : 1
         * freed_start_time : 2020-10-09 17:58:00
         * conf_id : 1
         * freed_now_time : 2020-10-09 17:58:03
         * min_invest_num : 100
         * max_invest_num : 50000
         * release_total : 20
         * released_total_days : 1
         */

        private int hold_area_id;
        private int freed_status;
        private String freed_start_time;
        private int conf_id;
        private String freed_now_time;
        private String min_invest_num;
        private String max_invest_num;
        private String release_total;
        private int released_total_days;

        public int getHold_area_id() {
            return hold_area_id;
        }

        public void setHold_area_id(int hold_area_id) {
            this.hold_area_id = hold_area_id;
        }

        public int getFreed_status() {
            return freed_status;
        }

        public void setFreed_status(int freed_status) {
            this.freed_status = freed_status;
        }

        public String getFreed_start_time() {
            return freed_start_time;
        }

        public void setFreed_start_time(String freed_start_time) {
            this.freed_start_time = freed_start_time;
        }

        public int getConf_id() {
            return conf_id;
        }

        public void setConf_id(int conf_id) {
            this.conf_id = conf_id;
        }

        public String getFreed_now_time() {
            return freed_now_time;
        }

        public void setFreed_now_time(String freed_now_time) {
            this.freed_now_time = freed_now_time;
        }

        public String getMin_invest_num() {
            return min_invest_num;
        }

        public void setMin_invest_num(String min_invest_num) {
            this.min_invest_num = min_invest_num;
        }

        public String getMax_invest_num() {
            return max_invest_num;
        }

        public void setMax_invest_num(String max_invest_num) {
            this.max_invest_num = max_invest_num;
        }

        public String getRelease_total() {
            return release_total;
        }

        public void setRelease_total(String release_total) {
            this.release_total = release_total;
        }

        public int getReleased_total_days() {
            return released_total_days;
        }

        public void setReleased_total_days(int released_total_days) {
            this.released_total_days = released_total_days;
        }
    }
}
