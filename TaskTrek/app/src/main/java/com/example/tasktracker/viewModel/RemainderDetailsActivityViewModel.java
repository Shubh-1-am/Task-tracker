package com.example.tasktracker.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tasktracker.entities.Remainder;
import com.example.tasktracker.repository.NoteRepository;
import com.example.tasktracker.repository.RemainderRepository;

import java.util.List;

public class RemainderDetailsActivityViewModel extends AndroidViewModel {
    private RemainderRepository remainderRepository;
    private LiveData<List<Remainder>> allRemainders;

    public RemainderDetailsActivityViewModel(@NonNull Application application) {
        super(application);
        remainderRepository = new RemainderRepository(application);
        allRemainders = remainderRepository.getAllRemainders();
    }
    public void insert(Remainder remainder){
        remainderRepository.insert(remainder);
    }

    public void update(Remainder remainder){
        remainderRepository.update(remainder);
    }

    public void delete(Remainder remainder){
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
        return allRemainders;
    }

    public LiveData<List<Remainder>> getAllRemaindersAccordingToSearchQuery(String searchQuery){
        return remainderRepository.getAllRemaindersAccordingToSearchQuery(searchQuery);
    }
    public LiveData<Remainder> getRemainderByID(int id){
        return remainderRepository.getRemainderByID(id);
    }
}
