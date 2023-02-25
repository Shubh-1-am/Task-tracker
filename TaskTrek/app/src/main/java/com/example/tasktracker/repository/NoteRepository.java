package com.example.tasktracker.repository;

import android.app.Application;
import android.view.animation.Transformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.tasktracker.DAO.NoteDAO;
import com.example.tasktracker.DAO.RemainderDAO;
import com.example.tasktracker.database.NoteDatabase;
import com.example.tasktracker.database.RemainderDatabase;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.entities.Remainder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allPinnedNotes;

    private RemainderDAO remainderDAO;
    private LiveData<List<Remainder>> allRemainders;


    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDAO = noteDatabase.noteDAO();
        allNotes = noteDAO.getAllNotes();
        allPinnedNotes = noteDAO.getAllPinnedNotes();

        RemainderDatabase remainderDatabase = RemainderDatabase.getInstance(application);
        remainderDAO = remainderDatabase.remainderDAO();
        allRemainders = remainderDAO.getAllRemainders();
    }

    public void insert(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.insert(note);
        });
    }

    public void update(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.update(note);
        });
    }

    public void delete(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.delete(note);
        });
    }

    public void deleteAllNotes() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.deleteAllNotes();
        });
    }


    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getAllPinnedNotes() {
        return allPinnedNotes;
    }

    public LiveData<List<Note>> getAllNotesAccordingToSearchQuery(String searchQuery) {
        return noteDAO.getAllNotesAccordingToSearchQuery(searchQuery);
    }

    public void insertRemainder(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.insert(remainder);
        });
    }

    public void updateRemainder(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.update(remainder);
        });
    }

    public void deleteRemainder(Remainder remainder) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.delete(remainder);
        });
    }

    public void deleteAllRemainders() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.deleteAllRemainders();
        });
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

    public void deleteByID(int ID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            remainderDAO.deleteByID(ID);
        });
    }

}
