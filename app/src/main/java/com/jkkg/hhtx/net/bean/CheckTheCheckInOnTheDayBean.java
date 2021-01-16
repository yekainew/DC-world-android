package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)

public class CheckTheCheckInOnTheDayBean extends JsonBean {

    /**
     * 签到表id
     */

    protected Integer checkin_id;
    /**
     *
     */
    protected Integer user_id;
    /**
     * 签到统计,1代表签到,0代表未签到
     */
    protected String check_in_statistics;
    /**
     * 创建时间
     */
    protected Date create_time;
    /**
     * 签到次数
     */
    protected Integer check_in_times;
    /**
     * 补签次数
     */
    protected Integer number_of_reissues;
    /**
     * 总收益
     */
    protected BigDecimal total_revenue;
    /**
     * 最后更新时间
     */
    protected Date update_time;

    /**
     * 下标,代表当前
     */
    protected Integer index;

    /**
     * 周一开始时间
     */
    protected Date startTime;

    /**
     * 当天是否已经签到,1是已经签到
     */
    protected Integer status;

    protected Integer continuous_check_in;


}
