package com.example.notes;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Note implements Comparable<Note>{
    private String title;
    private Date date;
    private String text;

    public Note(String title, Date date, String text) {
        this.title = title;
        this.text = text;
        this.date = date;

    }


    public String getTitle() {
        return title;
    }


    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public int compareTo(Note note) {
        return note.getDate().compareTo(getDate());
    }
}