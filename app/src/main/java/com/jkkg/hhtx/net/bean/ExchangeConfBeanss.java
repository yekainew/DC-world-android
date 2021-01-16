package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ExchangeConfBeanss extends JsonBean {


    private int usable;
    private BigDecimal invite_exchange_scale;
    private String invite_exchange_address;
    private String invite_exchange_curreny;
    private String contract_address;

}
