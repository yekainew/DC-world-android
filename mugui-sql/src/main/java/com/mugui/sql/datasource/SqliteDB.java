package com.mugui.sql.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mugui.base.base.Component;
import com.mugui.base.client.net.classutil.DataSave;

public class SqliteDB extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public SqliteDB(Context context) {
        super( context, "mugui", null, VERSION);
    }

    public SqliteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
