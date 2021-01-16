package com.mugui.sql.loader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.JsonBeanAttr;

/**
 * where 构造
 *
 * @author 木鬼
 */
public class Where extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8792016834022466177L;

	public static Where q() {
		return new Where();
	}

	/**
	 * 得到一个或多个jsonbean构造的where条件
	 * 
	 * @auther 木鬼
	 * @return
	 */
	public static Where q(JsonBean... attrs) {
		return new Where().query(attrs);
	}

	public Where() {
		sql = new StringBuilder();
	}

	public Where query(JsonBean... attrs) {
		queryEqualFields(attrs);
		queryBaseTerm(attrs);
		return this;
	}

	public void queryBaseTerm(JsonBean... attrs) {
		for (JsonBean bean : attrs) {
			List<Field> fields = JsonBeanAttr.getAttr(bean).getFields();
			for (Field f : fields) {
				f.setAccessible(true);
				Object object;
				try {
					object = f.get(bean);
					if (object != null) {
						and("`" + JsonBeanAttr.getAttr(bean).getTABLE() + "`.`" + f.getName() + "`", object);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Where and(String name, Object object) {
		if (sql == null) {
			sql = new StringBuilder();
		}
		sql.append(" AND ");
		sql.append(" ").append(name).append("=?");
		addParameter(object);
		return this;
	}

	/**
	 * 处理jsonbean中对等字段条件
	 * 
	 * @auther 木鬼
	 * @param attrs
	 * @return
	 */
	public Where queryEqualFields(JsonBean... attrs) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(attrs[0]);
		for (int j = 1; j < attrs.length; j++) {
			JsonBeanAttr attr2 = JsonBeanAttr.getAttr(attrs[j]);
			String[] equalFields = attr.equalFields(attrs[j]);
			for (String field : equalFields) {
				if (!sql.toString().equals("")) {
					sql.append(" AND ");
				}
				sql.append(" `").append(attr.getTABLE()).append("`.`").append(field + "`=")
						.append("`" + attr2.getTABLE()).append("`.").append(field).append("` ");
			}
		}
		return this;
	}

	/**
	 * 多数据查询
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where in(String filed_name, String value) {
		sql.append(" AND ");
		sql.append("`" + filed_name + "`").append(" in (").append(value.toString()).append(") ");
		return this;
	}

	/**
	 * in里面带参数
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where in(String filed_name, Select value) {
		sql.append(" and ").append("`" + filed_name + "`").append(" in (").append(value.toString()).append(") ");
		addParameter(value.getParameter());
		return this;
	}

	/**
	 * 模糊查询
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where like(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append(" like '%").append(value).append("%' ");
		return this;
	}

	/**
	 * 等于 =
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where eq(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append("=? ");
		addParameter(value);
		return this;
	}

	/**
	 * 不等于 !=
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where ne(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append("!=?");
		addParameter(value);
		return this;
	}

	/**
	 * 大于 >
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where gt(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append(">?");
		addParameter(value);
		return this;
	}

	/**
	 * 小于 <
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where lt(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append("<?");
		addParameter(value);
		return this;
	}

	/**
	 * 大于等于 >=
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where ge(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append(">=?");
		addParameter(value);
		return this;
	}

	/**
	 * 小于 <=
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where le(String filed_name, Object value) {
		sql.append(" and ").append("`" + filed_name + "`").append("<=?");
		addParameter(value);
		return this;
	}

	/**
	 * 取中间
	 * 
	 * @param filed_name
	 * @param start
	 * @param end
	 * @return
	 */
	public Where between(String filed_name, String start, String end) {
		String term = "'" + start + "','" + end + "'";
		sql.append(" and ").append("`" + filed_name + "`").append(" BETWEEN ").append(term);
		return this;
	}

	/**
	 * 排序
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where orderByDESC(String filed_name) {
		sql.append(" order by ").append("`" + filed_name + "`").append(" DESC");
		return this;
	}

	public Where orderByDESCKeyId(JsonBean bean) {
		return orderByDESC(JsonBeanAttr.getAttr(bean).getKEY());
	}

	/**
	 * 排序
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where orderByASC(String filed_name) {
		sql.append(" order by ").append("`" + filed_name + "`").append(" ASC");
		return this;
	}

	public Where orderByASCKeyId(JsonBean bean) {
		return orderByASC(JsonBeanAttr.getAttr(bean).getKEY());
	}

	/**
	 * 分组
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where groupBy(String filed_name) {
		sql.append(" GROUP BY ").append("`" + filed_name + "`");
		return this;
	}

	/**
	 * 不等于多条件
	 * 
	 * @param filed_name
	 * @param value
	 * @return
	 */
	public Where notIn(String filed_name, String value) {
		sql.append(" and ").append("`" + filed_name + "`").append(" NOT IN (").append(value).append(") ");
		return this;
	}

	/**
	 * 不等于NULL
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where neNull(String filed_name) {
		sql.append(" and ").append("`" + filed_name + "`").append("!=null ");
		return this;
	}

	/**
	 * 过滤NULL的数据
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where isNotNull(String filed_name) {
		sql.append(" and ").append("`" + filed_name + "`").append(" is NOT null ");
		return this;
	}

	/**
	 * 查询NULL的数据
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where isNull(String filed_name) {
		sql.append(" and ").append("`" + filed_name + "`").append(" is null ");
		return this;
	}

	/**
	 * 只取某些数据
	 * 
	 * @param filed_name
	 * @return
	 */
	public Where limit(int filed_name) {
		sql.append(" limit ").append("`" + filed_name + "`");
		return this;
	}

	/**
	 * 操作符用于合并两个或多个 SELECT 语句的结果集(不允许重复)
	 * 
	 * @param filed_name 表之间以逗号隔开
	 * @return
	 */
	public Where union(String filed_name) {
		String[] split = filed_name.split(",");
		for (int i = 0; i < split.length; i++) {
			if (i == split.length - 1) {
				sql.append(split[i]);
			} else {
				sql.append(split[i]).append(" UNION ");
			}
		}
		return this;
	}

	/**
	 * 操作符用于合并两个或多个 SELECT 语句的结果集(允许重复)
	 * 
	 * @param filed_name 表之间以逗号隔开
	 * @return
	 */
	public Where unionAll(String filed_name) {
		String[] split = filed_name.split(",");
		for (int i = 0; i < split.length; i++) {
			if (i == split.length - 1) {
				sql.append(split[i]);
			} else {
				sql.append(split[i]).append(" UNION ALL ");
			}
		}
		return this;
	}

	/**
	 * 不推荐的使用方法
	 * 
	 * @param sqlName
	 * @return
	 */
	public Where where(String sqlName) {
		sql.append(sqlName);
		return this;
	}

}
