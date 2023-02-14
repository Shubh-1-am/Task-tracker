package com.example.tasktracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracker.R;
import com.example.tasktracker.databinding.NoteItemBinding;
import com.example.tasktracker.entities.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    private List<Note> noteList;
    private Context context;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
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
        holder.setNoteBackground();
        holder.setPinImage();
        holder.setNoteImage();
        holder.setNoteUrl(context);
        holder.setNoteDescription(context);
    }

    @Override
    public int getItemCount() {
        return noteList == null ? 0 : noteList.size();
    }
}
