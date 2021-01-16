package com.mugui.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.mugui.base.bean.JsonBean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class JsonBeanAttr {
	private static HashMap<Class<? extends JsonBean>, JsonBeanAttr> attr_map = new LinkedHashMap<>();

	public static JsonBeanAttr getAttr(JsonBean jsonBean) {
		return getAttr(jsonBean.getClass());
	}

	public static JsonBeanAttr getAttr(Class<? extends JsonBean> class1) {
		JsonBeanAttr jsonBeanAttr = attr_map.get(class1);
		if (jsonBeanAttr == null) {
			jsonBeanAttr = new JsonBeanAttr(class1);
			attr_map.put(class1, jsonBeanAttr);
		}
		return jsonBeanAttr;
	}

	private Class<? extends JsonBean> jsonbean;

	private JsonBeanAttr(JsonBean jsonbean) {
		this(jsonbean.getClass());
	}

	private JsonBeanAttr(Class<? extends JsonBean> class1) {
		this.jsonbean = class1;
	}

	private SQLDB sqldb = null;

	private HashMap<String, FieldAttr> fieldAttrs = null;

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class FieldAttr {
		SQLField sql_field;
		Field field;
	}

	public String getTABLE() {
		return getSqlDB().TABLE();
	}

	private SQLDB getSqlDB() {
		if (sqldb == null) {
			if (jsonbean.isAnnotationPresent(SQLDB.class)) {
				sqldb = jsonbean.getAnnotation(SQLDB.class);
				return sqldb;
			}
			throw new RuntimeException("该" + jsonbean.getClass() + "未拥有数据库属性");
		}
		return sqldb;
	}

	private HashMap<Class<? extends JsonBean>, String[]> equalFields = new LinkedHashMap<>();

	/**
	 * 得到与另一个jsonbean对等的字段集合
	 * 
	 * @auther 木鬼
	 * @param jsonBean
	 * @return 对等字段集合
	 */
	public String[] equalFields(JsonBean jsonBean) {
		String[] strings = equalFields.get(jsonBean.getClass());
		if (strings == null) {
			synchronized (JsonBeanAttr.class) {
				strings = equalFields.get(jsonBean.getClass());
				if (strings == null) {
					ArrayList<String> list = new ArrayList<>();
					HashMap<String, FieldAttr> field = getFieldAttrs();
					HashMap<String, FieldAttr> field2 = JsonBeanAttr.getAttr(jsonBean).getFieldAttrs();
					for (String key : field.keySet()) {
						FieldAttr fieldAttr = field2.get(key);
						if (fieldAttr != null) {
							list.add(key);
						}
					}
					strings = (String[]) list.toArray();
					// 互相对等
					equalFields.put(jsonBean.getClass(), strings);
					JsonBeanAttr.getAttr(jsonBean).equalFields.put(jsonbean, strings);
				}
			}
		}
		return strings;
	}

	private HashMap<String, FieldAttr> getFieldAttrs() {
		if (fieldAttrs == null) {
			fieldAttrs = new LinkedHashMap<String, JsonBeanAttr.FieldAttr>();
			Field[] declaredFields = jsonbean.getDeclaredFields();
			for (Field f : declaredFields) {
				if (f.isAnnotationPresent(SQLField.class)) {
					f.setAccessible(true);
					SQLField annotation = f.getAnnotation(SQLField.class);
						if (!annotation.PRIMARY_KEY()) {
							continue;
						}
						fieldAttrs.put(f.getName(), new FieldAttr().setField(f).setSql_field(annotation));
						break;
				}
			}
			for (Field f : declaredFields) {
				if (f.isAnnotationPresent(SQLField.class)) {
					f.setAccessible(true);
					SQLField annotation = f.getAnnotation(SQLField.class);
						if (!annotation.PRIMARY_KEY()) {
							fieldAttrs.put(f.getName(), new FieldAttr().setField(f).setSql_field(annotation));
						}
				}
			}
		}
		return fieldAttrs;
	}

	private List<Field> fields = null;

	public FieldAttr[] getFieldAttr() {
		return getFieldAttrs().values().toArray(new FieldAttr[0]);
	}

	public List<Field> getFields() {
		if (fields == null) {
			synchronized (JsonBeanAttr.class) {
				if (fields == null) {
					ArrayList<Field> list = new ArrayList<>();
					for (FieldAttr attr : getFieldAttrs().values()) {
						list.add(attr.field);
					}
					fields = list;
				}
			}
		}
		return fields;
	}

	/**
	 * 得到字段集，以[,]号隔开
	 * 
	 * @auther 木鬼
	 * @return
	 */
	public String getFieldToString() {
		List<Field> fields2 = getFields();
		String str = null;
		for (Field field : fields2) {
			if (str == null)
				str = field.getName();
			else
				str = "," + field.getName();
		}
		return str;
	}

	public String getKEY() {
		return getFields().get(0).getName();
	}
}
