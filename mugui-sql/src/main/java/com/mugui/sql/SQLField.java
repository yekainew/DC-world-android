package com.mugui.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * *******字段属性配置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLField {

	/**
	 * 数据类型
	 * 
	 * @auther 木鬼
	 * @return
	 */
	public String DATA_TYPE() default "Integer";

	/**
	 * 是否为主键
	 * 
	 * @return
	 */
	public boolean PRIMARY_KEY() default false;

	public String PRIMARY_KEY_value() default "PRIMARY KEY";

	/**
	 * 是否有默认值
	 * 
	 * @return
	 */
	public boolean DEFAULT() default false;

	public String DEFAULT_value() default "DEFAULT";

	/**
	 * 默认值
	 * 
	 * @return
	 */
	public String DEFAULT_text() default "";

	/**
	 * 是否自动增长，默认可以
	 * 
	 * @return
	 */

	public boolean AUTOINCREMENT() default false;

	public String AUTOINCREMENT_value() default "auto_increment";

	/**
	 * 是否唯一,默认不是
	 * 
	 * @return
	 */
	public boolean UNIQUE() default false;

	public String UNIQUE_value() default "UNIQUE";

	/**
	 * 是否可以是空，默认可以
	 * 
	 * @return
	 */
	public boolean NULL() default true;

	public String NULL_value() default "NOT NULL";
}
