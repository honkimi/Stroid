package com.honkimi.stroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


/**
 * Core Data Access Class. When you add dao functions, you should create Dao class extended from this class and Dto class.
 *
 * @author kiminari.homma
 *
 */
public class StroidDao {

    /** Singleton */
    private static StroidDao dao;
    /** App Context */
    private Context mContext;
    /** SQLite access Object */
    private SQLiteDatabase mDb = null;

    /** DB Helper */
    private static DatabaseHelper dbHelper;

    /** Table Name */
    private String mTableName;

    /**
     * not called from external.
     */
    protected StroidDao(Context context, StroidMigration mgr) {
        this.mContext = context;
        dbHelper = DatabaseHelper.getInstance(this.mContext);
        mTableName = mgr.getTableName();
    }

    /**
     * Get Instance. This returns single object
     *
     * @param context
     *            App Context
     * @param mgr
     *
     * @return Dao Object
     */
    public static StroidDao getInstance(Context context, StroidMigration mgr) {
        if (dao == null) {
            dao = new StroidDao(context, mgr);
        }
        return dao;
    }

    /**
     * for db.query
     *
     * @return db
     */
    public SQLiteDatabase getDb() {
        return mDb;
    }

    /**
     * Inner class for data access. Android DB Implementation way.
     *
     * @author kiminari.homma
     *
     */
    private static final class DatabaseHelper extends SQLiteOpenHelper {
        /** Singleton Object */
        private static DatabaseHelper self = null;
        /** StroidMigrationManager instance */
        private static StroidMigrationManager manager;

        /**
         * Initialize DB from settings
         *
         * @param context
         *            App Context
         */
        private DatabaseHelper(Context context) {
            super(context, manager.getConf().getDBName(), null, manager.getConf().getDBVersion());
        }

        /**
         * Get instance of Singleton Method
         * @param context Context
         * @return DatabaseHelper
         */
        public static DatabaseHelper getInstance(Context context) {
            if (self == null) {
                manager = StroidMigrationManager.getInstance();
                self = new DatabaseHelper(context);
            }
            return self;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ArrayList<StroidMigration> mgrs = (ArrayList<StroidMigration>) manager.getList();
            for (StroidMigration mgr : mgrs) {
                db.execSQL(mgr.getOnCreateSql());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ArrayList<StroidMigration> mgrs = (ArrayList<StroidMigration>) manager.getList();
            for (StroidMigration mgr : mgrs) {
                if(mgr.getOnUpgradeSql(oldVersion, newVersion, db).isEmpty()){
                    continue;
                }
                db.execSQL(mgr.getOnUpgradeSql(oldVersion, newVersion, db));
            }
        }
    }

    /** DB Connection Open */
    public void open() {
        if (mDb == null) {
            mDb = dbHelper.getWritableDatabase();
        }
    }


    /**
     * Return record
     *
     * @return long count Data Count
     */
    public long getSize() {
        open();
        String sql = "select count(*) from " + mTableName;
        Cursor c = mDb.rawQuery(sql, null);
        c.moveToLast();
        long count = c.getLong(0);
        c.close();
        return count;
    }

    /**
     * Delete All tables.
     *
     * @return boolean
     */
    public int deleteAll() {
        open();
        int ret = mDb.delete(mTableName, null, null);
        return ret;
    }

    /**
     * @param id
     *            delete by id
     * @return true if success
     */
    public int delete(int id) {
        open();
        int ret = mDb.delete(mTableName, "_id =" + id, null);
        return ret;
    }

    /**
     * ！！！ REQUIES open() AND close()!
     * Use this in each Dao. Get cursor object from id.
     *
     * @param id
     *            id value to fetch
     * @return Cursor object
     */
    protected Cursor getCursorFind(int id) {
        String sql = "select * from " + mTableName + " where _id = " + id + ";";
        Cursor c = mDb.rawQuery(sql, null);
        return c;
    }

    /**
     * ！！！ REQUIES open() AND close()!
     * Use this in each Dao. Get cursor object from condition.
     *
     * @param cond
     *           Search condition string
     * @return Cursor object
     */
    protected Cursor getCursorFindByCondition(String cond) {
        String sql = "select * from " + mTableName + " where " + cond + ";";
        Cursor c = mDb.rawQuery(sql, null);
        return c;
    }

    /**
     * ！！！ REQUIES open() AND close()!
     * Use this in each Dao. Get cursor object.
     *
     * @return Cursor object
     */
    protected Cursor getCursorFindAll() {
        String sql = "select * from " + mTableName + ";";
        Cursor c = mDb.rawQuery(sql, null);
        return c;
    }

    /**
     * Insert values of ContentValue
     *
     * @param content  Contantable object
     * @return the inserted id
     * @throws Exception  Failed to save sqlite3
     */
    public long insert(Contentable content) throws Exception {
        open();
        ContentValues values = content.toContentValues();
        long ret = mDb.insertOrThrow(mTableName, null, values);
        return ret;
    }

    /**
     * update column specified by where.
     *
     * @param content  Contentable object
     * @param where  Where sentence with ?
     * @param whereArgs str array to replace ?
     * @return the updated columns number
     */
    public int update(Contentable content, String where, String[] whereArgs) {
        open();
        ContentValues values = content.toContentValues();
        int ret = mDb.update(mTableName, values, where, whereArgs);
        return ret;
    }

    /**
     * get target table name. use this in your subclass for your sql.
     *
     * @return table name
     */
    public String getTableName() {
        return mTableName;
    }
}
