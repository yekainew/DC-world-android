package com.mugui.sql.loader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;
import com.mugui.sql.util.StringUtils;

public class Update extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7687091899204637392L;
	/*
	 * 基本类型
	 */
	public final static int TYPE_BASE = 0;
	/*
	 * 增量更新类型
	 */
	public final static int TYPE_INCREMENT = 1;

	public Update(JsonBean bean, int type) {
		sql = new StringBuilder();
		switch (type) {
		case TYPE_BASE:
			queryBase(bean);
			break;
		case TYPE_INCREMENT:
			queryIncrement(bean);
			break;

		default:
			break;
		}
	}

	private Update queryBase(JsonBean bean) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		sql.append("UPDATE `").append( attr.getTABLE()).append("` SET ");
		int i = 1;
		List<Field> fields = attr.getFields();
		for (; i < fields.size(); i++) {
			try {
				Object value = fields.get(i).get(bean);
				if (value != null) {
					sql.append("`").append(fields.get(i).getName()).append("`=?");
					i++;
					addParameter(value);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (; i < fields.size(); i++) {
			try {
				Object value = fields.get(i).get(bean);
				if (value != null) {
					sql.append(",`").append(fields.get(i).getName()).append("`=?");
					addParameter(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.append(" WHERE `").append(attr.getKEY()).append("`=? ");
		addParameter(bean.get(attr.getKEY()));
		return this;
	}

	private Update queryIncrement(JsonBean bean) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		sql.append("UPDATE `").append( attr.getTABLE()).append("` SET ");
		int i = 1;
		Field[] fields = attr.getFields().toArray(new Field[0]);
		ArrayList<Object> list = new ArrayList<Object>();
		for (; i < fields.length; i++) {
			try {
				Object value = fields[i].get(bean);
				if (value != null) {
					String sql = handleFieldByIncrement(fields[i], bean, list);
					if (sql == null)
						continue;
					this.sql.append(sql);
					i++;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (; i < fields.length; i++) {
			try {
				Object value = fields[i].get(bean);
				if (value != null) {
					String sql = handleFieldByIncrement(fields[i], bean, list);
					if (sql != null) {
						this.sql.append(",").append(sql);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.append(" WHERE `").append(attr.getKEY()).append("`=? ");
		addParameter(bean.get(attr.getKEY()));
		return this;
	}

	private String handleFieldByIncrement(Field fields, JsonBean bean, ArrayList<Object> list)
			throws IllegalArgumentException, IllegalAccessException {
		if (fields.getType() == int.class || fields.getType() == Integer.class) {
			if ((int) fields.get(bean) == 0) {
				return null;
			}
			return "`" + fields.getName() + "`='" + fields.get(bean).toString() + "'+" + "`" + fields.getName() + "`";
		} else if (fields.getType() == BigDecimal.class) {
			if (((BigDecimal) fields.get(bean)).compareTo(BigDecimal.ZERO) == 0) {
				return null;
			}
			return "`" + fields.getName() + "`='"
					+ ((BigDecimal) fields.get(bean)).setScale(8, BigDecimal.ROUND_HALF_DOWN) + "'+" + "`"
					+ fields.getName() + "`";
		} else {
			Object object = fields.get(bean);
			list.add(object);
			addParameter(object);
			return "`" + fields.getName() + "`=?";
		}
	}

	/**
	 * 创建基本的更新语句
	 * 
	 * @param bean
	 * @return
	 */
	public static Update q(JsonBean bean) {
		return new Update(bean, TYPE_BASE);
	}

	/**
	 * 创建增量更新语句
	 * 
	 * @auther 木鬼
	 * @param bean
	 * @return
	 */
	public static Update increment(JsonBean bean) {
		return new Update(bean, TYPE_INCREMENT);
	}

}
