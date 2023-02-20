package com.example.tasktracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasktracker.Adapter.RemainderAdapter;
import com.example.tasktracker.broadcastReceiver.AlarmBroadcast;
import com.example.tasktracker.databinding.ActivityRemainderDetailsBinding;
import com.example.tasktracker.entities.Remainder;
import com.example.tasktracker.viewModel.RemainderDetailsActivityViewModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class RemainderDetailsActivity extends AppCompatActivity implements OnNotifyRemainderListener, OnDeleteRemainderListener, OnEditRemainderListener{


    private ActivityRemainderDetailsBinding activityRemainderDetailsBinding;
    private RemainderDetailsActivityViewModel remainderDetailsActivityViewModel;

    private AlertDialog dialogAddRemainder;

    private RecyclerView recyclerView;
    private RemainderAdapter remainderAdapter;

    private List<Remainder> remainderList;

    public static final String REMAINDER_TITLE = "remainder_title";
    public static final String REMAINDER_ID = "remainder_id";
    public static final String REMAINDER_LISTENER = "remainder_OnNotifyRemainderListener";

    public static OnNotifyRemainderListener onNotifyRemainderListener;

    private UtilApplication utilApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRemainderDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_remainder_details);
        remainderDetailsActivityViewModel = new ViewModelProvider(this).get(RemainderDetailsActivityViewModel.class);
        onNotifyRemainderListener = (OnNotifyRemainderListener) this;

        activityRemainderDetailsBinding.remainderActivityAddNewRemainderImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddRemainderDialog(false, null);

            }
        });

        activityRemainderDetailsBinding.remainderActivityBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        utilApplication = UtilApplication.getInstance();
        utilApplication.setActivity(this);


        recyclerView = activityRemainderDetailsBinding.remainderActivityRecyclerView;
        remainderAdapter = new RemainderAdapter(RemainderDetailsActivity.this);
        remainderAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RemainderDetailsActivity.this));
        recyclerView.setHasFixedSize(true);
        showAllRemainders();
        showRemainders();


    }

    private void showRemainders() {

        activityRemainderDetailsBinding.remainderActivitySearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString();
                showSearchedRemainder(searchQuery);
            }
        });
    }

    private void showSearchedRemainder(String searchQuery) {
        remainderDetailsActivityViewModel.getAllRemaindersAccordingToSearchQuery("%" + searchQuery + "%").observe(this, remainders -> {
            remainderList = remainders;
            runOnUiThread(this::setAdapter);
        });
    }

    private void showAllRemainders() {

        remainderDetailsActivityViewModel.getAllRemainders().observe(this, remainders -> {
            remainderList = remainders;
            if (remainderList == null || remainderList.size() == 0){
                Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show();
            }
            else {
              runOnUiThread(this::setAdapter);
            }

        });
    }

    private void setAdapter() {
        remainderAdapter.setRemainderList(remainderList);
        recyclerView.setAdapter(remainderAdapter);
    }


    @SuppressLint("MissingInflatedId")
    private void showAddRemainderDialog(boolean isEditMode, Remainder remainder) {

        Calendar calendar = Calendar.getInstance();

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
                        public void onDateSelected(String date, int year, int month, int dayOfMonth) {
                            if (!date.equals("")) {
                                dateTextView.setText(date);
                                dateTextView.setTextColor(getColor(R.color.white));
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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
                        public void onTimeSelected(String time, int hourOfDay, int minute){
                            if (!time.equals("")) {
                                timeTextView.setText(time);
                                timeTextView.setTextColor(getColor(R.color.white));
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
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

                            Remainder currentRemainder;
                            if (isEditMode){
                                currentRemainder = remainder;
                                deleteAlarm(currentRemainder.getId());
                                currentRemainder.setTitle(titleEditText.getText().toString().trim());
                                currentRemainder.setDate(dateTextView.getText().toString().trim());
                                currentRemainder.setTime(timeTextView.getText().toString().trim());
                                remainderDetailsActivityViewModel.update(currentRemainder);
                                Toast.makeText(RemainderDetailsActivity.this, "Remainder Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                currentRemainder = new Remainder();
                                currentRemainder.setTitle(titleEditText.getText().toString().trim());
                                currentRemainder.setDate(dateTextView.getText().toString().trim());
                                currentRemainder.setTime(timeTextView.getText().toString().trim());
                                remainderDetailsActivityViewModel.insert(currentRemainder);
                                Toast.makeText(RemainderDetailsActivity.this, "Remainder Added", Toast.LENGTH_SHORT).show();
                            }

                            setAlarm(currentRemainder, calendar);
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

    private void setAlarm(Remainder remainder, Calendar calendar) {

            Toast.makeText(this, "In set Alarm method", Toast.LENGTH_SHORT).show();
            Intent iBroadCast = new Intent(RemainderDetailsActivity.this, AlarmBroadcast.class);
            iBroadCast.putExtra(REMAINDER_TITLE, remainder.getTitle());
            iBroadCast.putExtra(REMAINDER_ID,remainder.getId());
            try{
//                iBroadCast.putExtra(REMAINDER_LISTENER, (OnNotifyRemainderListener)this);
            }catch (Exception e){
                Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(RemainderDetailsActivity.this, remainder.getId(), iBroadCast, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            Toast.makeText(RemainderDetailsActivity.this, ""+remainder.getTitle()+ " " +remainder.getDate()+" "+remainder.getTime(), Toast.LENGTH_SHORT).show();

    }

    private void deleteAlarm(int ID){
        Intent iBroadCast = new Intent(RemainderDetailsActivity.this, AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(RemainderDetailsActivity.this, ID, iBroadCast, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    private void getTime(OnTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
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
                    listener.onTimeSelected(selectedTime, hourOfDay, minute);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }


    private void getDate(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RemainderDetailsActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                   String selectedDate = dayOfMonth + "/" + month + "/" + year;
                    Toast.makeText(this, "dekhooooo"+selectedDate, Toast.LENGTH_SHORT).show();
                    listener.onDateSelected(selectedDate, year, month, dayOfMonth);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    @Override
    public void onNotifyRemainder(int ID) {
        remainderDetailsActivityViewModel.deleteByID(ID);
    }

    @Override
    public void onDeleteRemainder(Remainder remainder) {
        remainderDetailsActivityViewModel.delete(remainder);
        deleteAlarm(remainder.getId());
        Toast.makeText(this, "Remainder deleted successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEditRemainder(Remainder remainder) {
        if (dialogAddRemainder == null) {
            showAddRemainderDialog(true,remainder);
        }

        EditText titleEditText =  (EditText)(dialogAddRemainder.findViewById(R.id.dialog_remainder_title_editText));
        MaterialTextView dateTextView = (MaterialTextView) dialogAddRemainder.findViewById(R.id.dialog_remainder_date_textView);
        MaterialTextView timeTextView = (MaterialTextView) dialogAddRemainder.findViewById(R.id.dialog_remainder_time_textView);

        if (titleEditText != null && dateTextView != null && timeTextView != null){
            titleEditText.setText(remainder.getTitle());
            dateTextView.setText(remainder.getDate());
            timeTextView.setText(remainder.getTime());
        }


    }



}