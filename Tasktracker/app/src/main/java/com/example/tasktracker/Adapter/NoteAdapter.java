package com.example.tasktracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracker.R;
import com.example.tasktracker.databinding.NoteItemBinding;
import com.example.tasktracker.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    private List<Note> noteList;
    private final Context context;

    public NoteAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoteItemBinding noteItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.note_item, parent, false);
        return new NoteViewHolder(noteItemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = noteList.get(position);
        holder.setNote(currentNote);
        holder.setContext(context);
        holder.setNoteBackground();
        holder.setPinImage();
        holder.setNoteImage();
        holder.setNoteUrl();
        holder.setNoteDescription();

    }
    @Override
    public int getItemCount() {
        return noteList == null ? 0 : noteList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

   public void setNoteList(List<Note> newNoteList) {
        if (noteList == null) {
            noteList = newNoteList;
        } else {

            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NoteDiffCallback(noteList, newNoteList), false);
            noteList.clear();
            noteList.addAll(newNoteList);
            diffResult.dispatchUpdatesTo(this);

        }

    }
}
