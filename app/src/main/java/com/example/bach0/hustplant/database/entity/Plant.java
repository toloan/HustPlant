package com.example.bach0.hustplant.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Point;

/**
 * Created by bach0 on 4/15/2018.
 */

@Entity(tableName = "plant")
public class Plant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Embedded
    private Point position;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "water_level")
    private float waterLevel;
    @ColumnInfo(name = "target_water_level")
    private float targetWaterLevel;
    @ColumnInfo(name = "resource_id")
    private int resourceId;

    public Plant(int id, Point position, String name, String description, float waterLevel, float
            targetWaterLevel, int resourceId) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.description = description;
        this.waterLevel = waterLevel;
        this.targetWaterLevel = targetWaterLevel;
        this.resourceId = resourceId;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    public float getTargetWaterLevel() {
        return targetWaterLevel;
    }

    public void setTargetWaterLevel(float targetWaterLevel) {
        this.targetWaterLevel = targetWaterLevel;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
