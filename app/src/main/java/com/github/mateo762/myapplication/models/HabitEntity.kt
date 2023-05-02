package com.github.mateo762.myapplication.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity
data class HabitEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "habit_name") val name: String = "",
    @ColumnInfo(name = "habit_days") val days: List<DayOfWeek> = emptyList(),
    @ColumnInfo(name = "habit_start_time") val startTime: String = "",
    @ColumnInfo(name = "habit_end_time") val endTime: String = ""
    // Could additionally add new feature such as labels, but the entity is kept same as Firebase
    // NOTE: Room is not compatible with ArrayLists apparently, so only List<Object> can be used
)