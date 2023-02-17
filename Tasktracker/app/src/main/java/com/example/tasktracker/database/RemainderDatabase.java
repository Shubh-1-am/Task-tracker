package com.example.tasktracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tasktracker.DAO.RemainderDAO;
import com.example.tasktracker.entities.Remainder;

@Database(entities = {Remainder.class}, version = 1, exportSchema = false)
public abstract class RemainderDatabase extends RoomDatabase {

    public abstract RemainderDAO remainderDAO();

    public static RemainderDatabase instance;

    public static synchronized RemainderDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RemainderDatabase.class, "remainder_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
