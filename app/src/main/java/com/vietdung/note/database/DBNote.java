package com.vietdung.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vietdung.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBNote extends SQLiteOpenHelper {
    private Context context;

    public static final String DATABASE_NAME = "DataNote";
    public static final String TB_NAME = "Note";
    public static final String TB_ID = "NoteId";
    public static final String TB_TITLE = "NoteTitle";
    public static final String TB_NOTE = "NoteSub";
    public static final String TB_DATE_CREATE = "DateCreate";
    public static final String TB_DATE = "Date";
    public static final String TB_TIME = "Time";
    public static final String TB_IMAGE_URI = "URI";

    public DBNote(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tbNote = "CREATE TABLE " + TB_NAME + " (" + TB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_TITLE + " TEXT, "
                + TB_NOTE + " TEXT, "
                + TB_DATE_CREATE + " TEXT, "
                + TB_DATE + " TEXT, "
                + TB_TIME + " TEXT, "
                + TB_IMAGE_URI + " TEXT )";

        sqLiteDatabase.execSQL(tbNote);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_TITLE, note.getTitle());
        values.put(TB_NOTE, note.getNote());
        values.put(TB_DATE_CREATE, note.getDateCreate());
        values.put(TB_DATE, note.getDate());
        values.put(TB_TIME, note.getTime());
        values.put(TB_IMAGE_URI, note.getUri());
        db.insert(TB_NAME, null, values);
        db.close();
    }

    public void delelteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TB_NAME, TB_ID + " = "+id,null);
        db.close();
    }

    public List<Note> noteList() {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        String selectQuery = " Select * from " + TB_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(TB_ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(TB_TITLE)));
            note.setNote(cursor.getString(cursor.getColumnIndex(TB_NOTE)));
            note.setDateCreate(cursor.getString(cursor.getColumnIndex(TB_DATE_CREATE)));
            note.setDate(cursor.getString(cursor.getColumnIndex(TB_DATE)));
            note.setTime(cursor.getString(cursor.getColumnIndex(TB_TIME)));
            note.setUri(cursor.getString(cursor.getColumnIndex(TB_IMAGE_URI)));

            noteList.add(note);
            cursor.moveToNext();
        }
        return noteList;

    }

    public Note getNoteFromId(int Id) {
        SQLiteDatabase db = getWritableDatabase();
        Note note = new Note();
        String query = "SELECT * FROM " + DBNote.TB_NAME + " WHERE " + TB_ID + " = " + Id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            note.setDateCreate(cursor.getString(cursor.getColumnIndex(TB_DATE_CREATE)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(TB_TITLE)));
            note.setNote(cursor.getString(cursor.getColumnIndex(TB_NOTE)));
            note.setDate(cursor.getString(cursor.getColumnIndex(TB_DATE)));
            note.setTime(cursor.getString(cursor.getColumnIndex(TB_TIME)));
            note.setUri(cursor.getString(cursor.getColumnIndex(TB_IMAGE_URI)));
            cursor.moveToNext();
        }
        return note;

    }

    public int update(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB_TITLE, note.getTitle());
        values.put(TB_NOTE, note.getNote());
        values.put(TB_DATE_CREATE, note.getDateCreate());
        values.put(TB_DATE, note.getDate());
        values.put(TB_TIME, note.getTime());
        values.put(TB_IMAGE_URI, note.getUri());
        return db.update(TB_NAME, values, TB_ID + " = " + note.getId(), null);
    }

    public Note getNoteFromDateCreat() {
        SQLiteDatabase db = getWritableDatabase();
        Note note = new Note();
        String selectQuery = " Select * from " + TB_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        note.setId(cursor.getInt(cursor.getColumnIndex(TB_ID)));
        note.setDateCreate(cursor.getString(cursor.getColumnIndex(TB_DATE_CREATE)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(TB_TITLE)));
        note.setNote(cursor.getString(cursor.getColumnIndex(TB_NOTE)));
        note.setDate(cursor.getString(cursor.getColumnIndex(TB_DATE)));
        note.setTime(cursor.getString(cursor.getColumnIndex(TB_TIME)));
        note.setUri(cursor.getString(cursor.getColumnIndex(TB_IMAGE_URI)));
        return note;

    }

}
