package com.example.tasktracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tasktracker.databinding.ActivityAddEditBinding;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.viewModel.AddEditActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textview.MaterialTextView;
import com.yahiaangelo.markdownedittext.MarkdownEditText;
import com.yahiaangelo.markdownedittext.MarkdownStylesBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;

public class AddEditActivity extends AppCompatActivity {

    private Context context;
    private AddEditActivityViewModel addEditActivityViewModel;
    private ActivityAddEditBinding activityAddEditBinding;
    private boolean isEditMode;
    private Note currentNote;
    private AddEditActivityHandler addEditActivityHandler;

    private AlertDialog dialogAddImage;
    private AlertDialog dialogAddURL;

    private static final int  REQUEST_CODE_STORAGE_PERMISSION = 1;
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


        context = getBaseContext();
        isEditMode = getIntent().getBooleanExtra(MainActivity.IS_EDIT_MODE, false);

        initMarkdownEditText();
        activityAddEditBinding.addImageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }

            showAddImageDialog();
            }
        });

        activityAddEditBinding.noteImageImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
                showAddImageDialog();
            }
        });

        activityAddEditBinding.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
                activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.GONE);
                activityAddEditBinding.addImageLinearLayout.setVisibility(View.VISIBLE);
                currentNote.setNoteImage("");
            }
        });

        activityAddEditBinding.addUrlLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
                showAddURLDialog();
            }
        });

        activityAddEditBinding.addUrlImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
                showAddURLDialog();
            }
        });

        activityAddEditBinding.delete2ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // At every click on this button, delete the last markdown string [title](url) from currentNote.getMarkdownLinkText()
                // and update the markdownLinkText in currentNote
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
                String markdownLinkText = currentNote.getMarkdownLinkText();

                int lastIndex = markdownLinkText.lastIndexOf(") [");
                if (lastIndex == -1) {
                    markdownLinkText = "";
                    currentNote.setMarkdownLinkText(markdownLinkText);
                }
                else{
                    markdownLinkText = markdownLinkText.substring(0, lastIndex+1).trim();
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
                if (activityAddEditBinding.markdownStylesBarLinearLayout.getVisibility() == View.VISIBLE) {
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                    activityAddEditBinding.noteDescriptionMarkdownEditText.setCursorVisible(false);
                }
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
                activityAddEditBinding.noteImageImageview.setImageURI(Uri.parse(currentNote.getNoteImage()));
                activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
            }
            if (currentNote.getDescription() != null){
                activityAddEditBinding.noteDescriptionMarkdownEditText.renderMD(currentNote.getDescription());
            } else {
                activityAddEditBinding.noteDescriptionMarkdownEditText.setText("");
            }

            if (currentNote.getMarkdownLinkText() != null){
                activityAddEditBinding.addUrlLinearLayout.setVisibility(View.GONE);
                displayUrl();
                activityAddEditBinding.addUrlRelativeLayout.setVisibility(View.VISIBLE);
            }

            setPinImage();
        }
        addEditActivityViewModel.setNote(currentNote);

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
        }
        else
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
                if(hasFocus){
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    activityAddEditBinding.markdownStylesBarLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddImageDialog(){
        if (dialogAddImage == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_image, (ViewGroup) findViewById(R.id.layout_add_image_options), false);
            builder.setView(view);
            dialogAddImage = builder.create();
            if (dialogAddImage.getWindow() != null){
                dialogAddImage.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.dialog_choose_from_gallery_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddImage.dismiss();
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                    }
                    else {
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
                    }
                    else {
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
    private void showAddURLDialog(){
        if (dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layout_add_url_container), false);
            builder.setView(view);
            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.dialog_add_option_textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                    String url = ((EditText) Objects.requireNonNull(dialogAddURL.findViewById(R.id.dialog_add_link_editText))).getText().toString();
                    String title = ((EditText) Objects.requireNonNull(dialogAddURL.findViewById(R.id.dialog_add_title_editText))).getText().toString();

                    if (url.isEmpty()){
                        Toast.makeText(AddEditActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Create markdown text for link having title and store it as String in note.markDown
                        title = title.isEmpty() ? url : title;
                        String markdownText = "[" + title + "](" + url + ")";
                        if (currentNote.getMarkdownLinkText() == null){
                            currentNote.setMarkdownLinkText(markdownText);
                        }
                        else {
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
            Toast.makeText(this, ""+currentPhotoPath, Toast.LENGTH_SHORT).show();
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
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        activityAddEditBinding.noteImageImageview.setImageBitmap(bitmap);
                        activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
                        activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
                        currentNote.setNoteImage(getPathFromUri(selectedImageUri));
                        Toast.makeText(this, ""+currentNote.getNoteImage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (requestCode == REQUEST_CODE_TAKE_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
            activityAddEditBinding.noteImageImageview.setImageURI(Uri.parse(currentNote.getNoteImage()));
            activityAddEditBinding.noteImageConstraintLayout.setVisibility(View.VISIBLE);
            activityAddEditBinding.addImageLinearLayout.setVisibility(View.GONE);
        }
    }

    public  class AddEditActivityHandler {
        public void onColorSelected(View view) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(activityAddEditBinding.bottomSheet.getRoot());
            ImageView imageView = activityAddEditBinding.noteBackgroundImageView;

            switch (view.getId()){
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

        public void onBackgroundImageSelected(View view){
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(activityAddEditBinding.bottomSheet.getRoot());
            ImageView imageView = activityAddEditBinding.noteBackgroundImageView;
            switch (view.getId()){
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
        public  void pinUnpinNote(View view){
            ImageView imageView = (ImageView) view;
            if (currentNote.isPinned()){
                currentNote.setPinned(false);
                imageView.setImageResource(R.drawable.pin_unfilled);
            }
            else{
                currentNote.setPinned(true);
                imageView.setImageResource(R.drawable.pin_filled);
            }
        }

        public void onBackClick(View view){
            onBackPressed();
        }
        public void onSaveClick(View view){

            if (currentNote.getTitle() == null || currentNote.getTitle().isEmpty()){
                Toast.makeText(AddEditActivity.this, "Note Title is Empty", Toast.LENGTH_SHORT).show();
                return;
            }

                MarkdownEditText descriptionEditText = activityAddEditBinding.noteDescriptionMarkdownEditText;
                currentNote.setDescription(descriptionEditText.getMD());

            if (currentNote.getNoteBackground() == 0){
                currentNote.setNoteBackground(R.color.colorNoteDefaultColor);
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            currentNote.setLastUpdateDateTime(currentDateandTime);

            if (isEditMode) {
                addEditActivityViewModel.updateNote(currentNote);
                Toast.makeText(AddEditActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                // Return to MainActivity with flag Note_Updated
                setResult(RESULT_OK, new Intent().putExtra(NOTE_UPDATED, true));
                finish();

            }
            else {
                addEditActivityViewModel.insertNote(currentNote);
                Toast.makeText(AddEditActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                // Return to MainActivity with flag Note_Added
                setResult(RESULT_OK, new Intent().putExtra(NOTE_ADDED, true));
                finish();
            }



        }



    }
}