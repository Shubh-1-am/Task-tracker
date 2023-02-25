package com.example.tasktracker.viewModel;

import android.app.Application;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tasktracker.R;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.entities.Remainder;
import com.example.tasktracker.repository.NoteRepository;
import com.example.tasktracker.repository.RemainderRepository;

import java.util.List;

public class AddEditActivityViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;

    private RemainderRepository remainderRepository;
    private MutableLiveData<Note> noteLiveData = new MutableLiveData<>();

    public AddEditActivityViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        remainderRepository = new RemainderRepository(application);
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

    public void insertRemainder(Remainder remainder){
        remainderRepository.insert(remainder);
    }

    public void updateRemainder(Remainder remainder){
        remainderRepository.update(remainder);
    }

    public void deleteRemainder(Remainder remainder){
        remainderRepository.delete(remainder);
    }

    public void deleteAllRemainders(){
        remainderRepository.deleteAllRemainders();
    }

    public void deleteByID(int ID){remainderRepository.deleteByID(ID);}

    public int getLastInsertedIRemainderId(){
        return remainderRepository.getLastInsertedIRemainderId();
    }

    public LiveData<List<Remainder>> getAllRemainders(){
        return remainderRepository.getAllRemainders();
    }

    public LiveData<List<Remainder>> getAllRemaindersAccordingToSearchQuery(String searchQuery){
        return remainderRepository.getAllRemaindersAccordingToSearchQuery(searchQuery);
    }

    public LiveData<Remainder> getRemainderByID(int id){
        return remainderRepository.getRemainderByID(id);
    }





//    @BindingAdapter("isPinned")
//    public static void setPinned(ImageView view, boolean isPinned) {
//        view.setSelected(isPinned);
//    }


}
