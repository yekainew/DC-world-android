package com.mugui.sql;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.mugui.base.Mugui;
import com.mugui.base.bean.JsonBean;
import com.mugui.base.util.Other;
import com.mugui.sql.loader.Create;
import com.mugui.sql.loader.Delete;
import com.mugui.sql.loader.Insert;
import com.mugui.sql.loader.Select;
import com.mugui.sql.loader.Update;
import com.mugui.sql.loader.Where;
import com.mugui.sql.util.StringUtils;

/**
 * sql 主模块， 包含各种sql的查询与处理<br>
 * 自动对Jsonbean解析并生成相应的查询语句
 * 
 * @author 木鬼
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class SqlModel implements Mugui, SqlModeApi {

	private final static HashMap<String, Object> SQL_SERVER_MAP = new HashMap<>();

	/**
	 * 得到默认的sql服务
	 * 
	 * @auther 木鬼
	 * @return
	 */
	public SqlServer getSqlServer() {
		return getSqlServer(DBConf.getDefaultDBConf().getUrl());
	}

	private <T extends JsonBean> SqlServer getSqlServer(T bean) {
		return getSqlServer(bean.getClass());
	}

	/**
	 * 通过url得到一个sql服务<br>
	 * 不建议使用，若该url并未加载则无法拿到对应的sqlserver
	 * 
	 * @auther 木鬼
	 * @param url
	 * @return
	 */
	@Deprecated
	public SqlServer getSqlServer(String url) {
		if (url == null) {
			return new SqlServer();
		}
		SqlServer sqlServer = (SqlServer) SQL_SERVER_MAP.get(url);
		if (sqlServer == null) {
			sqlServer = new SqlServer(url);
			SQL_SERVER_MAP.put(url, sqlServer);
		}
		return sqlServer;
	}

	/**
	 * 通过jsonbean 得到一个sql服务
	 * 
	 * @auther 木鬼
	 * @param class1
	 * @return
	 */
	public SqlServer getSqlServer(Class<? extends JsonBean> class1) {
		SQLDB object2 = (SQLDB) SQL_SERVER_MAP.get(class1.getName());
		if (object2 != null) {
			return (SqlServer) SQL_SERVER_MAP.get(object2.url());
		}
		synchronized (SqlModel.class) {
			object2 = (SQLDB) SQL_SERVER_MAP.get(class1.getName());
			if (object2 != null) {
				return (SqlServer) SQL_SERVER_MAP.get(object2.url());
			}
			if (class1.isAnnotationPresent(SQLDB.class)) {
				SQLDB annotation = class1.getAnnotation(SQLDB.class);
				if (annotation.url().equals("")) {
					SqlServer sqlServer = new SqlServer();
					SQL_SERVER_MAP.put(annotation.url(), sqlServer);
					SQL_SERVER_MAP.put(class1.getName(), annotation);
					return sqlServer;
				}
				DBConf defaultConf = DBConf.getDefaultDBConf();
				DBConf dbConf = null;
				if (StringUtils.isBlank(annotation.url())) {
					dbConf = defaultConf;
				} else {
					dbConf = DBConf.getDBConf(annotation.url());
					if (dbConf == null) {
						String user = annotation.user().equals("") ? defaultConf.getUser() : annotation.user();
						String pwd = annotation.pwd().equals("") ? defaultConf.getPwd() : annotation.pwd();
						dbConf = new DBConf(defaultConf.getDrive(), annotation.url(), user, pwd);
					}
				}
				SqlServer sqlServer = new SqlServer(dbConf.getUrl());
				SQL_SERVER_MAP.put(annotation.url(), sqlServer);
				SQL_SERVER_MAP.put(class1.getName(), annotation);
				return sqlServer;
			}
		}
		throw new RuntimeException("该" + class1.getName() + " 未拥有sql配置");
	}

	@Override
	public boolean clearTableData(Class<? extends JsonBean> class1) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(class1);
		System.out.println("clearTableData:" + attr.getTABLE());
		SqlServer sqlServer = getSqlServer();
		String clearSql = sqlServer.getSql("clear_table_data").replaceAll("<TABLE>", attr.getTABLE());
		return sqlServer.update(clearSql);
	}

	@Override
	public void createTable(Class<? extends JsonBean> class1) {
		if (class1.isAnnotationPresent(SQLDB.class)) {
			SQLDB description = class1.getAnnotation(SQLDB.class);
			if (!isTable(description.TABLE())) {
				Create q = Create.q(class1);
				getSqlServer(class1).update(q.toString());
			}
		}
	}

	@Override
	public boolean isTable(String TABLE) {
		TableMode tMode = getSqlServer().selectBy("is_table", TABLE);
		if (tMode == null || tMode.getRowCount() == 1)
			return true;
		return false;
	}

	@Override
	public JSONArray getArray(TableMode tableMode) {
		return tableMode.getData();
	}

	@Override
	public <T extends JsonBean> List<T> getList(final TableMode tableMode, Class<T> beanClass) {
		LinkedList<T> jsonArray = new LinkedList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5464528312005612496L;

			@Override
			public String toString() {
				if (tableMode != null) {
					return tableMode.getData().toJSONString();
				}
				return "{}";
			}
		};
		if (tableMode != null) {
			for (int j = 0; j < tableMode.getRowCount(); ++j) {
				jsonArray.add(get(tableMode, j, beanClass));
			}
		}
		return jsonArray;
	}

	@Override
	public <T extends JsonBean> T get(TableMode tm, int j, Class<T> beanClass) {
		if (tm == null)
			return null;
		if (tm.getRowCount() <= 0)
			return null;
		return JsonBean.newBean(beanClass, tm.getRowData(j));
	}

	@Override
	public <T extends JsonBean> T save(T bean) {
		SqlServer server = getSqlServer(bean);
		Insert q = Insert.q(bean);
		boolean bool = server.update(q.toString(), q.getParameterArray());
		if (bool) {
			JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
			if (bean.get(attr.getKEY()) != null)
				return get(bean);
			TableMode tMode = server
					.select(server.getSql("select_last_id").replaceAll("<TABLE>", "`" + attr.getTABLE() + "`"));
			String i = tMode.getValueAt(0, 0);
			if (!Other.isInteger(i) || Integer.parseInt(i) == 0)
				return bean;
			bean.set(attr.getKEY(), Integer.parseInt(i));
			return bean;
		}

		throw new NullPointerException(
				"save is ERROR:" + q.toString() + "\r\n" + "-->" + Arrays.toString(q.getParameterArray()));
	}

	@Override
	public boolean remove(JsonBean bean) {
		Delete delete = JsonBeanSQL.delete(bean);
		return getSqlServer(bean).update(delete.toString(), delete.getParameterArray());
	}

	@Override
	public <T extends JsonBean> T get(T bean) {
		JsonBeanAttr attr = JsonBeanAttr.getAttr(bean);
		Select select = JsonBeanSQL.selectLoader(bean).where(Where.q().and(attr.getKEY(), bean.get(attr.getKEY())));
		return (T) select(bean.getClass(), select);
	}

	@Override
	public <T extends JsonBean> T select(T bean) {
		Select select = JsonBeanSQL.select(bean);
		return (T) select(bean.getClass(), select);
	}

	@Override
	public <T extends JsonBean> T select(Class<T> class1, Select select) {
		TableMode select2 = getSqlServer(class1).select(select.toString(), select.getParameterArray());
		return get(select2, 0, class1);
	}

	@Override
	public <T extends JsonBean> T select(Class<T> class1, JsonBean... bean) {
		Select select = JsonBeanSQL.select(bean);
		return select(class1, select);
	}

	@Override
	public JSONArray selectArray(JsonBean bean) {
		Select select = JsonBeanSQL.select(bean);
		return selectArray(bean.getClass(), select);
	}

	@Override
	public <T extends JsonBean> List<T> selectList(T bean) {
		Select select = JsonBeanSQL.select(bean);
		return (List<T>) selectList(bean.getClass(), select);
	}

	@Override
	public <T extends JsonBean> List<T> selectList(Class<T> class1, JsonBean... bean) {
		Select select = JsonBeanSQL.select(bean);
		return selectList(class1, select);
	}

	@Override
	public <T extends JsonBean> List<T> selectList(Class<T> class1, Select select) {
		TableMode select2 = getSqlServer(class1).select(select.toString(), select.getParameterArray());
		return getList(select2, class1);
	}

	@Override
	public JSONArray selectArray(Class<? extends JsonBean> class1, Select select) {
		TableMode select2 = getSqlServer(class1).select(select.toString(), select.getParameterArray());
		return getArray(select2);
	}

	@Override
	public <T extends JsonBean> boolean updata(T bean) {
		Update update = JsonBeanSQL.update(bean);
		return getSqlServer(bean).update(update.toString(), update.getParameterArray());
	}

	@Override
	public <T extends JsonBean> Integer count(T bean) {
		Select count = JsonBeanSQL.count(bean);
		TableMode select = getSqlServer(bean).select(count.toString(), count.getParameterArray());
		String valueAt = select.getValueAt(0, 0);
		if (valueAt == null) {
			return 0;
		}
		return Integer.parseInt(valueAt);
	}

	@Override
	public TableMode selectSql(String sql, Object... objects) {
		return getSqlServer().select(sql, objects);
	}

	@Override
	public TableMode selectBy(String sql_name, Object... objects) {
		return getSqlServer().selectBy(sql_name, objects);
	}

	@Override
	public boolean updateSql(String sql, Object... objects) {
		return getSqlServer().update(sql, objects);
	}

	@Override
	public boolean updateBy(String sql_name, Object... objects) {
		return getSqlServer().updateBy(sql_name, objects);
	}

	@Override
	public <T extends JsonBean> boolean updataByDifferential(T old, T New) {
		if (old.getClass() != New.getClass()) {
			throw new RuntimeException("错误的参数 " + old.getClass().getName() + " " + New.getClass().getName());
		}
		New = (T) JsonBean.newBean(New.getClass(), New);
		JsonBeanAttr attr = JsonBeanAttr.getAttr(old);
		List<Field> fieldAttr = attr.getFields();
		for (Field field : fieldAttr) {
			if (field == fieldAttr.get(0))
				continue;
			Class<?> class1 = field.getType();

			Object object = old.get(field.getName());
			if (object == null) {
				continue;
			}
			try {
				Object new_Object = field.get(New);
				if (object.equals(new_Object)) {
					field.set(New, null);
				} else if (class1 == int.class || class1 == Integer.class) {
					field.set(New, (int) new_Object - (int) object);
				} else if (class1 == BigDecimal.class) {
					field.set(New, ((BigDecimal) field.get(New)).subtract((BigDecimal) object));
				} else {

				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Update update = Update.increment(New);
		return getSqlServer().update(update.toString(), update.getParameterArray());
	}

	@Override
	public <T extends JsonBean> T selectDESC(T bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean));
		return (T) select(bean.getClass(), where);
	}

	@Override
	public <T extends JsonBean> T selectDESC(Class<T> class1, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean[0]));
		return select(class1, where);
	}

	@Override
	public <T extends JsonBean> JSONArray selectArrayDESC(T bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean));
		return selectArray(bean.getClass(), where);
	}

	@Override
	public <T extends JsonBean> JSONArray selectArrayDESC(Class<T> class1, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean[0]));
		return selectArray(class1, where);
	}

	@Override
	public <T extends JsonBean> JSONArray selectArrayDESC(Class<T> class1, String desc_name, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESC(desc_name));
		return selectArray(class1, where);
	}

	@Override
	public <T extends JsonBean> List<T> selectListDESC(T bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean));
		return (List<T>) selectList(bean.getClass(), where);
	}

	@Override
	public <T extends JsonBean> List<T> selectListDESC(Class<T> class1, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESCKeyId(bean[0]));
		return selectList(class1, where);
	}

	@Override
	public <T extends JsonBean> List<T> selectListDESC(Class<T> class1, Select where) {
		return selectList(class1, where);
	}

	@Override
	public <T extends JsonBean> T selectDESC(T bean, String desc_name) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESC(desc_name));
		return (T) select(bean.getClass(), where);
	}

	@Override
	public <T extends JsonBean> List<T> selectListDESC(T bean, String desc_name) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESC(desc_name));
		return (List<T>) selectList(bean.getClass(), where);
	}

	@Override
	public <T extends JsonBean> List<T> selectListDESC(Class<T> class1, String desc_name, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESC(desc_name));
		return (List<T>) selectList(class1, where);
	}

	@Override
	public <T extends JsonBean> T selectDESC(Class<T> class1, String desc_name, JsonBean... bean) {
		Select where = JsonBeanSQL.select(bean).where(Where.q().orderByDESC(desc_name));
		return select(class1, where);
	}

}
