package com.mugui.sql.loader;

import android.os.Build;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;
import com.mugui.sql.JsonBeanAttr.FieldAttr;
import com.mugui.sql.SQLField;

public class Create extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838526314463110299L;

	public Create(Class<? extends JsonBean> class1) {
		sql=new StringBuilder();
		query(class1);
	}

	private Create query(Class<? extends JsonBean> class1) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(class1);
		String sql = "CREATE TABLE " + attr.getTABLE() + "(\n";
		FieldAttr[] fields = attr.getFieldAttr();
		boolean bool = false;
		for (FieldAttr f : fields) {
			if (bool)
				sql += ",\n`" + f.getField().getName() + "` ";
			else
				sql += "`" + f.getField().getName() + "` ";
			bool = true;

			SQLField annotation = f.getSql_field();
			if (annotation.DATA_TYPE().equals(""))
				continue;
			sql += handleDataType(annotation.DATA_TYPE(), f.getField()) + " ";
			if (annotation.PRIMARY_KEY()) {
				sql += annotation.PRIMARY_KEY_value() + " ";
			}
			if (annotation.AUTOINCREMENT()) {
				sql += annotation.AUTOINCREMENT_value() + " ";
			}
			if (!annotation.NULL()) {
				sql += annotation.NULL_value() + " ";
			}
			if (annotation.UNIQUE()) {
				sql += annotation.UNIQUE_value() + " ";
			}
			if (annotation.DEFAULT()) {
				sql += annotation.DEFAULT_value() + " " + annotation.DEFAULT_text() + " ";
			}
		}
		sql += ");";
		if (!bool)
			throw new NullPointerException(class1.getName() + " not find fields");
		System.out.println(sql);
		this.sql=new StringBuilder(sql);
//		DBConf.getDefaultDBConf().write("create_table_" + attr.getTABLE(), sql);
		return this;
	}

	private String handleDataType(String data_TYPE, Field f) {
		if (!data_TYPE.equals("Integer"))
			return data_TYPE;
		Class<?> tyClass = f.getType();
		if (tyClass == int.class || tyClass == Integer.class) {
			return data_TYPE;
		}
		if (tyClass == Long.class || tyClass == long.class) {
			return "VARCHAR(64)";
		}
		if (tyClass == String.class) {
			return "VARCHAR(64)";
		}
		if (tyClass == Character.class || tyClass == char.class) {
			return "VARCHAR(4)";
		}
		if (tyClass == Double.class || tyClass == BigDecimal.class || tyClass == double.class
				|| tyClass == BigInteger.class) {
			return "decimal(20,8)";
		}
			if (tyClass == Date.class || tyClass == java.sql.Date.class ) {
				return "datetime";
			}
		return data_TYPE;
	}

	public static Create q(Class<? extends JsonBean> class1) {
		return new Create(class1);
	}

}
