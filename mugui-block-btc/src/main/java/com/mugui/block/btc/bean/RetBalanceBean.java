package com.mugui.block.btc.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RetBalanceBean extends JsonBean {

    private String type;

    private String network;
    private String hash;

    /**
     * 发送
     */
    private BigDecimal spend;

    /**
     * 接收
     */
    private BigDecimal receive;
}
