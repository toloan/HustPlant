package com.example.bach0.hustplant.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.bach0.hustplant.database.entity.Person;
import com.example.bach0.hustplant.database.entity.Plant;
import com.example.bach0.hustplant.database.entity.Water;
import com.example.bach0.hustplant.database.entity.WaterHistory;

/** Created by bach0 on 4/15/2018. */
@Database(
  entities = {Plant.class, Person.class, WaterHistory.class, Water.class},
  version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
  public abstract PlantDao plantDao();

  public abstract PersonDao personDao();

  public abstract WaterHistoryDao waterHistoryDao();

  public abstract WaterDao waterDao();
}
