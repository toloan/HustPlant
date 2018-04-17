package com.example.bach0.hustplant.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/** Created by bach0 on 4/15/2018. */
public class Converters {
  @TypeConverter
  public static Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public static Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}
