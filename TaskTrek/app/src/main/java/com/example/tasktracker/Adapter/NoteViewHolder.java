package com.example.tasktracker.Adapter;

import android.content.Context;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tasktracker.ItemClickListener;
import com.example.tasktracker.databinding.NoteItemBinding;
import com.example.tasktracker.entities.Note;
import com.google.android.material.textview.MaterialTextView;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private final NoteItemBinding noteItemBinding;
    private Note currentNote;

    private Context context;
    private ItemClickListener itemClickListener;

    public NoteViewHolder(@NonNull NoteItemBinding noteItemBinding) {
        super(noteItemBinding.getRoot());
        this.noteItemBinding = noteItemBinding;
        noteItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(currentNote);
            }
        });

        noteItemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onLongClick(currentNote);
                return true;
            }
        });

        noteItemBinding.noteItemNoteImagePinImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onPinClick(currentNote);
            }
        });
    }

    public void setNoteBackground() {
        LinearLayout noteLayout = noteItemBinding.noteItemParentLinearlayout;
        noteLayout.setBackgroundResource(currentNote.getNoteBackground());
    }

    public void setPinImage() {
        noteItemBinding.noteItemNoteImagePinImageview.setVisibility(currentNote.isPinned() ? View.VISIBLE : View.GONE);
    }

    public void setNoteImage() {
        noteItemBinding.noteItemNoteImageImageview.setVisibility(currentNote.getNoteImage() != null ? View.VISIBLE : View.GONE);
        Glide.with(context)
                .load(currentNote.getNoteImage())
                .into(noteItemBinding.noteItemNoteImageImageview);
    }

    public void setNote(Note note) {
        noteItemBinding.setNote(note);
        currentNote = note;
    }

    public void setNoteUrl() {
        if (currentNote.getMarkdownLinkText() != null) {
            MaterialTextView urlTextView = noteItemBinding.noteItemNoteUrlTextview;
            Markwon markwon = Markwon.builder(context)
                    .usePlugin(StrikethroughPlugin.create())
                    .usePlugin(TaskListPlugin.create(context))
                    .usePlugin(CorePlugin.create())
                    .usePlugin(new AbstractMarkwonPlugin() {
                        @Override
                        public void configureVisitor(@NonNull MarkwonVisitor.Builder builder) {
                            super.configureVisitor(builder);
                        }
                    }).build();
            markwon.setMarkdown(urlTextView, currentNote.getMarkdownLinkText());
            urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
            noteItemBinding.noteItemNoteUrlLinearlayout.setVisibility(View.VISIBLE);
        }

    }

    public void setNoteDescription() {
        if (currentNote.getDescription() != null) {
            MaterialTextView descriptionTextView = noteItemBinding.noteItemNoteDescriptionTextview;

            Markwon markwon = Markwon.builder(context)
                    .usePlugin(StrikethroughPlugin.create())
                    .usePlugin(TaskListPlugin.create(context))
                    .usePlugin(CorePlugin.create())
                    .usePlugin(new AbstractMarkwonPlugin() {
                        @Override
                        public void configureVisitor(@NonNull MarkwonVisitor.Builder builder) {
                            super.configureVisitor(builder);
                        }
                    }).build();
            markwon.setMarkdown(descriptionTextView, currentNote.getDescription());
            noteItemBinding.noteItemNoteDescriptionTextview.setVisibility(View.VISIBLE);
        }
    }


    public void setContext(Context context) {
        this.context = context;
        itemClickListener = (ItemClickListener) context;
    }
}
