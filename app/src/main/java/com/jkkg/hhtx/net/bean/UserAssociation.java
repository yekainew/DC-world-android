package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 用户邀请表
*表名：user_association
 * @author HayDen
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserAssociation extends JsonBean {

	/**
	*主键ID
	*/
	private Integer user_association_id;

	/**
	*用户基本表id
	*/
	private Integer user_id;

	/**
	*邀请奖励表ID
	*/
	private Integer user_reward_id;

	private String address;

	/**
	*邀请人用户ID，-1为顶级用户
	*/
	private Integer user_father;

	/**
	*关联此2人时，使用的code
	*/
	private String user_association_code;

	/**
	*状态:0、以关联。1、已删除
	*/
	private Integer user_association_status;

	/**
	*创建时间
	*/
	private Date user_association_create_time;

	/**
	*删除时间
	*/
	private Date user_association_delete_time;

	/**
	*0、有效。1、失效
	*/
	private Integer user_association_delete_status;
}
