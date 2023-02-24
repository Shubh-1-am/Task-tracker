package com.example.tasktracker;

import android.app.Application;

public class UtilApplication extends Application {

        private static UtilApplication instance;
        private RemainderDetailsActivity remainderDetailsActivity;
        private AddEditActivity addEditActivity;

        public static UtilApplication getInstance() {
            if (instance == null) {
                instance = new UtilApplication();
            }
            return instance;
        }


    public RemainderDetailsActivity getRemainderDetailsActivity() {
        return remainderDetailsActivity;
    }

    public void setRemainderDetailsActivity(RemainderDetailsActivity remainderDetailsActivity) {
        this.remainderDetailsActivity = remainderDetailsActivity;
    }

    public AddEditActivity getAddEditActivity() {
        return addEditActivity;
    }

    public void setAddEditActivity(AddEditActivity addEditActivity) {
        this.addEditActivity = addEditActivity;
    }
}
