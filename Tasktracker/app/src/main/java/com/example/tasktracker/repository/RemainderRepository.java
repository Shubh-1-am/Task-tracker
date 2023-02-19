package com.example.tasktracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tasktracker.DAO.RemainderDAO;
import com.example.tasktracker.database.NoteDatabase;
import com.example.tasktracker.database.RemainderDatabase;
import com.example.tasktracker.entities.Remainder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemainderRepository {

    private final RemainderDAO remainderDAO;
    private LiveData<List<Remainder>> allRemainders;

    public RemainderRepository(Application application){
        RemainderDatabase remainderDatabase = RemainderDatabase.getInstance(application);
        remainderDAO = remainderDatabase.remainderDAO();
        allRemainders = remainderDAO.getAllRemainders();
    }

    public void insert(Remainder remainder){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.insert(remainder);
        });
    }

    public void update(Remainder remainder){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.update(remainder);
        });
    }

    public void delete(Remainder remainder){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.delete(remainder);
        });
    }

    public void deleteAllRemainders(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(remainderDAO::deleteAllRemainders);
    }

    public void deleteByID(int ID){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.deleteByID(ID);
        });
    }

    public LiveData<List<Remainder>> getAllRemainders() {
        return allRemainders;
    }

    public LiveData<List<Remainder>> getAllRemaindersAccordingToSearchQuery(String searchQuery) {
        return remainderDAO.getAllRemaindersAccordingToSearchQuery(searchQuery);
    }

    public RemainderDAO getRemainderDAO() {
        return remainderDAO;
    }

}
