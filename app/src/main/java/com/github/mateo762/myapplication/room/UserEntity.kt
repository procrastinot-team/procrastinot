package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey() val uid: String,
    @ColumnInfo(name = "username") var username: String?,
    @ColumnInfo(name = "habit_list") val habitList: List<HabitEntity>?
)
