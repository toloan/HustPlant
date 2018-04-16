package com.example.bach0.hustplant.database.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Point;

/** Created by bach0 on 4/16/2018. */
@Entity(tableName = "water")
public class Water {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Embedded private Point position;

    public Water(int id, Point position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
