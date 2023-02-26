package com.github.mateo762.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityActivity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "activity") val activity: String?,
)
