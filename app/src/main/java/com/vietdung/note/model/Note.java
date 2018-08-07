package com.vietdung.note.model;

import java.util.List;

public class Note {
    private int id;
    private String title;
    private String note;
    private String date;
    private String time;
    private String dateCreate;
    private String uri;



    public Note(int id, String title, String note, String dateCreate, String date,String time,String urlImage) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.dateCreate =dateCreate;
        this.date = date;
        this.time = time;
        this.uri = urlImage;

    }

    public Note() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
