package com.honkimi.app;

import android.database.sqlite.SQLiteDatabase;

import com.honkimi.stroid.StroidMigration;

/**
 * Created by kiminari.homma on 14/02/13.
 */
public class NoteMigration implements StroidMigration {
    public static final String TABLE_NAME = "notes";
    public static final String COL_ID = "id";
    public static final String COL_NOTE = "note";
    public static final String DEF_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String DEF_NOTE = "TEXT NOT NULL";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getOnCreateSql() {
        String str = "CREATE TABLE " + getTableName() + " ( "
                + COL_ID + " " + DEF_ID + ","
                + COL_NOTE + " " + DEF_NOTE + ");";
        return str;
    }

    @Override
    public String getOnUpgradeSql(int oldVersion, int newVersion, SQLiteDatabase db) {
        return "";
    }
}
