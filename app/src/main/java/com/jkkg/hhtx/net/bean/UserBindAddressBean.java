package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserBindAddressBean extends JsonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585905420888381945L;

	private Integer user_address_id;

	private Integer user_id;

	private String address;

	private String bc_name;

	private Date user_address_create_time;

}
