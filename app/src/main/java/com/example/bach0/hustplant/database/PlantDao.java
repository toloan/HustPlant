package com.example.bach0.hustplant.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.bach0.hustplant.database.entity.Plant;

import java.util.List;

/**
 * Created by bach0 on 4/15/2018.
 */

@Dao
public interface PlantDao {
    @Query("SELECT * FROM plant")
    List<Plant> getAll();

    @Query("SELECT * FROM plant WHERE id IN (:plantIds)")
    List<Plant> loadAllByIds(int[] plantIds);

    @Query("SELECT * FROM plant WHERE name LIKE :name")
    List<Plant> findByName(String name);

    @Insert
    void insertAll(Plant... plants);

    @Delete
    void delete(Plant plant);
}