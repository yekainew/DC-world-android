package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WalletDCBean extends JsonBean {


    private String usable;
    private String update_time;
    private int wallet_id;
    private String create_time;
    private int user_id;
    private String frozen;
    private int wallet_type;
    private String version;
    private int currency_id;

}
