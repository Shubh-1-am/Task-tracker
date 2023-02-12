package com.example.tasktracker.entities;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes_table")
public class Note extends BaseObservable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_pinned")
    private boolean isPinned = false;

    @ColumnInfo(name = "last_update_date_time")
    private String lastUpdateDateTime ;

    @ColumnInfo(name = "note_background")
    private int noteBackground;

    @ColumnInfo(name = "note_image")
    private String noteImage;

    @ColumnInfo(name = "url")
    private String url;

    public Note(int id, String title, String description, boolean isPinned, String lastUpdateDateTime, int noteBackground, String noteImage, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isPinned = isPinned;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.noteBackground = noteBackground;
        this.noteImage = noteImage;
        this.url = url;
    }

    @Ignore
    public Note() {
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
//        notifyPropertyChanged(com.example.tasktracker.BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    @Bindable
    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    @Bindable
    public int getNoteBackground() {
        return noteBackground;
    }

    public void setNoteBackground(int noteBackground) {
        noteBackground = noteBackground;
    }

    @Bindable
    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
