package com.jkkg.hhtx.net.bean;


import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 节点释放
 *
 * @author wangTao
 * @date 2020-11-21 16:25:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class NodeEmancipationBean extends JsonBean {
    private static final long serialVersionUID = 1L;

    /**
     * 节点 释放
     */

    protected Integer node_emancipation_id;
    /**
     *
     */
    protected Integer node_super_id;
    protected String dc_type_name;
    /**
     *
     */
    protected Integer dc_type_id;
    /**
     * 数量
     */
    protected BigDecimal amount;
    /**
     * 释放天数
     */
    protected Integer heaven;

    /**
     * 已经释放
     */
    protected BigDecimal already_released;
    /**
     * 剩余待释放
     */
    protected BigDecimal remaining_to_be_released;
    /**
     * 剩余释放天数
     */
    protected Integer remaining_release_days;
    /**
     * 1是认购奖励  2是 社区奖励
     */
    protected Integer type;
    /**
     * 1创建,2是发放完成.
     */
    protected Integer state;
    /**
     * 每日释放率
     */
    protected BigDecimal release_ratio;

    /**
     *
     */
    protected Date create_time;
    /**
     *
     */
    protected Date update_time;


    /**
     * 可提取
     */
    protected BigDecimal extractable;

    /**
     * 已提取
     */
    protected BigDecimal extracted;


}
