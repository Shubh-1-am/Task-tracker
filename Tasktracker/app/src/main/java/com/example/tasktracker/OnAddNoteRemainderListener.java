package com.example.tasktracker;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tasktracker.entities.Remainder;

public interface OnAddNoteRemainderListener {
    int onAddNoteRemainderListener(Context context);
    LiveData<Remainder> onGettingRemainderByID(int ID);
}
