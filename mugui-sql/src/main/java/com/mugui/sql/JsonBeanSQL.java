package com.mugui.sql;

import com.mugui.base.Mugui;
import com.mugui.base.bean.JsonBean;
import com.mugui.sql.loader.Delete;
import com.mugui.sql.loader.Select;
import com.mugui.sql.loader.Update;

/**
 * JsonBean 类的sql语句生成器
 * 
 * @author 木鬼
 *
 */
public class JsonBeanSQL implements Mugui {
	/**
	 * 根据key得到数据
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param bean
	 * @return
	 */
	public static Select get(JsonBean bean) {
		return selectLoader(bean);
	}

	/**
	 * 得到基本查询语句<br>
	 * 默认将采用左连接查询<br>
	 * 且按照bean数组中的顺序进行左连接
	 */
	public static Select select(JsonBean... beans) {
		if (beans.length == 1) {
			return selectLoader(beans[0]).where(beans);
		}
		Select selectLoader = selectLoader(beans[0]);
		selectLoader.leftjoin(beans);
		selectLoader.where(beans);
		return selectLoader;
	}

	/**
	 * 得到基本更新语句
	 */
	public static Update update(JsonBean bean) {
		return updateLoader(bean);
	}

	/**
	 * 得到基本统计语句
	 */
	public static Select count(JsonBean bean) {

		return countLoader(bean);
	}

	/**
	 * 得到基本删除语句
	 */
	public static Delete delete(JsonBean bean) {
		return deleteLoader(bean);

	}

	/**
	 * 得到查询装载器
	 * 
	 * @auther 木鬼
	 */
	public static Select selectLoader(JsonBean bean) {
		return Select.q(bean);
	}

	/**
	 * 更新装载器
	 * 
	 * @auther 木鬼
	 */
	public static Update updateLoader(JsonBean bean) {
		return Update.q(bean);
	}

	/**
	 * 统计装载器
	 * 
	 * @auther 木鬼
	 */
	public static Select countLoader(JsonBean bean) {
		Select q = Select.q("count(0)", bean);
		return q;
	}

	/**
	 * 删除装载器
	 * 
	 * @auther 木鬼
	 */
	public static Delete deleteLoader(JsonBean bean) {
		return Delete.q(bean);
	}
}
