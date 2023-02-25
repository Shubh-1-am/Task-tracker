package com.example.tasktracker.Adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.tasktracker.entities.Note;

import java.util.List;

public class NoteDiffCallback extends DiffUtil.Callback {

    private final List<Note> oldNotes;
    private final List<Note> newNotes;

    public NoteDiffCallback(List<Note> oldNotes, List<Note> newNotes) {
        this.oldNotes = oldNotes;
        this.newNotes = newNotes;
    }

    @Override
    public int getOldListSize() {
        return oldNotes.size();
    }

    @Override
    public int getNewListSize() {
        return newNotes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).getId() == newNotes.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).equals(newNotes.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
