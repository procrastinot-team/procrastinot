package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity

data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val hid: Int,
    @ColumnInfo(name = "habit_name") val habitName: String?,
    @ColumnInfo(name = "habit_days") val habitDays: List<DayOfWeek>?,
    @ColumnInfo(name = "habit_start_time") val habitStartTime: String?,
    @ColumnInfo(name = "habit_end_time") val habitEndTime: String?
    // Could additionally add new feature such as labels, but the entity is kept same as Firebase
    // NOTE: Room is not compatible with ArrayLists apparently, so only List<Object> can be used
    )