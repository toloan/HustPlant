package com.example.bach0.hustwater_history.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.bach0.hustplant.database.entity.WaterHistory;

import java.util.List;

/**
 * Created by bach0 on 4/15/2018.
 */

@Dao
public interface WaterHistoryDao {
    @Query("SELECT * FROM water_history")
    List<WaterHistory> getAll();

    @Query("SELECT * FROM water_history WHERE id IN (:waterHistoryIds)")
    List<WaterHistory> loadAllByIds(int[] waterHistoryIds);

    @Query("SELECT * FROM water_history WHERE person_id = :personId")
    List<WaterHistory> findByPerson(int personId);

    @Query("SELECT * FROM water_history WHERE person_id = :plantId")
    List<WaterHistory> findByPlant(int plantId);

    @Insert
    void insertAll(WaterHistory... persons);

    @Delete
    void delete(WaterHistory person);
}