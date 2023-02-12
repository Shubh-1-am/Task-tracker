package com.example.tasktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tasktracker.databinding.ActivityMainBinding;
import com.example.tasktracker.viewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private MainHandler mainHandler;

    public static final String IS_EDIT_MODE = "isEditMode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainActivityViewModel = new MainActivityViewModel(getApplication());
        activityMainBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);
        mainHandler = new MainHandler();

    }

    private class MainHandler{

        public void onAddNoteClick(View view){
            Intent intent = new Intent(MainActivity.this,AddEditActivity.class);
            intent.putExtra(IS_EDIT_MODE,false);
            startActivity(intent);
        }


    }
}