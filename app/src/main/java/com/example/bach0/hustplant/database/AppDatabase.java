package com.example.bach0.hustplant.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.bach0.hustplant.database.entity.Person;
import com.example.bach0.hustplant.database.entity.Plant;

/**
 * Created by bach0 on 4/15/2018.
 */

@Database(entities = {Plant.class, Person.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlantDao plantDao();
    public abstract PersonDao personDao();
}