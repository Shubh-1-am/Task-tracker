package com.example.tasktracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tasktracker.entities.Remainder;

import java.util.List;

@Dao
public interface RemainderDAO {

    @Insert
    void insert(Remainder remainder);

    @Update
    void update(Remainder remainder);

    @Delete
    void delete(Remainder remainder);

    @Query("DELETE FROM remainder_table")
    void deleteAllRemainders();

    @Query("DELETE FROM remainder_table WHERE id = :id")
    void deleteByID(int id);

    @Query("SELECT * FROM remainder_table ORDER BY DATE ASC, TIME ASC")
    LiveData<List<Remainder>> getAllRemainders();

    @Query("SELECT * FROM remainder_table WHERE title LIKE :searchQuery OR date LIKE :searchQuery OR time LIKE :searchQuery ORDER BY DATE ASC, TIME ASC")
    LiveData<List<Remainder>> getAllRemaindersAccordingToSearchQuery(String searchQuery);



}
