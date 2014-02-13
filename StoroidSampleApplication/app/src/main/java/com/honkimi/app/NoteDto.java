package com.honkimi.app;

import android.content.ContentValues;
import android.database.Cursor;

import com.honkimi.stroid.Contentable;

/**
 * Created by kiminari.homma on 14/02/13.
 */
public class NoteDto implements Contentable {
    private Integer id;
    private String note;

    public NoteDto(){
    }

    public NoteDto(String note){
        this.note = note;
    }

    public NoteDto (Cursor c) {
        this.id = c.getInt(c.getColumnIndex(NoteMigration.COL_ID));
        this.note = c.getString(c.getColumnIndex(NoteMigration.COL_NOTE));
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

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NoteMigration.COL_ID, getId());
        values.put(NoteMigration.COL_NOTE, getNote());
        return values;
    }
}
