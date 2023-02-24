package com.example.tasktracker.entities;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.tasktracker.BR;

import java.io.Serializable;
import java.util.Objects;

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

    @ColumnInfo(name = "markdown_link_text")
    private String markdownLinkText;

    @ColumnInfo(name = "remainder_id")
    private int remainder_id;


    public Note(int id, String title, String description, boolean isPinned, String lastUpdateDateTime, int noteBackground, String noteImage, String url, String markdownLinkText, int remainder_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isPinned = isPinned;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.noteBackground = noteBackground;
        this.noteImage = noteImage;
        this.url = url;
        this.markdownLinkText = markdownLinkText;
        this.remainder_id = remainder_id;
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
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
        notifyPropertyChanged(BR.pinned);
    }

    @Bindable
    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;

    }

    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
        notifyPropertyChanged(BR.lastUpdateDateTime);
    }

    @Bindable
    public int getNoteBackground() {
        return noteBackground;

    }

    public void setNoteBackground(int noteBackground) {
        this.noteBackground = noteBackground;
        notifyPropertyChanged(BR.noteBackground);
    }

    @Bindable
    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
        notifyPropertyChanged(BR.noteImage);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getMarkdownLinkText() {
        return markdownLinkText;
    }

    public void setMarkdownLinkText(String markdownLinkText) {
        this.markdownLinkText = markdownLinkText;
        notifyPropertyChanged(BR.markdownLinkText);
    }

    @Bindable
    public int getRemainder_id() {
        return remainder_id;
    }

    public void setRemainder_id(int remainder_id) {
        this.remainder_id = remainder_id;
        notifyPropertyChanged(BR.remainder_id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        // check null
        return Objects.equals(id, note.id) &&
                Objects.equals(title, note.title) &&
                Objects.equals(description, note.description) &&
                Objects.equals(isPinned, note.isPinned) &&
                Objects.equals(lastUpdateDateTime, note.lastUpdateDateTime) &&
                Objects.equals(noteBackground, note.noteBackground) &&
                Objects.equals(noteImage, note.noteImage) &&
                Objects.equals(url, note.url) &&
                Objects.equals(markdownLinkText, note.markdownLinkText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isPinned, lastUpdateDateTime, noteBackground, noteImage, url, markdownLinkText,remainder_id);
    }
}
