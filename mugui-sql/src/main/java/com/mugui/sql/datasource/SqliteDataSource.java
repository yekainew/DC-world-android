package com.mugui.sql.datasource;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.sql.DBConf;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SqliteDataSource implements  DataSource {
    @Override
    public SQLiteDatabase getDataSource() {
        return init();
    }

    @Override
    public SQLiteDatabase getDataSource(DBConf conf) {
        return init();
    }

    @Override
    public SQLiteDatabase getDataSource(String url) {
        return init();
    }

    private static SqliteDB sqliteDB=null;

    private SQLiteDatabase init() {
        if(sqliteDB==null){
            sqliteDB=new SqliteDB(DataSave.app.getApplicationContext());
        }
        SQLiteDatabase writableDatabase = sqliteDB.getWritableDatabase();
        return writableDatabase;
    }
}
