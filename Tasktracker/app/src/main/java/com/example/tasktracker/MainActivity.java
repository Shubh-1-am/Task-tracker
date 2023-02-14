package com.example.tasktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tasktracker.Adapter.NoteAdapter;
import com.example.tasktracker.databinding.ActivityMainBinding;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.viewModel.MainActivityViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private MainHandler mainHandler;


    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    List<Note> noteList;


    public static final String IS_EDIT_MODE = "isEditMode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainHandler = new MainHandler();
        activityMainBinding.setVariable(BR.handler,mainHandler);

        showNotes();
    }

    public void showNotes(){
        mainActivityViewModel.getAllNotes().observe(this, notes -> {
            noteList = notes;
            if (noteList == null || noteList.size() == 0) {
                activityMainBinding.emptyListImageView.setVisibility(View.VISIBLE);
            } else {
                runOnUiThread(() -> {
                    activityMainBinding.emptyListImageView.setVisibility(View.GONE);
                    noteAdapter = new NoteAdapter(MainActivity.this, noteList);
                    recyclerView = activityMainBinding.recyclerView;
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(noteAdapter);
                    recyclerView.scrollToPosition(0);
                });
            }
        });
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