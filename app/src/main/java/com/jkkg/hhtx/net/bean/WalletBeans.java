package com.jkkg.hhtx.net.bean;

public class WalletBeans {

    /**
     * usable : 99999
     * update_time : 2020-09-30 17:04:43
     * wallet_id : 2
     * create_time : 2020-09-27 17:29:18
     * user_id : 27688
     * frozen : 0
     * wallet_type : 1
     * version : 1
     * currency_id : 1
     */

    private String usable;
    private String update_time;
    private int wallet_id;
    private String create_time;
    private int user_id;
    private String frozen;
    private int wallet_type;
    private String version;
    private int currency_id;

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public int getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(int wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }
}
