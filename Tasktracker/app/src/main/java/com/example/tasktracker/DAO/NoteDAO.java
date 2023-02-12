package com.example.tasktracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tasktracker.entities.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    @Query("SELECT * FROM notes_table ORDER BY is_pinned DESC, last_update_date_time DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes_table WHERE is_pinned = 1 ORDER BY last_update_date_time DESC")
    LiveData<List<Note>> getAllPinnedNotes();

    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery OR last_update_date_time LIKE :searchQuery ORDER BY last_update_date_time DESC")
    LiveData<List<Note>> getAllNotesAccordingToSearchQuery(String searchQuery);
}
