package com.example.tasktracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tasktracker.Adapter.NoteAdapter;
import com.example.tasktracker.broadcastReceiver.AlarmBroadcast;
import com.example.tasktracker.databinding.ActivityMainBinding;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ItemClickListener {


    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private MainHandler mainHandler;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    private AlertDialog dialogDeleteNote;
    private AlertDialog dialogUnpinNote;
    List<Note> noteList;
    public static final String IS_EDIT_MODE = "isEditMode";
    public static final String NOTE_EXTRA = "note_for_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainHandler = new MainHandler();
        activityMainBinding.setVariable(BR.handler, mainHandler);

        noteAdapter = new NoteAdapter(MainActivity.this);
        noteAdapter.setHasStableIds(true);
        recyclerView = activityMainBinding.recyclerView;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        showAllNotes();
        showNotes();

    }


    private void showNotes() {

        activityMainBinding.searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) showAllNotes();
            }
        });
        activityMainBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString();
                showSearchedNotes(searchQuery);

            }
        });

    }

    public void showAllNotes() {
        mainActivityViewModel.getAllNotes().observe(this, notes -> {
            noteList = notes;
            if (noteList == null || noteList.size() == 0) {
                activityMainBinding.emptyListImageView.setVisibility(View.VISIBLE);
            } else {
                runOnUiThread(() -> {
                    activityMainBinding.emptyListImageView.setVisibility(View.GONE);
                    setAdapter(noteList);
                });
            }
        });


    }

    private void showSearchedNotes(String searchQuery) {
        mainActivityViewModel.getAllNotesAccordingToSearchQuery("%" + searchQuery + "%")
                .observe(this, notes -> {
                    noteList = notes;
                    runOnUiThread(() -> setAdapter(noteList));
                });
    }


    private void setAdapter(List<Note> noteList) {
        noteAdapter.setNoteList(noteList);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public void onClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(IS_EDIT_MODE, true);
        intent.putExtra(NOTE_EXTRA, note);
        startActivity(intent);
    }

    @Override
    public void onLongClick(Note note) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layout_delete_note_container), false);
        builder.setView(view);
        dialogDeleteNote = builder.create();
        if (dialogDeleteNote.getWindow() != null) {
            dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        view.findViewById(R.id.dialog_yes_delete_option_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteNote.dismiss();
                mainActivityViewModel.delete(note);
                noteList.remove(note);
                setAdapter(noteList);

            }
        });

        view.findViewById(R.id.dialog_no_delete_option_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteNote.dismiss();
            }
        });

        dialogDeleteNote.show();
    }


    @Override
    public void onPinClick(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_unpin_note, (ViewGroup) findViewById(R.id.layout_unpin_note_container), false);
        builder.setView(view);
        dialogUnpinNote = builder.create();
        if (dialogUnpinNote.getWindow() != null) {
            dialogUnpinNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        view.findViewById(R.id.dialog_yes_unpin_option_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnpinNote.dismiss();
                note.setPinned(false);
                mainActivityViewModel.update(note);
            }
        });

        view.findViewById(R.id.dialog_no_unpin_option_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnpinNote.dismiss();
            }
        });

        dialogUnpinNote.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public class MainHandler {

        public void onAddNoteClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra(IS_EDIT_MODE, false);
            startActivity(intent);
        }

        public void onAlarmClick(View view) {
            Intent intent = new Intent(MainActivity.this, RemainderDetailsActivity.class);
            startActivity(intent);
        }

    }
}