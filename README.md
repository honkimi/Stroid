Stroid
======

Storage Android. Simple SQLite3 Mapper.

There are many Android SQLite3 OR Mapping libraries. 
They are too much functions and complicated. 
This library's theme is "Simple".

You can easily create SQLite3 table, CURDs and update scheme.

Installation
============
This library is for Android Studio Project.

1, Clone this project.

2, Create "lib" directory in your Android project. If your project name is "SampleApp", create "SampleApp/lib".

3, Copy this project to the lib directory. "SampleApp/lib/Stroid".

4, Edit "settings.gradle" to include the library. For example, `include ':app', ':lib:Stroid'`

5, Open "Project Structure", open your app's dependency and add(+) ":lib:Stroid".


Usage
=====
You need to prepare 3 files.

* Migration class
* Dto class
* Dao class

Migration class
---------------
This class is the definition of the table and upgrade sql.

    public class NoteMigration implements StroidMigration {
        public static final String TABLE_NAME = "notes";
        public static final String COL_ID = "id";
        public static final String COL_NOTE = "note";
        public static final String COL_LASTUPDATE = "lastupdate";
        public static final String DEF_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
        public static final String DEF_NOTE = "TEXT NOT NULL";
        public static final String DEF_LASTUPDATE = "TEXT NOT NULL";

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }

        @Override
        public String getOnCreateSql() {
            String str = "CREATE TABLE " + getTableName() + " ( "
                    + COL_ID + " " + DEF_ID + ","
                    + COL_NOTE + " " + DEF_NOTE + ","
                    + COL_LASTUPDATE + " " + DEF_LASTUPDATE + ");";
            return str;
        }

        @Override
        public String getOnUpgradeSql(int oldVersion, int newVersion, SQLiteDatabase db) {
            return "";
        }
    }

Yeah, I know String+ is not good way for the performance and memory issue. But it's readable and I have never been in trouble of this way.

Then, you need to edit your Application class. Set DB version and DB name. If you plus the DB version, each Migrations's onUpgrade() will be called.

    public class StroidSampleApplication extends Application {

        StroidMigrationManager.StroidConf dbConf = new StroidMigrationManager.StroidConf() {
            @Override
            public String getDBName() {
                return this.getClass().getSimpleName();
            }

            @Override
            public int getDBVersion() {
                return 1;
            }
        };

        @Override
        public void onCreate() {
            StroidMigrationManager mng = StroidMigrationManager.getInstance();
            mng.setConf(dbConf);

            mng.add(new NoteMigration());
        }
    }

Make sure that you edit application attribute in AndroidManifest.
    <application
        android:name="StroidSampleApplication"
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name">
    ...
    </application>

Regarding `getOnUpgradeSql`, you can write upgrade sql sentences. There are some cases:

* want to create new table when upgrading.
* want to add column when upgrading.

### Create table when upgrading

    @Override
    public String getOnUpgradeSql(int oldVersion, int newVersion, SQLiteDatabase db) {
        return "CREATE TABLE if not exists " + getTableCreateSql();
    }

### Add column when upgrading
In this case, we need to a little bit hack.

    try {
        String str = "ALTER TABLE " + getTableName() + " ADD COLUMN "
                + COL_FAVORITE_FLG + " " + DEF_FAVORITE_FLG + " DEFAULT 0";
        db.execSQL(str);
    } catch (SQLiteException e) {
    }
    return "";

Dto class
---------
After the table definition, create dto object to fetch data. In order to combine Dao class, implement "Contentable". 
It requires `toContentValues()` returns CotnentValue.

    public class NoteDto implements Contentable {
        private Integer id;
        private String note;
        private String lastupdate;

        public NoteDto(){
        }

        public NoteDto(int id, String note, String lastupdate){
            this.note = note;
            this.id = id;
            this.lastupdate = lastupdate;
        }

        public NoteDto (Cursor c) {
            this.id = c.getInt(c.getColumnIndex(NoteMigration.COL_ID));
            this.note = c.getString(c.getColumnIndex(NoteMigration.COL_NOTE));
            this.lastupdate = c.getString(c.getColumnIndex(NoteMigration.COL_LASTUPDATE));
        }

        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getNote() {
            return note;
        }
        public void setNote(String note) {
            this.note = note;
        }
        public String getLastupdate() {
            return lastupdate;
        }
        public void setLastupdate(String lastupdate) {
            this.lastupdate = lastupdate;
        }
        public ContentValues toContentValues() {
            ContentValues values = new ContentValues();
            values.put(NoteMigration.COL_ID, getId());
            values.put(NoteMigration.COL_NOTE, getNote());
            values.put(NoteMigration.COL_LASTUPDATE, getLastupdate());
            return values;
        }
    }

Dao class
---------
Lastly, let's implement Dao class.

    public class NoteDao extends StroidDao {
        private static NoteDao dao;

        protected NoteDao(Context context, StroidMigration mgr) {
            super(context, mgr);
        }

        public static NoteDao getInstance(Context context, StroidMigration mgr) {
            if (dao == null) {
                dao = new NoteDao(context, mgr);
            }
            return dao;
        }

        public List<NoteDto> findAll() {
            List<NoteDto> postList = new ArrayList<NoteDto>();
            open();
            Cursor c = getCursorFindAll();
            if (c.moveToFirst()) {
                do {
                    NoteDto note = new NoteDto(c);
                    postList.add(note);
                } while (c.moveToNext());
            }
            return postList;
        }

        public NoteDto findById(int id) {
            NoteDto note = null;
            open();
            Cursor c = getCursorFindByCondition("id = " + id);
            if (c.moveToFirst()) {
                note = new NoteDto(c);
            }
            return note;
        }
    }

By default, deleteAll(), delete(id), update() and insert() are implemented in StroidDao.
But you need to implement find method due to Cursor issue.


Sample Activity
---------------

In your Activity class, define Dao in the field.

    private NoteDao noteDao;
    
In `onCreate()`, init Dao class and then you can use every dao methods.
    noteDao = NoteDao.getInstance(this, new NoteMigration());
    // find
    noteDao.findAll();
    // delete
    noteDao.deleteAll();
    // insert
    NoteDto note = new NoteDto("test");
    try {
        noteDao.insert(note);
    } catch (Exception e) {
        Toast.makeText(this, getString(R.string.failed_save), Toast.LENGTH_LONG);
    }
    

For more details, See Sample Application.


