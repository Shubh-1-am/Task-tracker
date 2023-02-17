package com.example.tasktracker.Adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.tasktracker.entities.Remainder;

import java.util.List;


public class RemainderDiffUtilCallback extends DiffUtil.Callback {

    private final List<Remainder> oldRemainder;
    private final List<Remainder> newRemainder;

    public RemainderDiffUtilCallback(List<Remainder> oldRemainder, List<Remainder> newRemainder) {
        this.oldRemainder = oldRemainder;
        this.newRemainder = newRemainder;
    }

    @Override
    public int getOldListSize() {
        return oldRemainder.size();
    }

    @Override
    public int getNewListSize() {
        return newRemainder.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRemainder.get(oldItemPosition).getId() == newRemainder.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRemainder.get(oldItemPosition).equals(newRemainder.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

