package com.example.bach0.hustplant.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by bach0 on 4/15/2018.
 */
@Entity(foreignKeys = {@ForeignKey(entity = Person.class, parentColumns = "id", childColumns =
        "person_id"), @ForeignKey(entity = Plant.class, parentColumns = "id", childColumns =
        "plant_id")},
        tableName = "water_history")
public class WaterHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "person_id")
    private int personId;

    @ColumnInfo(name = "plant_id")
    private int plantId;

    @ColumnInfo(name = "water_amount")
    private float waterAmount;

    @ColumnInfo(name = "date")
    private Date date;

    public WaterHistory(int id, int personId, int plantId, float waterAmount, Date date) {
        this.id = id;
        this.personId = personId;
        this.plantId = plantId;
        this.waterAmount = waterAmount;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
