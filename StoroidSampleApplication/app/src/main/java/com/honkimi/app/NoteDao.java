package com.honkimi.app;

import android.content.Context;
import android.database.Cursor;

import com.honkimi.stroid.StroidDao;
import com.honkimi.stroid.StroidMigration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiminari.homma on 14/02/13.
 */
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
