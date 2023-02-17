package com.example.tasktracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasktracker.databinding.ActivityRemainderDetailsBinding;
import com.example.tasktracker.entities.Remainder;
import com.example.tasktracker.viewModel.RemainderDetailsActivityViewModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class RemainderDetailsActivity extends AppCompatActivity {


    private ActivityRemainderDetailsBinding activityRemainderDetailsBinding;
    private RemainderDetailsActivityViewModel remainderDetailsActivityViewModel;

    private AlertDialog dialogAddRemainder;
    private final Calendar calendar = Calendar.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRemainderDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_remainder_details);
        remainderDetailsActivityViewModel = new ViewModelProvider(this).get(RemainderDetailsActivityViewModel.class);

        activityRemainderDetailsBinding.remainderActivityAddNewRemainderImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddRemainderDialog();

            }
        });
    }


    @SuppressLint("MissingInflatedId")
    private void showAddRemainderDialog() {

        if (dialogAddRemainder == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RemainderDetailsActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_remainder, (ViewGroup) findViewById(R.id.layout_add_remainder_dialog_container), false);
            builder.setView(view);
            dialogAddRemainder = builder.create();
            if (dialogAddRemainder.getWindow() != null) {
                dialogAddRemainder.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            MaterialTextView dateTextView = view.findViewById(R.id.dialog_remainder_date_textView);
            MaterialTextView timeTextView = view.findViewById(R.id.dialog_remainder_time_textView);
            EditText titleEditText = view.findViewById(R.id.dialog_remainder_title_editText);
            view.findViewById(R.id.dialog_remainder_date_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(String date) {
                            if (!date.equals("")) {
                                dateTextView.setText(date);
                                dateTextView.setTextColor(getColor(R.color.white));
                            }
                        }
                    });

                }
            });

            view.findViewById(R.id.dialog_remainder_time_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTime(new OnTimeSelectedListener() {
                        @Override
                        public void onTimeSelected(String time) {
                            if (!time.equals("")) {
                                timeTextView.setText(time);
                                timeTextView.setTextColor(getColor(R.color.white));
                            }
                        }
                    });
                }
            });

            view.findViewById(R.id.dialog_remainder_save_option_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (titleEditText.getText().toString().trim().isEmpty()) {
                        titleEditText.setError("Title cannot be empty");
                    }
                    else if (dateTextView.getText().toString().trim().isEmpty()) {
                        dateTextView.setError("Date cannot be empty");
                    }
                    else if (timeTextView.getText().toString().trim().isEmpty()) {
                        timeTextView.setError("Time cannot be empty");
                    }
                    else {
                        Remainder remainder = new Remainder();
                        remainder.setTitle(titleEditText.getText().toString().trim());
                        remainder.setDate(dateTextView.getText().toString().trim());
                        remainder.setTime(timeTextView.getText().toString().trim());
//                        remainderDetailsActivityViewModel.insert(remainder);
                        Toast.makeText(RemainderDetailsActivity.this, ""+remainder.getTitle()+ " " +remainder.getDate()+" "+remainder.getTime(), Toast.LENGTH_SHORT).show();
                        dialogAddRemainder.dismiss();
                    }

                }
            });

            view.findViewById(R.id.dialog_remainder_cancel_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddRemainder.dismiss();
                }
            });
        }

        dialogAddRemainder.show();

    }

    private void getTime(OnTimeSelectedListener listener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                RemainderDetailsActivity.this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    // Convert the hourOfDay to 12-hour format
                    int hour = hourOfDay % 12;
                    if (hour == 0) {
                        hour = 12;
                    }
                    String suffix = hourOfDay >= 12 ? "PM" : "AM";
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, suffix);
                    listener.onTimeSelected(selectedTime);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }


    private void getDate(OnDateSelectedListener listener) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RemainderDetailsActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                   String selectedDate = dayOfMonth + "/" + month + "/" + year;
                    Toast.makeText(this, "dekhooooo"+selectedDate, Toast.LENGTH_SHORT).show();
                    listener.onDateSelected(selectedDate);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }
}