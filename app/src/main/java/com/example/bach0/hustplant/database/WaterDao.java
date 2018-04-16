package com.example.bach0.hustplant.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.bach0.hustplant.database.entity.Water;

import java.util.List;

/** Created by bach0 on 4/16/2018. */
@Dao
public interface WaterDao {
    @Query("SELECT * FROM water")
    List<Water> getAll();

    @Insert
    void insertAll(Water... waters);

    @Delete
    void delete(Water water);

    @Query("SELECT * FROM water WHERE id = :waterId LIMIT 1")
    Water loadById(int waterId);
}
