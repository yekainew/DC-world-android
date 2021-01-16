package com.mugui.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mugui.base.bean.DefaultJsonBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

 class SqlUtils {
	private SQLiteDatabase dataSource;

//	private Connection connection = null;
//	@Getter
//	private PreparedStatement preparedStatement = null;

	public SqlUtils() {
		this.dataSource = DBConf.getDefaultDBConf().getDataSource();
	}

	public SqlUtils(DBConf dbConf) {
		this.dataSource = dbConf.getDataSource();
	}

	private SQLiteDatabase getConnection() throws SQLException {
		return dataSource;
	}

	private boolean lock_of_update = false;

	public void setLockOfSelect(boolean lock_of_update) {
		this.lock_of_update = lock_of_update;
	}

	public Cursor select(String sql, Object[] parvar) throws SQLException {
		if (sql == null)
			throw new NullPointerException("SQL is null");
		if (lock_of_update) {
			sql += " for update";
		}
		String[] strings=handerParameter(parvar);
		return  dataSource.rawQuery(sql, strings);
	}

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String[] handerParameter(Object[] parvar) throws SQLException {
		if (parvar != null && parvar.length > 0) {

			JSONObject object=new JSONObject();
			for (int i = 0; i < parvar.length; ++i) {
				if (parvar[i] == null){
					throw new SQLException("参数：parvar i="+i+" 为空");
				}
				else if (parvar[i].getClass() == BigInteger.class) {
					object.put(i+"",((BigInteger) parvar[i]).longValue());
				}else if (parvar[i].getClass() == BigDecimal.class) {
					object.put(i+"",((BigDecimal) parvar[i]).stripTrailingZeros().toPlainString());
				}else if (parvar[i].getClass() == Date.class) {
					object.put(i+"",format.format((Date) parvar[i]));
				}
				else
					object.put(i+"",parvar[i]);
			}
			String[] str=new String[parvar.length];
			for(int i=0;i<parvar.length;i++){
				str[i]=object.getString(i+"");
			}
			return str;
		}else {
			return null;
		}
	}

	public int update(String sql, Object[] parvar) throws SQLException {
		if (sql == null)
			throw new NullPointerException("SQL is null");
		int k = 1;

//		k = preparedStatement.executeUpdate();
		String[] str=handerParameter(parvar);
		if(str==null){
			dataSource.execSQL(sql);
		}else{
			dataSource.execSQL(sql,str);
		}
		return k;
	}

	public void Close() {
		if (dataSource != null) {
				dataSource.close();
			dataSource = null;
		}
	}

	/**
	 * 不可靠的检测
	 * 
	 * @auther 木鬼
	 * @return
	 * @throws SQLException
	 */
	@Deprecated
	public boolean isClose() throws SQLException {
		return dataSource==null||!dataSource.isOpen();
	}

//	/**
//	 * 批量执行
//	 *
//	 * @auther 木鬼
//	 * @param sqls
//	 * @throws SQLException
//	 */
//	public void batch(String[] sqls) throws SQLException {
//		if (sqls == null)
//			throw new NullPointerException("SQL is null");
//		connection.setAutoCommit(false);
//		Statement statement = connection.createStatement();
//		for (String sql : sqls) {
//			statement.addBatch(sql);
//		}
//		statement.executeBatch();
//		connection.commit();
//		connection.setAutoCommit(true);
//		statement.close();
//	}

	public void rollback() throws SQLException {
		if(!autoCommit){
			dataSource.endTransaction();
		}
	}

	public void commit() throws SQLException {

		if(!autoCommit) {
			dataSource.setTransactionSuccessful();
			dataSource.endTransaction();
		}
	}

	private boolean autoCommit = true;

	public void setAutoCommit(boolean bool) throws SQLException {
		lock_of_update = false;
		autoCommit = bool;
		if (dataSource != null) {
			dataSource.beginTransaction();
		}
	}

	public boolean getAutoCommit() throws SQLException {
		return autoCommit;
	}

//	public void closePreparedStatement() {
//		if (preparedStatement != null) {
//			try {
//				preparedStatement.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			preparedStatement = null;
//		}
//	}

}