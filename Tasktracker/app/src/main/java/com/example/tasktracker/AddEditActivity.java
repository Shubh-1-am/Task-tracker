package com.example.tasktracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tasktracker.broadcastReceiver.AlarmBroadcast;
import com.example.tasktracker.database.RemainderDatabase;
import com.example.tasktracker.databinding.ActivityAddEditBinding;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.entities.Remainder;
import com.example.tasktracker.viewModel.AddEditActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textview.MaterialTextView;
import com.yahiaangelo.markdownedittext.MarkdownEditText;
import com.yahiaangelo.markdownedittext.MarkdownStylesBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;

public class AddEditActivity extends AppCompatActivity implements OnNotifyRemainderListener {

    private Context context;
    private AddEditActivityViewModel addEditActivityViewModel;
    private ActivityAddEditBinding activityAddEditBinding;
    private boolean isEditMode;
    private Note currentNote;
    private AddEditActivityHandler addEditActivityHandler;

    private AlertDialog dialogAddImage;
    private AlertDialog dialogAddURL;

    private AlertDialog dialogAddRemainder;
    private Remainder currentRemainder;
    private Remainder previousRemainder;
    private Calendar calendar = Calendar.getInstance();

    private OnAddNoteRemainderListener onAddNoteRemainderListener;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY = 2;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 3;

    private static final int REQUEST_CODE_TAKE_PHOTO_FROM_CAMERA = 4;
    public static final String NOTE_UPDATED = "note_updated";
    public static final String NOTE_ADDED = "note_added";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        addEditActivityViewModel = new ViewModelProvider(this).get(AddEditActivityViewModel.class);
        activityAddEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit);
        addEditActivityHandler = new AddEditActivityHandler();
        activityAddEditBinding.setVariable(BR.viewModel, addEditActivityViewModel);
        activityAddEditBinding.setVariable(BR.handler, addEditActivityHandler);
        activityAddEditBinding.setLifecycleOwner(this);

        UtilApplication app = UtilApplication.getInstance();
        app.setAddEditActivity(this);


        context = getBaseContext();
        isEditMode = getIntent().getBooleanExtra(MainActivity.IS_EDIT_MODE, false);

        initMarkdownEditText();
        activityAddEditBinding.addImageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manageMarkdownStyleBar();
                showAddImageDialog();
            }
        });

        activityAddEditBinding.noteImageImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                showAddImageDialog();
            }
        });

        activityAddEditBinding.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.GONE);
                activityAddEditBinding.addImageLinearLayout.setVisibility(View.VISIBLE);
                currentNote.setNoteImage("");
            }
        });

        activityAddEditBinding.addUrlLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                showAddURLDialog();
            }
        });

        activityAddEditBinding.bottomSheet.getRoot().findViewById(R.id.bottom_sheet_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                showBottomSheetDialog();
            }
        });

        activityAddEditBinding.addUrlImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                showAddURLDialog();
            }
        });


        activityAddEditBinding.delete2ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // At every click on this button, delete the last markdown string [title](url) from currentNote.getMarkdownLinkText()
                // and update the markdownLinkText in currentNote
                manageMarkdownStyleBar();
                String markdownLinkText = currentNote.getMarkdownLinkText();

                int lastIndex = markdownLinkText.lastIndexOf(") [");
                if (lastIndex == -1) {
                    markdownLinkText = "";
                    currentNote.setMarkdownLinkText(markdownLinkText);
                } else {
                    markdownLinkText = markdownLinkText.substring(0, lastIndex + 1).trim();
                    currentNote.setMarkdownLinkText(markdownLinkText);
                    displayUrl();
                }

                if (markdownLinkText.isEmpty()) {
                    activityAddEditBinding.addUrlRelativeLayout.setVisibility(View.GONE);
                    activityAddEditBinding.addUrlLinearLayout.setVisibility(View.VISIBLE);
                }


            }
        });

        activityAddEditBinding.noteBackgroundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMarkdownStyleBar();
                showBottomSheetDialog();

            }
        });


        if (!isEditMode) {
            currentNote = new Note();
            activityAddEditBinding.noteBackgroundImageView.setImageResource(R.color.colorNoteDefaultColor);
            currentNote.setPinned(false);
            currentNote.setNoteImage("");
            currentNote.setNoteBackground(R.color.colorNoteDefaultColor);
        } else {
            currentNote = (Note) (getIntent().getSerializableExtra(MainActivity.NOTE_EXTRA));
            activityAddEditBinding.lastEditedOnLinearLayout.setVisibility(View.VISIBLE);

            activityAddEditBinding.noteBackgroundImageView.setImageResource(currentNote.getNoteBackground());
            if (currentNote.getNoteImage() != null && !currentNote.getNoteImage().isEmpty()) {
                activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
//                activityAddEditBinding.noteImageImageview.setImageURI(Uri.parse(currentNote.getNoteImage()));
                Glide.with(this).load(currentNote.getNoteImage()).into(activityAddEditBinding.noteImageImageview);
                activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
            }
            if (currentNote.getDescription() != null) {
                activityAddEditBinding.noteDescriptionMarkdownEditText.renderMD(currentNote.getDescription());
            } else {
                activityAddEditBinding.noteDescriptionMarkdownEditText.setText("");
            }

            if (currentNote.getMarkdownLinkText() != null) {
                activityAddEditBinding.addUrlLinearLayout.setVisibility(View.GONE);
                displayUrl();
                activityAddEditBinding.addUrlRelativeLayout.setVisibility(View.VISIBLE);
            }
            showRemainderDescription();
            setPinImage();
        }
        addEditActivityViewModel.setNote(currentNote);

        activityAddEditBinding.addRemainderLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNote.getRemainder_id() == 0) {
                    showAddRemainderDialog(false);
                } else {
                    addEditActivityViewModel.getRemainderByID(currentNote.getRemainder_id()).observe(AddEditActivity.this, new Observer<Remainder>() {
                        @Override
                        public void onChanged(Remainder remainder) {
                            if (remainder != null) {
                                previousRemainder = remainder;
                            }
                            showAddRemainderDialog(true);

                        }
                    });
                }
                showRemainderDescription();

            }

        });
    }

    private void showRemainderDescription() {
        if (currentNote.getRemainder_id() != 0) {
            addEditActivityViewModel.getRemainderByID(currentNote.getRemainder_id()).observe(AddEditActivity.this, new Observer<Remainder>() {
                @Override
                public void onChanged(Remainder remainder) {
                    if (remainder != null) {
                        Toast.makeText(AddEditActivity.this, "" + remainder.getTitle() + " " + remainder.getDate(), Toast.LENGTH_SHORT).show();
                        activityAddEditBinding.addRemainderTextview.setText(remainder.getDate() + " " + remainder.getTime());
                        activityAddEditBinding.addRemainderTextview.setTextColor(getColor(R.color.white));
                    }
                }
            });
        }
    }


    private void manageMarkdownStyleBar() {
        if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
            activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
            activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
        }
    }

    private void setPinImage() {
        if (currentNote.isPinned()) {
            activityAddEditBinding.pinUnpinImageView.setImageResource(R.drawable.pin_filled);
        } else {
            activityAddEditBinding.pinUnpinImageView.setImageResource(R.drawable.pin_unfilled);
        }
    }

    private void showBottomSheetDialog() {

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(activityAddEditBinding.bottomSheet.getRoot());
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY);
        }
    }

    private void initMarkdownEditText() {
        MarkdownEditText markdownEditText = activityAddEditBinding.noteDescriptionMarkdownEditText;
        MarkdownStylesBar markdownStylesBar = activityAddEditBinding.markdownStylesBar;
        markdownStylesBar.setStylesList(new MarkdownEditText.TextStyle[]{
                MarkdownEditText.TextStyle.BOLD,
                MarkdownEditText.TextStyle.ITALIC,
                MarkdownEditText.TextStyle.STRIKE,
                MarkdownEditText.TextStyle.UNORDERED_LIST,
                MarkdownEditText.TextStyle.ORDERED_LIST,
                MarkdownEditText.TextStyle.TASKS_LIST
        });
        markdownStylesBar.setMarkdownEditText(markdownEditText);

        markdownEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markdownEditText.setCursorVisible(true);
                activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.VISIBLE);

            }
        });
        markdownEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddImageDialog() {
        if (dialogAddImage == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_image, (ViewGroup) findViewById(R.id.layout_add_image_options), false);
            builder.setView(view);
            dialogAddImage = builder.create();
            if (dialogAddImage.getWindow() != null) {
                dialogAddImage.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.dialog_choose_from_gallery_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddImage.dismiss();
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                    } else {
                        selectImage();
                    }
                }
            });

            view.findViewById(R.id.dialog_take_photo_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddImage.dismiss();
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddEditActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                    } else {
                        takePhoto();
                    }
                }
            });

            view.findViewById(R.id.dialog_cancel_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddImage.dismiss();
                }
            });

        }
        dialogAddImage.show();
    }

    @SuppressLint("MissingInflatedId")
    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layout_add_url_container), false);
            builder.setView(view);
            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.dialog_add_option_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                    String url = ((EditText) Objects.requireNonNull(dialogAddURL.findViewById(R.id.dialog_add_link_editText))).getText().toString();
                    String title = ((EditText) Objects.requireNonNull(dialogAddURL.findViewById(R.id.dialog_add_title_editText))).getText().toString();

                    if (url.isEmpty()) {
                        Toast.makeText(AddEditActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    } else {
                        // Create markdown text for link having title and store it as String in note.markDown
                        title = title.isEmpty() ? url : title;
                        String markdownText = "[" + title + "](" + url + ")";
                        if (currentNote.getMarkdownLinkText() == null) {
                            currentNote.setMarkdownLinkText(markdownText);
                        } else {
                            // If there is already a markdown text for link, add a new line then append the new markdown text to it
                            currentNote.setMarkdownLinkText(currentNote.getMarkdownLinkText() + " " + markdownText);
                        }

                        activityAddEditBinding.addUrlLinearLayout.setVisibility(View.GONE);
                        displayUrl();
                        activityAddEditBinding.addUrlRelativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            view.findViewById(R.id.dialog_cancel2_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }
        dialogAddURL.show();
    }

    private void displayUrl() {
        MaterialTextView urlTextView = activityAddEditBinding.urlPlaceholderTextview;
        Markwon markwon = Markwon.builder(this)
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TaskListPlugin.create(this))
                .usePlugin(CorePlugin.create())
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureVisitor(@NonNull MarkwonVisitor.Builder builder) {
                        super.configureVisitor(builder);
                    }
                }).build();
        markwon.setMarkdown(urlTextView, currentNote.getMarkdownLinkText());
        Log.d("TAG", "displayUrl: " + urlTextView);
        Log.d("TAG", "displayUrl: " + urlTextView.getText());
        urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.notes.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image != null) {
            String currentPhotoPath = image.getAbsolutePath();
            currentNote.setNoteImage(currentPhotoPath);
            Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
        }
        return image;
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    currentNote.setNoteImage(getPathFromUri(selectedImageUri));
                    activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
                    activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(activityAddEditBinding.noteImageImageview);
                }
            }
        }

        if (requestCode == REQUEST_CODE_TAKE_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
            activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
            activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
            Glide.with(this)
                    .load(currentNote.getNoteImage())
                    .into(activityAddEditBinding.noteImageImageview);
        }
    }

    @Override
    public void onNotifyRemainder(int ID) {
        addEditActivityViewModel.deleteByID(ID);
        Toast.makeText(context, "Delettedddddd", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    private void showAddRemainderDialog(boolean isEditMode) {


        if (dialogAddRemainder == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_remainder, (ViewGroup) findViewById(R.id.layout_add_remainder_dialog_container), false);

            builder.setView(view);
            dialogAddRemainder = builder.create();
            if (dialogAddRemainder.getWindow() != null) {
                dialogAddRemainder.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            MaterialTextView dateTextView = view.findViewById(R.id.dialog_remainder_date_textView);
            MaterialTextView timeTextView = view.findViewById(R.id.dialog_remainder_time_textView);
            EditText titleEditText = view.findViewById(R.id.dialog_remainder_title_editText);

            titleEditText.setText(currentNote.getTitle());
            if (previousRemainder != null) {
                dateTextView.setText(previousRemainder.getDate());
                timeTextView.setText(previousRemainder.getTime());
                titleEditText.setText(previousRemainder.getTitle());

                dateTextView.setTextColor(getColor(R.color.white));
                timeTextView.setTextColor(getColor(R.color.white));
                titleEditText.setTextColor(getColor(R.color.white));
            }

            if (currentRemainder != null) {
                dateTextView.setText(currentRemainder.getDate());
                timeTextView.setText(currentRemainder.getTime());
                titleEditText.setText(currentRemainder.getTitle());
                dateTextView.setTextColor(getColor(R.color.white));
                timeTextView.setTextColor(getColor(R.color.white));
                titleEditText.setTextColor(getColor(R.color.white));
            }

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
                        public void onTimeSelected(String time, int hourOfDay, int minute) {
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
                    } else if (dateTextView.getText().toString().trim().isEmpty() || dateTextView.getText().toString().trim().equals("Select Date")) {
                        dateTextView.setError("Date cannot be empty");
                    } else if (timeTextView.getText().toString().trim().isEmpty() || timeTextView.getText().toString().trim().equals("Select Time")) {
                        timeTextView.setError("Time cannot be empty");
                    } else {


                        if (isEditMode && previousRemainder != null) {
                            currentRemainder = previousRemainder;
                            deleteAlarm(currentRemainder.getId());
                            currentRemainder.setTitle(titleEditText.getText().toString().trim());
                            currentRemainder.setDate(dateTextView.getText().toString().trim());
                            currentRemainder.setTime(timeTextView.getText().toString().trim());
                            currentNote.setRemainder_id(currentRemainder.getId());
                        } else if (isEditMode) {
                            currentRemainder = new Remainder();
                            currentRemainder.setTitle(titleEditText.getText().toString().trim());
                            currentRemainder.setDate(dateTextView.getText().toString().trim());
                            currentRemainder.setTime(timeTextView.getText().toString().trim());
                            int lastInsertedId = addEditActivityViewModel.getLastInsertedIRemainderId();
                            currentRemainder.setId(lastInsertedId + 1);

                        } else {
                            currentRemainder = new Remainder();
                            currentRemainder.setTitle(titleEditText.getText().toString().trim());
                            currentRemainder.setDate(dateTextView.getText().toString().trim());
                            currentRemainder.setTime(timeTextView.getText().toString().trim());
                            int lastInsertedId = addEditActivityViewModel.getLastInsertedIRemainderId();
                            currentRemainder.setId(lastInsertedId + 1);
                        }
                        activityAddEditBinding.addRemainderTextview.setText(currentRemainder.getDate() + " " + currentRemainder.getTime());
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
        Log.d("TAG", "setAlarm: innnn");
        Toast.makeText(this, "In set Alarm method", Toast.LENGTH_SHORT).show();
        Intent iBroadCast = new Intent(AddEditActivity.this, AlarmBroadcast.class);
        iBroadCast.putExtra(RemainderDetailsActivity.REMAINDER_TITLE, remainder.getTitle());
        iBroadCast.putExtra(RemainderDetailsActivity.REMAINDER_ID, remainder.getId());
        Toast.makeText(this, "ID here is : " + currentNote.getRemainder_id(), Toast.LENGTH_SHORT).show();
        Log.d("TAG", "setAlarm: id here is : " + currentNote.getRemainder_id());
        iBroadCast.putExtra(RemainderDetailsActivity.IS_REMAINDER_FROM_REMAINDER_DETAILS_ACTIVITY, false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditActivity.this, remainder.getId(), iBroadCast, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(AddEditActivity.this, "" + remainder.getTitle() + " " + remainder.getDate() + " " + remainder.getTime(), Toast.LENGTH_SHORT).show();

    }

    private void deleteAlarm(int ID) {
        Intent iBroadCast = new Intent(AddEditActivity.this, AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditActivity.this, ID, iBroadCast, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void getTime(OnTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddEditActivity.this,
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
                AddEditActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = dayOfMonth + "/" + month + "/" + year;
                    Toast.makeText(this, "dekhooooo" + selectedDate, Toast.LENGTH_SHORT).show();
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("savedNoteState", currentNote);
        outState.putSerializable("savedCurrentRemainderState", currentRemainder);
        outState.putSerializable("savedPreviousRemainderState", previousRemainder);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentNote = (Note) savedInstanceState.getSerializable("savedNoteState");
        currentRemainder = (Remainder) savedInstanceState.getSerializable("savedCurrentRemainderState");
        previousRemainder = (Remainder) savedInstanceState.getSerializable("savedPreviousRemainderState");
        if (currentNote.getNoteImage() != null && currentNote.getNoteImage().length() > 0) {
            activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
            activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
            Glide.with(this)
                    .load(currentNote.getNoteImage())
                    .into(activityAddEditBinding.noteImageImageview);
        }
        if (currentNote.getNoteBackground() != 0) {
            activityAddEditBinding.noteBackgroundImageView.setImageResource(currentNote.getNoteBackground());
        }
        if (currentNote.getMarkdownLinkText() != null) {
            activityAddEditBinding.addUrlLinearLayout.setVisibility(View.GONE);
            displayUrl();
            activityAddEditBinding.addUrlRelativeLayout.setVisibility(View.VISIBLE);
        }

        if (previousRemainder != null && currentRemainder == null){
            activityAddEditBinding.addRemainderTextview.setText(previousRemainder.getDate() + " " + previousRemainder.getTime());
            activityAddEditBinding.addRemainderTextview.setTextColor(getColor(R.color.white));
        }
        else if (currentRemainder != null) {
            activityAddEditBinding.addRemainderTextview.setText(currentRemainder.getDate() + " " + currentRemainder.getTime());
            activityAddEditBinding.addRemainderTextview.setTextColor(getColor(R.color.white));
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialogAddRemainder != null && dialogAddRemainder.isShowing()) {
            dialogAddRemainder.dismiss();
        }
        if (dialogAddImage != null && dialogAddImage.isShowing()) {
            dialogAddImage.dismiss();
        }
        if (dialogAddURL != null && dialogAddURL.isShowing()) {
            dialogAddURL.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogAddRemainder != null && dialogAddRemainder.isShowing()) {
            dialogAddRemainder.dismiss();
        }
        if (dialogAddImage != null && dialogAddImage.isShowing()) {
            dialogAddImage.dismiss();
        }
        if (dialogAddURL != null && dialogAddURL.isShowing()) {
            dialogAddURL.dismiss();
        }
    }

    public class AddEditActivityHandler {
        public void onColorSelected(View view) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(activityAddEditBinding.bottomSheet.getRoot());
            ImageView imageView = activityAddEditBinding.noteBackgroundImageView;

            switch (view.getId()) {
                case R.id.color_1:
                    checkUncheckColors("color1");
                    currentNote.setNoteBackground(R.color.colorNoteDefaultColor);
                    imageView.setImageResource(R.color.colorNoteDefaultColor);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_2:
                    checkUncheckColors("color2");
                    currentNote.setNoteBackground(R.color.color_2_White);
                    imageView.setImageResource(R.color.color_2_White);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_3:
                    checkUncheckColors("color3");
                    currentNote.setNoteBackground(R.color.color_3_light_gray);
                    imageView.setImageResource(R.color.color_3_light_gray);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_4:
                    checkUncheckColors("color4");
                    currentNote.setNoteBackground(R.color.color_4_dark_gray);
                    imageView.setImageResource(R.color.color_4_dark_gray);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_5:
                    checkUncheckColors("color5");
                    currentNote.setNoteBackground(R.color.color_5_dark_orange);
                    imageView.setImageResource(R.color.color_5_dark_orange);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

                case R.id.color_6:
                    checkUncheckColors("color6");
                    currentNote.setNoteBackground(R.color.color_6_dark_red);
                    imageView.setImageResource(R.color.color_6_dark_red);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

                case R.id.color_7:
                    checkUncheckColors("color7");
                    currentNote.setNoteBackground(R.color.color_7_dark_purple);
                    imageView.setImageResource(R.color.color_7_dark_purple);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

                case R.id.color_8:
                    checkUncheckColors("color8");
                    currentNote.setNoteBackground(R.color.color_8_dark_blue);
                    imageView.setImageResource(R.color.color_8_dark_blue);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_9:
                    checkUncheckColors("color9");
                    currentNote.setNoteBackground(R.color.color_9_sky_blue);
                    imageView.setImageResource(R.color.color_9_sky_blue);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

                case R.id.color_10:
                    checkUncheckColors("color10");
                    currentNote.setNoteBackground(R.color.color_10_forest_green);
                    imageView.setImageResource(R.color.color_10_forest_green);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_11:
                    checkUncheckColors("color11");
                    currentNote.setNoteBackground(R.color.color_11_yellow);
                    imageView.setImageResource(R.color.color_11_yellow);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.color_12:
                    checkUncheckColors("color12");
                    currentNote.setNoteBackground(R.color.color_12_orange);
                    imageView.setImageResource(R.color.color_12_orange);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

            }


        }

        public void onBackgroundImageSelected(View view) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(activityAddEditBinding.bottomSheet.getRoot());
            ImageView imageView = activityAddEditBinding.noteBackgroundImageView;
            switch (view.getId()) {
                case R.id.bg_image_1:
                    currentNote.setNoteBackground(R.drawable.bg2);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg2);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_2:
                    currentNote.setNoteBackground(R.drawable.bg3);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg3);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_3:
                    currentNote.setNoteBackground(R.drawable.bg4);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg4);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_4:
                    currentNote.setNoteBackground(R.drawable.bg5);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg5);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_5:
                    currentNote.setNoteBackground(R.drawable.bg6);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg6);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_6:
                    currentNote.setNoteBackground(R.drawable.bg7);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg7);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_7:
                    currentNote.setNoteBackground(R.drawable.bg8);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg8);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_8:
                    currentNote.setNoteBackground(R.drawable.bg9);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg9);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_9:
                    currentNote.setNoteBackground(R.drawable.bg10);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg10);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case R.id.bg_image_10:
                    currentNote.setNoteBackground(R.drawable.bg11);
                    checkUncheckColors("");
                    imageView.setImageResource(R.drawable.bg11);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;

            }


        }


        private void checkUncheckColors(String id_to_be_checked) {

            for (int i = 1; i < 12; i++) {
                String imageView_id = "color_" + i + "_check";
                if (id_to_be_checked.equals("color" + i))
                    activityAddEditBinding.bottomSheet.getRoot().findViewById(getResources().getIdentifier(imageView_id, "id", getPackageName())).setVisibility(View.VISIBLE);
                else
                    activityAddEditBinding.bottomSheet.getRoot().findViewById(getResources().getIdentifier(imageView_id, "id", getPackageName())).setVisibility(View.GONE);
            }

        }

        public void pinUnpinNote(View view) {
            ImageView imageView = (ImageView) view;
            if (currentNote.isPinned()) {
                currentNote.setPinned(false);
                imageView.setImageResource(R.drawable.pin_unfilled);
            } else {
                currentNote.setPinned(true);
                imageView.setImageResource(R.drawable.pin_filled);
            }
        }

        public void onBackClick(View view) {
            onBackPressed();
        }

        public void onSaveClick(View view) {

            if (currentNote.getTitle() == null || currentNote.getTitle().isEmpty()) {
                Toast.makeText(AddEditActivity.this, "Note Title is Empty", Toast.LENGTH_SHORT).show();
                return;
            }

            MarkdownEditText descriptionEditText = activityAddEditBinding.noteDescriptionMarkdownEditText;
            currentNote.setDescription(descriptionEditText.getMD());

            if (currentNote.getNoteBackground() == 0) {
                currentNote.setNoteBackground(R.color.colorNoteDefaultColor);
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateAndTime = sdf.format(new Date());
            currentNote.setLastUpdateDateTime(currentDateAndTime);

            if (isEditMode) {

                if (previousRemainder != null) {
                    Log.d("TAG", "onSaveClick: log1");
                    addEditActivityViewModel.updateRemainder(currentRemainder);
                    Log.d("TAG", "onSaveClick: log2");
                    setAlarm(currentRemainder, calendar);
                    Log.d("TAG", "onSaveClick: log3");
                    currentNote.setRemainder_id(previousRemainder.getId());
                    Log.d("TAG", "onSaveClick: log4");
                } else if(currentRemainder != null){
                    Log.d("TAG", "onSaveClick: log5");
                    addEditActivityViewModel.insertRemainder(currentRemainder);
                    Log.d("TAG", "onSaveClick: log6");
                    setAlarm(currentRemainder, calendar);
                    Log.d("TAG", "onSaveClick: log7");
                    int lastRemainderId = addEditActivityViewModel.getLastInsertedIRemainderId();
                    Log.d("TAG", "onSaveClick: log8");
                    currentNote.setRemainder_id(lastRemainderId);
                    Log.d("TAG", "onSaveClick: log9");
                }
                Log.d("TAG", "onSaveClick: log10");
                addEditActivityViewModel.updateNote(currentNote);
                Log.d("TAG", "onSaveClick: log11");

                Toast.makeText(AddEditActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent().putExtra(NOTE_UPDATED, true));
                Log.d("TAG", "onSaveClick: log11");
                finish();

            } else {
                Log.d("TAG", "onSaveClick: log12");
                if (currentRemainder != null) {
                    Log.d("TAG", "onSaveClick: log13");
                    addEditActivityViewModel.insertRemainder(currentRemainder);
                    Log.d("TAG", "onSaveClick: log14");
                    setAlarm(currentRemainder, calendar);
                    Log.d("TAG", "onSaveClick: log15");
                    int lastRemainderId = addEditActivityViewModel.getLastInsertedIRemainderId();
                    Log.d("TAG", "onSaveClick: log16");
                    currentNote.setRemainder_id(lastRemainderId);
                    Log.d("TAG", "onSaveClick: log17");
                }
                Log.d("TAG", "onSaveClick: log18");
                addEditActivityViewModel.insertNote(currentNote);
                Log.d("TAG", "onSaveClick: log19");
                Toast.makeText(AddEditActivity.this, "Note added Successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent().putExtra(NOTE_ADDED, true));
                Log.d("TAG", "onSaveClick: log20");
                finish();
            }


        }


    }


}