package com.example.tasktracker.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tasktracker.entities.Note;
import com.example.tasktracker.repository.NoteRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allPinnedNotes;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
        allPinnedNotes = noteRepository.getAllPinnedNotes();
    }

    public void insert(Note note){
        noteRepository.insert(note);
    }

    public void update(Note note){
        noteRepository.update(note);
    }

    public void delete(Note note){
        noteRepository.delete(note);
    }

    public void deleteAllNotes(){
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public LiveData<List<Note>> getAllPinnedNotes(){
        return allPinnedNotes;
    }

    public LiveData<List<Note>> getAllNotesAccordingToSearchQuery(String searchQuery){
        return noteRepository.getAllNotesAccordingToSearchQuery(searchQuery);
    }
}
