package com.example.tasktracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tasktracker.DAO.NoteDAO;
import com.example.tasktracker.database.NoteDatabase;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.entities.Remainder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allPinnedNotes;

    public NoteRepository(Application application){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDAO = noteDatabase.noteDAO();
        allNotes = noteDAO.getAllNotes();
        allPinnedNotes = noteDAO.getAllPinnedNotes();
    }

    public void insert(Note note){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.insert(note);
        });
    }

    public void update(Note note){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.update(note);
        });
    }

    public void delete(Note note){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDAO.delete(note);
        });
    }

    public void deleteAllNotes(){
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

    public LiveData<List<Note>> getAllNotesAccordingToSearchQuery(String searchQuery){
        return noteDAO.getAllNotesAccordingToSearchQuery(searchQuery);
    }


}
