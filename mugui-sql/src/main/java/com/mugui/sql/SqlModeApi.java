package com.mugui.sql;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.mugui.base.Mugui;
import com.mugui.base.bean.JsonBean;
import com.mugui.sql.loader.Select;
import com.mugui.sql.loader.Where;

public interface SqlModeApi extends Mugui {
	/**
	 * 清空表数据
	 * 
	 * @auther 木鬼
	 * @param TABLE
	 * @return
	 */
	@Deprecated
	boolean clearTableData(Class<? extends JsonBean> class1);

	/**
	 * 创建表
	 * 
	 * @auther 木鬼
	 * @param class1
	 */
	void createTable(Class<? extends JsonBean> class1);

	/**
	 * 表是否存在
	 * 
	 * @auther 木鬼
	 * @param TABLE
	 * @return
	 */
	boolean isTable(String TABLE);

	/**
	 * 将数据集装换为对应的JSONArray
	 * 
	 * @param <T>
	 * @param tableMode
	 * @param beanClass
	 * @return
	 */
	JSONArray getArray(TableMode tableMode);

	/**
	 * 将数据集装换为对应的List
	 * 
	 * @param <T>
	 * @param tableMode
	 * @param beanClass
	 * @return
	 */
	<T extends JsonBean> List<T> getList(TableMode tableMode, Class<T> beanClass);

	/**
	 * 将数据集中的某行数据装换为对应的jsonbean
	 * 
	 * @param <T>
	 * @param tm
	 * @param j
	 * @param beanClass
	 * @return
	 */
	<T extends JsonBean> T get(TableMode tm, int j, Class<T> beanClass);

	/**
	 * 保存
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T save(T bean);

	/**
	 * 删除
	 * 
	 * @auther 木鬼
	 * @param bean
	 * @return
	 */
	boolean remove(JsonBean bean);

	/**
	 * 得到这个数据，直接通过主键拿取
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T get(T bean);

	/**
	 * 简单查询
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param beans
	 * @return
	 */
	<T extends JsonBean> T select(T beans);

	<T extends JsonBean> T select(Class<T> class1, JsonBean... bean);

	/**
	 * 查询
	 * 
	 * @auther 木鬼
	 * @param bean
	 * @return
	 */
	JSONArray selectArray(JsonBean bean);

	/**
	 * 查询
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectList(T bean);

	/**
	 * 更新
	 * 
	 * @auther 木鬼
	 * @param <T>
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> boolean updata(T bean);

	/**
	 * 根据条件简单统计
	 * 
	 * @auther 木鬼
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> Integer count(T bean);

	/**
	 * 直接使用sql语句查询
	 * 
	 * @auther 木鬼
	 * @param sql
	 * @param objects
	 * @return
	 */
	TableMode selectSql(String sql, Object... objects);

	/**
	 * 直接使用sql语句名称查询
	 * 
	 * @auther 木鬼
	 * @param sql_name sql名称
	 * @param objects  参数
	 * @return
	 */
	TableMode selectBy(String sql_name, Object... objects);

	/**
	 * 直接使用sql语句更新
	 * 
	 * @auther 木鬼
	 * @param sql
	 * @param objects 参数
	 * @return
	 */
	boolean updateSql(String sql, Object... objects);

	/**
	 * 直接使用sql语句名称更新
	 * 
	 * @param sql_name
	 * @param objects  参数
	 * @return
	 */
	boolean updateBy(String sql_name, Object... objects);

	/**
	 * 比较2个对象之间的差异更新<br>
	 * 暂时只支持{@link Integer},{@link BigDecimal}<br>
	 * 以参数New为更新后的数据
	 * 
	 * @param <T>
	 * @param old 老的
	 * @param New 新的
	 * @return
	 */
	<T extends JsonBean> boolean updataByDifferential(T old, T New);

	/**
	 * 得到最后一个满足条件的数据
	 * 
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T selectDESC(T bean);

	/**
	 * 得到最后一个满足条件的数据
	 * 
	 * @param bean
	 * @param desc_name 倒序条件
	 * @return
	 */
	<T extends JsonBean> T selectDESC(T bean, String desc_name);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> JSONArray selectArrayDESC(T bean);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectListDESC(T bean);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param bean
	 * @param desc_name 倒序条件
	 * @return
	 */
	<T extends JsonBean> List<T> selectListDESC(T bean, String desc_name);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param class1 指定的jsonbean类型
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectListDESC(Class<T> class1, JsonBean... bean);

	/**
	 * 查询出来的倒序取出
	 *
	 * @param class1 返回的对象类型
	 * @param where
	 * @param <T>
	 * @return
	 */
	<T extends JsonBean> List<T> selectListDESC(Class<T> class1, Select where);
	/**
	 * 查询出来的倒序取出
	 * 
	 * @param class1    指定的jsonbean类型
	 * @param desc_name 倒序条件
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectListDESC(Class<T> class1, String desc_name, JsonBean... bean);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param class1
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> JSONArray selectArrayDESC(Class<T> class1, JsonBean... bean);

	/**
	 * 查询出来的倒序取出
	 * 
	 * @param class1
	 * @param desc_name 倒序条件
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> JSONArray selectArrayDESC(Class<T> class1, String desc_name, JsonBean... bean);

	/**
	 * 得到最后一个满足条件的数据
	 * 
	 * @param class1
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T selectDESC(Class<T> class1, JsonBean... bean);

	/**
	 * 得到最后一个满足条件的数据
	 * 
	 * @param class1
	 * @param desc_name 倒序条件
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T selectDESC(Class<T> class1, String desc_name, JsonBean... bean);

	/**
	 * 查询
	 * 
	 * @param <T>
	 * @param class1
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectList(Class<T> class1, JsonBean... bean);

	/**
	 * 根据Select对象进行查询
	 * 
	 * @param <T>
	 * @param class1 用于接收的jsonbean
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> T select(Class<T> class1, Select select);

	/**
	 * 根据Select对象进行查询
	 * 
	 * @param <T>
	 * @param class1 用于接收的jsonbean
	 * @param bean
	 * @return
	 */
	<T extends JsonBean> List<T> selectList(Class<T> class1, Select where);

	/**
	 * 根据Select对象进行查询
	 * 
	 * @param <T>
	 * @param class1 用于接收的jsonbean
	 * @param bean
	 * @return
	 */
	JSONArray selectArray(Class<? extends JsonBean> class1, Select where);

}
