package com.example.tasktracker.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracker.OnDeleteRemainderListener;
import com.example.tasktracker.OnEditRemainderListener;
import com.example.tasktracker.databinding.RemainderItemBinding;
import com.example.tasktracker.entities.Remainder;

public class RemainderViewHolder extends RecyclerView.ViewHolder{

    private final RemainderItemBinding remainderItemBinding;
    private Remainder currentRemainder;
    private Context context;

    private OnDeleteRemainderListener deleteRemainderListener;
    private OnEditRemainderListener editRemainderListener;

    public RemainderViewHolder(@NonNull RemainderItemBinding remainderItemBinding) {
        super(remainderItemBinding.getRoot());
        this.remainderItemBinding = remainderItemBinding;

        remainderItemBinding.remainderItemDeleteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRemainderListener.onDeleteRemainder(currentRemainder);
            }
        });

        remainderItemBinding.remainderIetmEditImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRemainderListener.onEditRemainder(currentRemainder);
                Toast.makeText(context, "Hiiii", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setRemainder(Remainder remainder){
        this.currentRemainder = remainder;
        remainderItemBinding.setRemainder(remainder);
    }

    public void setContext(Context context) {
        this.context = context;
        deleteRemainderListener = (OnDeleteRemainderListener) context;
        editRemainderListener = (OnEditRemainderListener) context;
    }
}
