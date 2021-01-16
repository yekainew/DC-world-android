package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLField;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class InformationBean extends JsonBean {
    private static final long serialVersionUID = 1L;

    /**
     * 资讯
     */

    @SQLField(PRIMARY_KEY = true)
    private Integer information_id;
    @SQLField
    private Integer user_id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     *
     */
    private Date create_time;
    /**
     *
     */
    private Date update_time;
    /**
     * 看涨
     */
    private Integer bullish;
    /**
     * 看跌
     */
    private Integer bearish;
    /**
     * 评论数
     */
    private Integer number_of_comments;
    /**
     * 批准状态  1是待审核,2是已经通过,3 拒绝发布，4 删除
     */
    protected Integer approval_status;
    /**
     * 评审时间
     */
    protected Date review_time;
    /**
     * 审核说明
     */
    protected String review_instructions;
    /**
     * 删除理由
     */
    protected String del_reason;
    /**
     * 图片url集合, [] 数组
     */
    private String imgs;

    /**
     * 用户是否点赞了,点赞的类型是什么1看涨2看叠
     */
    protected Integer bullish_bearish_type;




}
