package com.mugui.block.btc.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BTCFee extends JsonBean {

    private BigDecimal fastestFee;
    private BigDecimal halfHourFee;
    private BigDecimal hourFee;

}
