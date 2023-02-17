package com.example.tasktracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracker.R;
import com.example.tasktracker.databinding.RemainderItemBinding;
import com.example.tasktracker.entities.Remainder;

import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderViewHolder>{

    private final Context context;
    private List<Remainder> remainderList;

    public RemainderAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public RemainderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RemainderItemBinding remainderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.remainder_item, parent, false);
        return new RemainderViewHolder(remainderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RemainderViewHolder holder, int position) {
        Remainder currentRemainder = remainderList.get(position);
        holder.setRemainder(currentRemainder);

    }

    @Override
    public int getItemCount() {
        return remainderList == null ? 0 : remainderList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setRemainderList(List<Remainder> newRemainderList) {
        if (remainderList == null) {
            remainderList = newRemainderList;
            notifyItemRangeInserted(0, newRemainderList.size());
        } else {
            RemainderDiffUtilCallback remainderDiffUtilCallback = new RemainderDiffUtilCallback(remainderList, newRemainderList);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(remainderDiffUtilCallback);
            remainderList = newRemainderList;
            diffResult.dispatchUpdatesTo(this);
        }
    }
}
