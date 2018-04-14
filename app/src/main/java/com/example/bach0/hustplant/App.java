package com.example.bach0.hustplant;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.bach0.hustplant.database.AppDatabase;
import com.example.bach0.hustplant.database.Initializer;

/**
 * Created by bach0 on 4/15/2018.
 */

public class App extends Application {
    private static final String DATABASE_NAME = "hust_plant";
    private static App INSTANCE;
    private AppDatabase mDatabase;

    static public App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,
                DATABASE_NAME).addCallback(new Initializer()).build();
        INSTANCE = this;
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }
}
