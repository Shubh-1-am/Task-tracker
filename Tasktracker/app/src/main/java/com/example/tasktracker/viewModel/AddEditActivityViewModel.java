package com.example.tasktracker.viewModel;

import android.app.Application;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tasktracker.entities.Note;
import com.example.tasktracker.repository.NoteRepository;

public class AddEditActivityViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private MutableLiveData<Note> noteLiveData = new MutableLiveData<>();

    public AddEditActivityViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public void insertNote(Note note){
        noteRepository.insert(note);
    }

    public void updateNote(Note note){
        noteRepository.update(note);
    }

    public void deleteNote(Note note){
        noteRepository.delete(note);
    }

    public void setNote(Note note){
        noteLiveData.setValue(note);
    }

    public MutableLiveData<Note> getNote(){
        return noteLiveData;
    }

    @BindingAdapter("isPinned")
    public static void setPinned(ImageView view, boolean isPinned) {
        view.setSelected(isPinned);
    }


}
