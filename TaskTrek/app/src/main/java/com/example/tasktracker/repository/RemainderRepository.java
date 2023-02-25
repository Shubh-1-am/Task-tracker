package com.example.tasktracker.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.tasktracker.DAO.RemainderDAO;
import com.example.tasktracker.RemainderDetailsActivity;
import com.example.tasktracker.database.NoteDatabase;
import com.example.tasktracker.database.RemainderDatabase;
import com.example.tasktracker.entities.Remainder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RemainderRepository {

    private final RemainderDAO remainderDAO;
    private LiveData<List<Remainder>> allRemainders;

    public RemainderRepository(Application application) {
        RemainderDatabase remainderDatabase = RemainderDatabase.getInstance(application);
        remainderDAO = remainderDatabase.remainderDAO();
        allRemainders = remainderDAO.getAllRemainders();
    }

    public void insert(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.insert(remainder);
        });
    }

    public void update(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.update(remainder);
        });
    }

    public void delete(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.delete(remainder);
        });
    }

    public void deleteAllRemainders() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(remainderDAO::deleteAllRemainders);
    }

    public void deleteByID(int ID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.deleteByID(ID);
        });
    }

    public int getLastInsertedIRemainderId() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            return remainderDAO.getLastInsertedIRemainderId();
        });
        int lastInsertedID;
        try {
            lastInsertedID = future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d("TAg", "getLastInsertedIRemainderId: " + lastInsertedID);
        return lastInsertedID;
    }

    public LiveData<List<Remainder>> getAllRemainders() {
        return allRemainders;
    }

    public LiveData<List<Remainder>> getAllRemaindersAccordingToSearchQuery(String searchQuery) {
        return remainderDAO.getAllRemaindersAccordingToSearchQuery(searchQuery);
    }

    public LiveData<Remainder> getRemainderByID(int id) {
        return remainderDAO.getRemainderByID(id);
    }

    public RemainderDAO getRemainderDAO() {
        return remainderDAO;
    }
}
