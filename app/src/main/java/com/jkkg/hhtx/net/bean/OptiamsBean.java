package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OptiamsBean extends JsonBean {

    /**
     * hold_area_id : 1
     * optimal_keep : 0E-8
     * date : 2020-10-21 18:31:31
     * optimal_keep_id : 11
     */

    private String hold_area_id;
    private BigDecimal optimal_keep;
    private Date date;
    private int optimal_keep_id;


}
