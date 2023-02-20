package com.example.tasktracker;

import android.app.Application;

public class UtilApplication extends Application {

        private static UtilApplication instance;
        private RemainderDetailsActivity activity;

        public static UtilApplication getInstance() {
            if (instance == null) {
                instance = new UtilApplication();
            }
            return instance;
        }

    public void setActivity(RemainderDetailsActivity activity) {
        this.activity = activity;
    }

    public RemainderDetailsActivity getActivity() {
        return activity;
    }
}
