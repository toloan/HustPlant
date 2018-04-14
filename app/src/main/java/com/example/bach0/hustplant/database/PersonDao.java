package com.example.bach0.hustplant.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.bach0.hustplant.database.entity.Person;

import java.util.List;

/**
 * Created by bach0 on 4/15/2018.
 */

@Dao
public interface PersonDao {
    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Query("SELECT * FROM person WHERE id IN (:personIds)")
    List<Person> loadAllByIds(int[] personIds);

    @Query("SELECT * FROM person WHERE name LIKE :name")
    List<Person> findByName(String name);

    @Insert
    void insertAll(Person... persons);

    @Delete
    void delete(Person person);
}