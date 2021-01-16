package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class InviteUserCountBean extends JsonBean {


    private BigDecimal self_hold_num;
    private int team_size;
    private BigDecimal sum_team_num;
    private BigDecimal sum_self_num;
    private BigDecimal sum_push_num;
    private int push_size;
    private int rank;
    private boolean team;
    private BigDecimal income_num;
    private BigDecimal direct_push_num;
    /**
     * 全网挖矿总算力
     */
    private BigDecimal net_all_power=BigDecimal.ZERO;
    /**
     * 全网推广总算力
     */
    private BigDecimal net_push_power=BigDecimal.ZERO;

    /**
     * 全网产出
     */
    private BigDecimal now_output_dc=BigDecimal.ZERO;

}
