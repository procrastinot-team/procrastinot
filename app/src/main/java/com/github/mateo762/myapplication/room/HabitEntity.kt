package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class HabitEntity(
    @PrimaryKey val hid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "active_days") val activeDays: List<String>?,
    @ColumnInfo(name = "start_hour") val startHour: String?,
    @ColumnInfo(name = "end_hour") val endHour: String?
    // Could additionally add new feature such as labels
    // For now, day/time will be handled as strings, like in the Firebase form
    // instead of SQL DataTypes
    )