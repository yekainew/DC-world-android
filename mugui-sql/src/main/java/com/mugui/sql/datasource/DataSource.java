package com.mugui.sql.datasource;

import android.database.sqlite.SQLiteDatabase;

import com.mugui.base.Mugui;
import com.mugui.sql.DBConf;

public interface DataSource extends Mugui {
	SQLiteDatabase getDataSource();

	SQLiteDatabase getDataSource(DBConf conf);

	SQLiteDatabase getDataSource(String url);
}
