package com.vietdung.note.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vietdung.note.R;
import com.vietdung.note.adapter.NoteAdapter;
import com.vietdung.note.database.DBNote;
import com.vietdung.note.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Note> noteList;
    NoteAdapter noteAdapter;
    RecyclerView rv_Note;
    Toolbar toolbar;
    int numberofColumns = 2;
    DBNote dbNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addEvents();
    }

    private void addEvents() {
        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnAdd:
                Intent intent= new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initView() {
        rv_Note = findViewById(R.id.rvNote);
        toolbar = findViewById(R.id.tbHome);
        dbNote = new DBNote(getApplicationContext());
        noteList = dbNote.noteList();
        noteAdapter = new NoteAdapter(MainActivity.this,noteList);
        noteAdapter.notifyDataSetChanged();
        rv_Note.setAdapter(noteAdapter);
        rv_Note.setLayoutManager(new GridLayoutManager(this, numberofColumns));
    }
}
