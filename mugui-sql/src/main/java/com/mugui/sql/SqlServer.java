package com.mugui.sql;

import android.database.Cursor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import com.mugui.base.Mugui;

/**
 * 只限于单一事务<br>
 * 事实告诉我们，任何复杂的多重事务都可转换为单一事务<br>
 * {@link threadLocal} 此类应满足同线程，同数据源，同连接<br>
 * 但是同线程，不同数据源，不同连接<br>
 * 且此类在同线程中，应可多次创建，且同数据源，连接不变。
 * 
 * @author 木鬼
 *
 */
public class SqlServer implements Mugui {

	private static final ThreadLocal<HashMap<String, SqlUtils>> threadLocal = new ThreadLocal<>();

	/*
	 * ****************************************分割***********************************
	 * ***********
	 */
	private static int LIMIT_MAX_SIZE = 2000;

	/**
	 * 默认的限制查询数据行数
	 * 
	 * @auther 木鬼
	 * @param limit_max_size
	 */
	public void setLIMIT_MAX_SIZE(int limit_max_size) {
		LIMIT_MAX_SIZE = limit_max_size;
	}

	public SqlServer() {
		dbConf = DBConf.getDefaultDBConf();
	}

	public SqlServer(String url) {
		dbConf = DBConf.getDBConf(url);
	}

	private DBConf dbConf = null;

	private static final ThreadLocal<Boolean> AUTO_COMMIT = new ThreadLocal<>();
	private static final ThreadLocal<Boolean> LOCK_OF_SELECT = new ThreadLocal<>();

	/**
	 * 开启事务
	 * 
	 * @auther 木鬼
	 * @param bool
	 * @throws Exception
	 */
	public void setAutoCommit(boolean bool) throws Exception {
		AUTO_COMMIT.set(bool);
	}
	public boolean isAutoCommit() {
		return AUTO_COMMIT.get()!=null?AUTO_COMMIT.get():false;
	}
	/**
	 * 开启排它锁
	 * 
	 * @auther 木鬼
	 * @param lock_of_update true为开
	 */
	public void setLockOfSelect(boolean lock_of_update) {
		LOCK_OF_SELECT.set(lock_of_update);
	}

	/**
	 * 提交事务<br>
	 * 提交事务将默认触发回收连接操作
	 * 
	 * @throws SQLException
	 * @throws Exception
	 */
	public void commit() throws SQLException, Exception {
		HashMap<String, SqlUtils> hashMap = threadLocal.get();
		if (hashMap == null) {
			return;
		}
		for (SqlUtils sqlUtils : hashMap.values()) {
			try {
				sqlUtils.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reback();
	}

	/**
	 * 连接回收
	 */
	public static void reback() {
		HashMap<String, SqlUtils> hashMap = threadLocal.get();
		if (hashMap == null) {
			return;
		}
		for (SqlUtils sqlUtils : hashMap.values()) {
			try {
				sqlUtils.Close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		hashMap.clear();
		AUTO_COMMIT.set(true);
		LOCK_OF_SELECT.set(false);

	}

	/**
	 * 回滚事务
	 * 
	 * @auther 木鬼
	 * @throws Exception
	 */
	public void rollback() throws Exception {
		try {
			getSqlUtils().rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<SelectListener> listeners = null;

	/**
	 * 查询监听器
	 * 
	 * @author 木鬼
	 *
	 */
	public static interface SelectListener {
		/**
		 * 处理并返回sql语句
		 * 
		 * @auther 木鬼
		 * @param sql
		 * @return
		 */
		String handleSql(String sql);

		/**
		 * 处理并返回查询参数
		 * 
		 * @auther 木鬼
		 * @param objects
		 * @return
		 */
		Object[] handlePar(Object... objects);
	}

	/**
	 * 对SelectListener接口的默认实现
	 * 
	 * @author 木鬼
	 *
	 */
	public static class SelectListenerImpl implements SelectListener {
		@Override
		public String handleSql(String sql) {
			return sql;
		}

		@Override
		public Object[] handlePar(Object... objects) {
			return objects;
		}
	}

	static {
		/*
		 * 限制最大的数据查询行数
		 */
		addSelectListener(new SelectListenerImpl() {
			@Override
			public String handleSql(String sql) {
				if(sql.indexOf("select")<0){
					return sql;
				}
				if (sql.toLowerCase().indexOf("limit") < 0) {
					sql = sql.replaceAll("[;]", " ");
					sql += " limit 0," + LIMIT_MAX_SIZE;
				}
				return sql;
			}
		});
	}

	public TableMode selectBy(String sql_name, Object... params) {
		String sql = dbConf.getSQL(sql_name);
		if (sql == null)
			throw new NullPointerException(sql_name + " SQL is not find of " + dbConf);
		return select(sql, params);
	}

	public static void addSelectListener(SelectListener listener) {
		if (listeners == null) {
			synchronized (SqlServer.class) {
				if (listeners == null)
					listeners = new ArrayList<SelectListener>() {
						/**
							 * 
							 */
						private static final long serialVersionUID = -7234167947506229832L;

						@Override
						public boolean add(SelectListener e) {
							super.add(0, e);
							return true;
						}
					};

			}
		}
		listeners.add(listener);
	}

	public static void removeSelectListener(SelectListener listener) {
		if (listeners == null) {
			synchronized (SqlServer.class) {
				if (listeners == null)
					listeners = new ArrayList<SelectListener>() {
						/**
						 * 
						 */
						private static final long serialVersionUID = -7234167947506229832L;

						@Override
						public boolean add(SelectListener e) {
							super.add(0, e);
							return true;
						}
					};
			}
		}
		listeners.remove(listener);
	}

	public TableMode select(String sql, Object... params) {
		if (listeners != null && !listeners.isEmpty()) {
			for (SelectListener listener : listeners) {
				sql = listener.handleSql(sql);
				params = listener.handlePar(params);
			}
		}
		TableMode tm = null;
		try {
			Cursor set = getSqlUtils().select(sql, params);
			tm = new TableMode(set);
			return tm;
		} catch (Exception e) {
			System.out.println("sql:" + sql + "\r\nparams:" + Arrays.toString(params));
			closeConnection();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement();
		}
	}

	private SqlUtils getSqlUtils() {
		HashMap<String, SqlUtils> hashMap = threadLocal.get();
		if (hashMap == null) {
			threadLocal.set(hashMap = new HashMap<>());
		}
		SqlUtils sqlUtils = hashMap.get(dbConf.getUrl());
		if (sqlUtils == null) {
			sqlUtils = new SqlUtils(dbConf);
			hashMap.put(dbConf.getUrl(), sqlUtils);
		} else {
			while (true) {
				Cursor select =null;
				try {
					select= sqlUtils.select("select 1", null);
					select.close();
					break;
				} catch (Throwable e) {
					if(select!=null){
						select.close();
					}
					sqlUtils.Close();
					sqlUtils = new SqlUtils(dbConf);
					hashMap.put(dbConf.getUrl(), sqlUtils);
				}
			}
		}
		try {
			if (AUTO_COMMIT.get() != null) {
				sqlUtils.setAutoCommit(AUTO_COMMIT.get());
			}
			if (LOCK_OF_SELECT.get() != null) {
				sqlUtils.setLockOfSelect(LOCK_OF_SELECT.get());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sqlUtils;
	}

	public boolean updateBy(String sql_name, Object... params) {
		String sql = dbConf.getSQL(sql_name);
		if (sql == null)
			throw new NullPointerException(sql_name + " SQL is not find of " + dbConf);
		return update(sql, params);
	}

	public String getSql(String sql_name) {
		return dbConf.getSQL(sql_name);
	}

	public boolean update(String sql, Object... params) {
		if (sql == null || sql.isEmpty()) {
			throw new NullPointerException("sql is null");
		}
		try {
			int flag = 0;
			flag = getSqlUtils().update(sql, params);
			return flag > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sql:" + sql + "\r\nparams:" + Arrays.toString(params));
			closeConnection();
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement();
		}
	}

	private void closePreparedStatement() {
//		getSqlUtils().closePreparedStatement();
	}

	private void closeConnection() {
		try {
			rollback();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getSqlUtils().Close();
		}
	}
@Deprecated
	public void batchSQL(String[] sqls) {
//		if (sqls == null || sqls.length == 0) {
//			throw new NullPointerException("sqls is null");
//		}
//		try {
//			getSqlUtils().batch(sqls);
//		} catch (Exception e) {
//			e.printStackTrace();
//			closeConnection();
//			throw new RuntimeException(e);
//		} finally {
//			closePreparedStatement();
//		}
	}

}
