package com.example.tasktracker;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tasktracker.entities.Remainder;

import java.io.Serializable;

public interface OnNotifyRemainderListener{

    void onNotifyRemainder(int ID);

}
