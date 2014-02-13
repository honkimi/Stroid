package com.honkimi.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
    private NoteDao noteDao;

    private LinearLayout container;
    private TextView noteText;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteDao = NoteDao.getInstance(this, new NoteMigration());

        container = (LinearLayout) findViewById(R.id.container);
        noteText = (TextView) findViewById(R.id.noteText);
        editText = (EditText) findViewById(R.id.noteText);

        initNotes();
    }

    private void initNotes() {
        List<NoteDto> noteList = noteDao.findAll();
        for(NoteDto note : noteList) {
            addNoteInView(note);
        }
    }

    /** onClicked Add Note Btn */
    public void onAddClicked(View v) {
        if (noteText.getText().length() == 0 ) {
            return;
        }
        NoteDto note = new NoteDto(noteText.getText().toString());
        insertNote(note);
        addNoteInView(note);
    }

    private void insertNote(NoteDto note) {
        try {
            noteDao.insert(note);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.failed_save), Toast.LENGTH_LONG);
        }
    }

    private void addNoteInView(NoteDto note) {
        TextView text = new TextView(this);
        text.setText(note.getNote());

        container.addView(text);

        // hide Keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        noteText.setText("");
    }

    /**  onClicked Delete Note btn */
    public void onDeleteAllClicked(View v) {
        noteDao.deleteAll();
        container.removeAllViews();
    }
}
