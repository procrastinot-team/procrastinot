package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "username") var username: String?,
    @ColumnInfo(name = "habit_list") val habitList: List<HabitEntity>?
)
