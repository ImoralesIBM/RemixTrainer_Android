package com.remixtrainer;

import android.app.Application;
import android.content.Intent;

public class RemixTrainerApplication extends Application {
    public static DatabaseLocal mDatabase;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = new DatabaseLocal();
    }
}