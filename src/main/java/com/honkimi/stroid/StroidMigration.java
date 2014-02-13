package com.honkimi.stroid;

import android.database.sqlite.SQLiteDatabase;

/**
 * Migration Interface.
 * Define your table definitions.
 * @author kiminari.homma
 *
 */
public interface StroidMigration {
    /**
     * return table name
     * @return table name
     */
    String getTableName();
    /**
     * return table create sql
     * @return sql
     */
    String getOnCreateSql();
    /**
     * return table update saql
     * @param oldVersion old version
     * @param newVersion new version
     * @param db SQLiteDatabase
     * @return sql
     */
    String getOnUpgradeSql(int oldVersion, int newVersion, SQLiteDatabase db);
}
