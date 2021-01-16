package com.mugui.sql;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


import com.alibaba.fastjson.annotation.JSONField;
import com.mugui.base.bean.JsonBean;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.sql.datasource.SqliteDataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 默认的DB配置
 * 
 * @author 木鬼
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class DBConf extends JsonBean {
	// 默认配置文件
	private final static String DEFAULT_CONFIG_URL = "default.sql";

	private static String DATA_SOURCE_CLASS_NAME = SqliteDataSource.class.getName();

	/**
	 * 设置数据库数据源类<br>
	 * 用于自定义数据源处理类<br>
	 * @author 木鬼
	 * @param data_source_class_name
	 */
	public final static void setDATA_SOURCE_CLASS_NAME(String data_source_class_name) {
		DATA_SOURCE_CLASS_NAME = data_source_class_name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2184676836370105530L;
	private static DBConf instance = null;

	public static final DBConf getDefaultDBConf() {
		if (instance == null) {
			synchronized (DBConf.class) {
				if (instance != null)
					return instance;
				instance = new DBConf();
			}
		}
		return instance;
	}

	/**
	 * 通过url得到数据库配置文件
	 * 
	 * @auther 木鬼
	 * @param url
	 * @return
	 */
	public static DBConf getDBConf(String url) {
		DBConf dbConf = DB_CONF_MAP.get(url);
		return dbConf;
	}

	private static final ConcurrentHashMap<String, DBConf> DB_CONF_MAP = new ConcurrentHashMap<>();

	/**
	 * 驱动名
	 */
	private String drive;
	/**
	 * 基本连接
	 */
	private String url;

	/**
	 * 用户
	 */
	private String user;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 最大的连接池数量
	 */
	private int maxPoolSize = 200;

	/**
	 * 最小空闲的连接数
	 */
	private int minimumldle = 10;
	@JSONField(serialize = false)
	private transient SQLiteDatabase dataSource;

	public SQLiteDatabase getDataSource() {
		if (dataSource == null) {
			synchronized (DBConf.class) {
				if (dataSource == null) {
					Class<?> forName;
					try {
						forName = Class.forName(DATA_SOURCE_CLASS_NAME);
						com.mugui.sql.datasource.DataSource newInstance = (com.mugui.sql.datasource.DataSource) forName
								.newInstance();
						dataSource = newInstance.getDataSource(this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return dataSource;
	}

	/**
	 * 一个默认的conf ,会从默认文件中读取默认的配置
	 */
	public DBConf() {
		this(new File(DEFAULT_CONFIG_URL));
		// 从springboot 配置文件中读取
		try {
			readConf(DBConf.class.getResourceAsStream("default.sql"));
		} catch (IOException e) {
			try {
				readConf(DBConf.class.getResourceAsStream("/default.sql"));
			} catch (IOException e1) {
			}
		}
	}



	/**
	 * 从文件夹中载入一个sql配置
	 * 
	 * @param file
	 */
	public DBConf(File file) {
		readConf(file);
	}

	/**
	 * 从一切io流中读入sql配置
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public DBConf(InputStream stream) throws IOException {
		readConf(stream);
	}

	/**
	 * @param drive 驱动名
	 * @param url   数据库连接
	 * @param user  用户
	 * @param pwd   密码
	 */
	public DBConf(String drive, String url, String user, String pwd) {
		readConf(drive, url, user, pwd);
	}

	/**
	 * 有时候我们希望直接通过一个连接建立数据库配置<br>
	 * 未实现
	 * 
	 * @param connection_url 数据库连接
	 */
	@Deprecated
	public DBConf(String connection_url) {
		readConf(connection_url);
	}

	/**
	 * 从文件夹中载入一个sql配置
	 * 
	 * @param file
	 */
	public void readConf(File file) {
		if (file == null)
			return;
		InputStream inputStream;
		try {// 从文件中加载
			inputStream = new FileInputStream(file);
			readConf(inputStream);
			inputStream.close();
		} catch (Exception e) {
		}
		try {// 从jar包中加载
			 inputStream = DataSave.app.getResources().getAssets().open(file.getPath());
			//inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file.getPath());
			readConf(inputStream);
			inputStream.close();
		} catch (Exception e1) {
		}
	}

	/**
	 * 得到一个sql语句
	 * 
	 * @return
	 * 
	 * @auther 木鬼
	 */
	public String getSQL(String sql_name) {
		String sql =null;
		if (map == null || (sql = map.get(sql_name)) == null || sql.equals("")) {
			throw new RuntimeException("不存在的sql语句");
		}
		return sql;
	}

	private HashMap<String, String> map = null;

	/**
	 * 从一切io流中读入sql配置<br>
	 * 流必须是文本格式，且符合默认格式规范
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public void readConf(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new IOException("io流错误");
		}
		if (map == null) {
			map = new HashMap<>();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		String key = "";
		String value = "";
		String s;
		while ((s = br.readLine()) != null) {
			if (s.trim().isEmpty())
				continue;
			if (s.charAt(0) == '#' || s.trim().isEmpty()) {
				if (!value.isEmpty() && !key.trim().isEmpty()) {
					map.put(key.trim(), value.trim());
				}
				continue;
			}
			int i = s.indexOf(":");
			if (i == -1) {
				if (key.equals(""))
					throw new IOException("文件解析错误");
				else {
					value += " " + s + " ";
				}
			} else {
				if (!value.equals("")) {
					map.put(key.trim(), value.trim());
					key = "";
					value = "";
				}
				key += s.substring(0, i);
				value += " " + s.substring(i + 1, s.length()) + " ";
			}
		}
		if (!value.equals("")) {
			map.put(key.trim(), value.trim());
		}
		initBaseConf();
	}

	/**
	 * 写入一个数据库配置
	 * 
	 * @auther 木鬼
	 * @param string
	 * @param sql
	 */
	public void write(String string, String sql) {
		if (map == null) {
			map = new HashMap<>();
		}
		map.put(string, sql);
	}

	private void initBaseConf() {
		String url = (String) map.get("url");
		String pwd = (String) map.get("password");
		String drive = (String) map.get("driver-class-name");
		String user = (String) map.get("username");
		if (url != null) {
			this.url = url;
			DB_CONF_MAP.put(url, this);
		}
		if (pwd != null) {
			this.pwd = pwd;
		}
		if (drive != null) {
			this.drive = drive;
		}
		if (user != null) {
			this.user = user;
		}
	}

	/**
	 * 
	 * @param drive 驱动class
	 * @param url   数据库连接
	 * @param user  用户
	 * @param pwd   密码
	 */
	public void readConf(String drive, String url, String user, String pwd) {
		try {
			readConf(DBConf.class.getResourceAsStream("default.sql"));
		} catch (IOException e) {
			try {
				readConf(DBConf.class.getResourceAsStream("/default.sql"));
			} catch (IOException e1) {
			}
		}
		this.drive = drive;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		DB_CONF_MAP.put(url, this);
	}

	/**
	 * 有时候我们希望直接通过一个连接建立数据库配置 <br>
	 * 未实现
	 * 
	 * @param connection_url 数据库连接
	 */
	@Deprecated
	public void readConf(String connection_url) {

	}

	/**
	 * 有时候希望复制一份当前配置 <br>
	 * 建议直接调用{@link JsonBean #newBean(JsonBean)}}方法 ，因为它实在是太方便了
	 * 
	 * @return
	 */
	@Deprecated
	public DBConf copy() {
		return DBConf.newBean(this);
	}

}
