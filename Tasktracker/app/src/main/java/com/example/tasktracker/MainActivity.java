package com.example.tasktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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


        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainHandler = new MainHandler();
        activityMainBinding.setVariable(BR.handler,mainHandler);
    }

    public class MainHandler{

        public void onAddNoteClick(View view){
            Toast.makeText(MainActivity.this, "Add note", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,AddEditActivity.class);
            intent.putExtra(IS_EDIT_MODE,false);
            startActivity(intent);
        }


    }
}