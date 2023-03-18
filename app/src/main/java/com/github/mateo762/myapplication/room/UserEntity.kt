package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UserEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "friend_list") val friendList: List<UserEntity>?,
    @ColumnInfo(name = "habit_list") val habitList: List<HabitEntity>?
)
