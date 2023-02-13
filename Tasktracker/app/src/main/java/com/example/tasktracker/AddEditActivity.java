package com.example.tasktracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tasktracker.databinding.ActivityAddEditBinding;
import com.example.tasktracker.entities.Note;
import com.example.tasktracker.viewModel.AddEditActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AddEditActivity extends AppCompatActivity {

    private Context context;
    private AddEditActivityViewModel addEditActivityViewModel;
    private ActivityAddEditBinding activityAddEditBinding;
    private boolean isEditMode;
    private Note currentNote;
    private AddEditActivityHandler addEditActivityHandler;

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


        if (!isEditMode) {
            currentNote = new Note();
            addEditActivityViewModel.setNote(currentNote);
            initBottomSheet();


        }

    }

    private void initBottomSheet() {
        final LinearLayout layoutBottomSheet = activityAddEditBinding.getRoot().findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior =  BottomSheetBehavior.from(layoutBottomSheet);
        layoutBottomSheet.findViewById(R.id.bottom_sheet_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    public  class AddEditActivityHandler {
        public void onColorSelected(View view) {
            switch (view.getId()){
                case R.id.color_1:
                    checkUncheckColors("color1");
                    currentNote.setNoteBackground(R.color.colorNoteDefaultColor);
                    break;
                case R.id.color_2:
                    checkUncheckColors("color2");
                    currentNote.setNoteBackground(R.color.color_2_White);
                    break;
                case R.id.color_3:
                    checkUncheckColors("color3");
                    currentNote.setNoteBackground(R.color.color_3_light_gray);
                    break;
                case R.id.color_4:
                    checkUncheckColors("color4");
                    currentNote.setNoteBackground(R.color.color_4_dark_gray);
                    break;
                case R.id.color_5:
                    checkUncheckColors("color5");
                    currentNote.setNoteBackground(R.color.color_5_dark_orange);
                    break;

                case R.id.color_6:
                    checkUncheckColors("color6");
                    currentNote.setNoteBackground(R.color.color_6_dark_red);
                    break;

                case R.id.color_7:
                    checkUncheckColors("color7");
                    currentNote.setNoteBackground(R.color.color_7_dark_purple);
                    break;

                case R.id.color_8:
                    checkUncheckColors("color8");
                    currentNote.setNoteBackground(R.color.color_8_dark_blue);
                    break;
                case R.id.color_9:
                    checkUncheckColors("color9");
                    currentNote.setNoteBackground(R.color.color_9_sky_blue);
                    break;

                case R.id.color_10:
                    checkUncheckColors("color10");
                    currentNote.setNoteBackground(R.color.color_10_forest_green);
                    break;
                case R.id.color_11:
                    checkUncheckColors("color11");
                    currentNote.setNoteBackground(R.color.color_11_yellow);
                    break;
                case R.id.color_12:
                    checkUncheckColors("color12");
                    currentNote.setNoteBackground(R.color.color_12_orange);
                    break;

            }


        }

        public void onBackgroundImageSelected(View view){
            switch (view.getId()){
                case R.id.bg_image_1:
                    currentNote.setNoteBackground(R.drawable.bg2);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_2:
                    currentNote.setNoteBackground(R.drawable.bg3);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_3:
                    currentNote.setNoteBackground(R.drawable.bg4);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_4:
                    currentNote.setNoteBackground(R.drawable.bg5);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_5:
                    currentNote.setNoteBackground(R.drawable.bg6);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_6:
                    currentNote.setNoteBackground(R.drawable.bg7);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_7:
                    currentNote.setNoteBackground(R.drawable.bg8);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_8:
                    currentNote.setNoteBackground(R.drawable.bg9);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_9:
                    currentNote.setNoteBackground(R.drawable.bg10);
                    checkUncheckColors("");
                    break;
                case R.id.bg_image_10:
                    currentNote.setNoteBackground(R.drawable.bg11);
                    checkUncheckColors("");
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


    }
}