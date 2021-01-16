package com.jkkg.hhtx.net.bean;

import com.mugui.base.base.Component;
import com.mugui.base.bean.JsonBean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户基本信息表
 *表名：user_account
 * @author HayDen
 */
@Component
@Getter
@Setter
@Accessors(chain = true)
public class CertificationBean extends JsonBean {

    //HX发送绑定手机号验证码
    public final static String bind_mobile = "bind_mobile";

    //HX发送绑定邮箱验证码
    public final static String bind_mailbox = "bind_mailbox";

    //HX绑定支付密码
    public final static String payment_password = "payment_password";

    //HX修改支付密码
    public final static String change_password = "change_password";

    /**
     *主键ID
     */
    private Integer user_account_id;

    /**
     *用户基本信息表ID
     */
    private Integer user_id;

    /**
     *用户姓名
     */
    private String user_name;

    /**
     *用户头像
     */
    private String user_img;

    /**
     *用户昵称
     */
    private String user_call;

    /**
     *证件号
     */
    private String user_certificate_no;

    /**
     *国籍
     */
    private String user_country;

    /**
     *地区(省、市)
     */
    private String user_region;

    /**
     *信息认证状态(0：待审核  2：审核成功  3:审核失败)
     */
    private Integer user_account_static;

    /**
     *审核意见
     */
    private String user_proposal;

    /**
     *人面照
     */
    private String user_self;

    /**
     *证件照反面
     */
    private String user_card_photo_back;

    /**
     *证件照正面
     */
    private String user_card_photo_just;

    /**
     *手持证件照
     */
    private String user_card_photo_hold;

    /**
     *创建时间
     */
    private Date user_account_create_time;

    /**
     *修改时间
     */
    private Date user_account_update_time;

    /**
     *审核时间
     */
    private Date user_account_examine_time;

    private Integer[] userIds;


}
