package com.example.tasktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.os.Bundle;

import com.example.tasktracker.entities.Note;
import com.example.tasktracker.viewModel.AddEditActivityViewModel;

public class AddEditActivity extends AppCompatActivity {

    private Context context;
    private AddEditActivityViewModel addEditActivityViewModel;
    private ViewDataBinding activityAddEditBinding;
    private boolean isEditMode;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        addEditActivityViewModel = new AddEditActivityViewModel(getApplication());
        activityAddEditBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_add_edit, null, false);
        activityAddEditBinding.setVariable(BR.viewModel, addEditActivityViewModel);
        activityAddEditBinding.setLifecycleOwner(this);

        context = getBaseContext();
        isEditMode = getIntent().getBooleanExtra(MainActivity.IS_EDIT_MODE,false);


        if (!isEditMode){
            currentNote = new Note();
            addEditActivityViewModel.setNote(currentNote);
            


        }



    }
}