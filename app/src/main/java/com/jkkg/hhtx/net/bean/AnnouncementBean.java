package com.jkkg.hhtx.net.bean;
 
import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 公告
 *
 * @author wangTao
 * @email 2450572350@gmail.com
 * @date 2020-04-27 10:36:20
 */
@Getter
@Setter
@Accessors(chain = true)
public class AnnouncementBean extends JsonBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    private BigInteger announcement_id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date create_time;

    private Date update_time;
    /**
     * 状态,1启用,0禁用
     */
    private Integer state;
    /**
     * 公告到期时间,空为不限制
     */
    private Date end_time;
    /**
     * 1是矿机2是交易所.
     */
    private Integer type;

}
