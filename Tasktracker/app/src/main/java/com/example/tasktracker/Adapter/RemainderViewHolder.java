package com.example.tasktracker.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracker.databinding.RemainderItemBinding;
import com.example.tasktracker.entities.Remainder;

public class RemainderViewHolder extends RecyclerView.ViewHolder{

    private final RemainderItemBinding remainderItemBinding;
    private Remainder currentRemainder;

    public RemainderViewHolder(@NonNull RemainderItemBinding remainderItemBinding) {
        super(remainderItemBinding.getRoot());
        this.remainderItemBinding = remainderItemBinding;

        remainderItemBinding.remainderItemDeleteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        remainderItemBinding.remainderIetmEditImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void setRemainder(Remainder remainder){
        this.currentRemainder = remainder;
        remainderItemBinding.setRemainder(remainder);
    }
}
