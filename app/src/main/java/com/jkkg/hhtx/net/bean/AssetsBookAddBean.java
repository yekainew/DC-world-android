package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AssetsBookAddBean extends JsonBean {

    /**
     * 钱包底层
     */
    protected String wallet_bottom;
    /**
     * 钱包地址
     */
    protected String address;

    /**
     * 备注，可不填
     */
    protected String remarks;



}
