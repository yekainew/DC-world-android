package com.mugui.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库注解
 * 
 * @author 木鬼
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SQLDB {


	/**
	 * 表名
	 * 
	 * @auther 木鬼
	 * @return
	 */
	String TABLE() ;
	

	/**
	 * 主键
	 * 
	 * @auther 木鬼
	 * @return
	 */
	String KEY();

	/*
	 * ****************************数据库连接配置
	 */
	String url() default "";

	String user() default "";

	String pwd() default "";



}
