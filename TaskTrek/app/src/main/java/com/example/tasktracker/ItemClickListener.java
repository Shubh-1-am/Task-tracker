package com.example.tasktracker;

import com.example.tasktracker.entities.Note;

public interface ItemClickListener {
    void onClick(Note note);

    void onLongClick(Note note);

    void onPinClick(Note note);
}
