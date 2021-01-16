package com.jkkg.hhtx.net.bean;

 
import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;


/**
 * 短信模板配置表Bo
* @author HayDen
*/
@Getter
@Setter
@Accessors(chain = true)
public class MallSmsBo extends JsonBean {

	/**
	*用户ID
	*/
	private Integer userId;

	/**
	* 短信模板
	*/
	private String sms_code;

	/**
	 * 参数
	 * 格式示例: code:123456
	 */
	private Map<String,String> code;

	/**
	 * 1:邮箱
	 */
	private Integer max;

	/**
	 * 发送账号
	 */
	private String sms_log_number;

	/**
	 * 区号
	 */
	private String areaCode;

	//1：界面  2：后台
	private Integer appAdmin;
	private String imgCode;

}
