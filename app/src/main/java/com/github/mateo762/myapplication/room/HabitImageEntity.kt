package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HabitImageEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val habitId: String,
    @ColumnInfo var url: String,
    @ColumnInfo var date: String
)