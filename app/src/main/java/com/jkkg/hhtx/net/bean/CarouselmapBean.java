package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**

 * 首页轮播图

 *

 * @author wangTao

 * @email 2450572350@gmail.com

 * @date 2019-10-24 21:56:53

 */

@Getter

@Setter

@Accessors(chain = true)


public class CarouselmapBean extends JsonBean {

	private static final long serialVersionUID = 1L;




	/**

	 * 主键

	 */





	private Integer wheel_planting_id;

	/**

	 * 主键名称

	 */


	private String wheel_planting_name;

	/**

	 * 连接URL

	 */


	private String wheel_planting_url;

	/**

	 * 图片地址

	 */



	private String rotary_planting_map;

	/**

	 * 0有效 1无效

	 */



	private Integer wheel_planting_delete;

	/**

	 * 创建时间

	 */



	private Date wheel_planting_create_time;

	/**

	 * 删除时间

	 */



	private Date rotary_planting_delete_time;

	/**

	 * 备注

	 */



	private String rotary_planting_remarks;




	/**

	 * 轮播图所关联的服务器

	 */



	private String wheel_planting_server;




	/**

	 * 轮播图类型 (0 无类型 1 浏览器网页类型 2 内嵌浏览器网页类型 3 悬浮窗式网页类型 4 指定app应用页类型 )

	 */



	private Integer wheel_planting_type;




}