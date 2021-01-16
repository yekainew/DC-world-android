package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLField;
 
 
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 意见反馈
 *
 * @author wangTao
 * @date 2020-05-28 17:40:11
 */
@Getter
@Setter
@Accessors(chain = true)

public class FeedbackBean extends JsonBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */

    @SQLField(PRIMARY_KEY = true)
     
    private BigInteger id;
    /**
     *
     */
    @SQLField
     
    private Integer user_id;
    /**
     * 内容
     */
    @SQLField
     
    private String content;
    /**
     *
     */
    @SQLField
     
    private Date create_time;
    /**
     *
     */
    @SQLField
     
    private Date update_time;
    /**
     * 1是创建,2是已经回复
     */
    @SQLField
     
    private Integer state;
    /**
     * 1是交易所2是矿机
     */
    @SQLField
     
    private Integer type;

    /**
     * 回复
     */
    @SQLField
     
    private String reply;

}
