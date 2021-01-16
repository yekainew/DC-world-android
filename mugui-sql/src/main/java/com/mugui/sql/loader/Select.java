package com.mugui.sql.loader;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;

/**
 * select 语句构造器
 * 
 * @author 木鬼
 *
 */
public class Select extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1958440367463198775L;

	public Select() {
		sql = new StringBuilder("select");
	}

	/**
	 * 创建一个基本的select语句
	 * 
	 * @auther 木鬼
	 * @param per  自定义查询出的字段
	 * @param bean 表名
	 * @return
	 */
	public static Select q(String per, JsonBean bean) {
		return new Select().query(per, bean);
	}

	public static Select q(JsonBean bean) {
		return q("*", bean);
	}

	private JsonBean base = null;

	private Select query(String string, JsonBean bean) {
		this.base = bean;
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		sql.append(" ").append(string).append(" from `").append(attr.getTABLE()).append("` ");
		return this;
	}

	/**
	 * 左连接
	 * 
	 * @auther 木鬼
	 * @param sql
	 * @return
	 */
	public Select leftjoin(JsonBean... beans) {
		for (JsonBean bean : beans) {
			if (bean == base)
				continue;
			JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
			sql.append(" left join `").append(attr.getTABLE()).append("` on (")
					.append(Where.q().queryEqualFields(base, bean)).append(") ");
		}
		return this;
	}

	/**
	 * 左连接，并加入自定义条件
	 * 
	 * @auther 木鬼
	 * @param bean 左连接对象
	 * @param sql  自定义条件
	 * @return
	 */
	public Select leftjoin(JsonBean bean, String sql) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		this.sql.append(" left join `").append(attr.getTABLE()).append("` on (")
				.append(Where.q().queryEqualFields(base, bean)).append(" and ").append(sql).append(") ");
		return this;
	}

	private boolean iswhere = false;

	public Select where(JsonBean... bean) {
		if (!iswhere) {
			sql.append(" where  1=1  ");
			iswhere = true;
		}
		Where q = Where.q(bean);
		sql.append(q.toString());
		addParameter(q.getParameter());
		return this;
	}

	public Select where(Where where) {
		if (!iswhere) {
			sql.append(" where  1=1  ");
			iswhere = true;
		}
		sql.append(where.toString());
		addParameter(where.getParameter());
		return this;
	}

}
