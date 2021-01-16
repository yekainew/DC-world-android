package com.mugui.sql.loader;

import java.lang.reflect.Field;
import java.util.List;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;
import com.mugui.sql.util.StringUtils;

public class Insert extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4885739010347526581L;

	public Insert(JsonBean bean) {
		sql = new StringBuilder();
		query(bean);
	}

	private Insert query(JsonBean bean) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		sql.append("INSERT INTO `").append(attr.getTABLE()).append("`(");
		int i = 0;
		int size = 0;
		List<Field> fields = attr.getFields();
		for (; i < fields.size(); i++) {
			try {
				Object object = bean.get(fields.get(i).getName());
				if (object != null && StringUtils.isNotBlank(String.valueOf(object))) {
					sql.append("`").append(fields.get(i++).getName()).append("`");
					size++;
					addParameter(object);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (; i < fields.size(); i++) {
			try {
				Object object = bean.get(fields.get(i).getName());
				if (object != null && StringUtils.isNotBlank(String.valueOf(object))) {
					sql.append(",`").append(fields.get(i).getName()).append("`");
					size++;
					addParameter(object);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sql.append(")VALUES (");
		i = 1;
		while (i < size) {
			sql.append("?,");
			i++;
		}
		sql.append("?) ");
		return this;

	}

	public static Insert q(JsonBean bean) {
		return new Insert(bean);
	}
}
