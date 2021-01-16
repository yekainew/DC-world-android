package com.jkkg.hhtx.net.bean;

public class HelpBean {

    /**
     * system_id : 56
     * system_extra : resonance
     * system_key : help
     * to_update : 2
     * system_value : http://47.242.173.217:8033/help/html/resonance.html
     */

    private int system_id;
    private String system_extra;
    private String system_key;
    private int to_update;
    private String system_value;

    public int getSystem_id() {
        return system_id;
    }

    public void setSystem_id(int system_id) {
        this.system_id = system_id;
    }

    public String getSystem_extra() {
        return system_extra;
    }

    public void setSystem_extra(String system_extra) {
        this.system_extra = system_extra;
    }

    public String getSystem_key() {
        return system_key;
    }

    public void setSystem_key(String system_key) {
        this.system_key = system_key;
    }

    public int getTo_update() {
        return to_update;
    }

    public void setTo_update(int to_update) {
        this.to_update = to_update;
    }

    public String getSystem_value() {
        return system_value;
    }

    public void setSystem_value(String system_value) {
        this.system_value = system_value;
    }
}
