package com.example.bach0.hustplant.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by bach0 on 4/15/2018.
 */
@Entity(foreignKeys = {@ForeignKey(entity = Person.class, parentColumns = "id", childColumns =
        "person_id"), @ForeignKey(entity = Plant.class, parentColumns = "id", childColumns =
        "plant_id")},
        tableName = "water_history")
public class WaterHistory {
    @ColumnInfo(name = "person_id")
    private int personId;
    @ColumnInfo(name = "plant_id")
    private int plantId;
    @PrimaryKey(autoGenerate = true)

    private int id;
    @ColumnInfo(name = "water_amount")
    private float waterAmount;

    public WaterHistory(int personId, int plantId, float waterAmount) {
        this.personId = personId;
        this.plantId = plantId;
        this.waterAmount = waterAmount;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(float waterAmount) {
        this.waterAmount = waterAmount;
    }

}
