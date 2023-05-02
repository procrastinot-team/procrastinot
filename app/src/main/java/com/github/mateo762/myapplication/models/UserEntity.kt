package com.github.mateo762.myapplication.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mateo762.myapplication.room.HabitImageEntity
import com.google.firebase.database.IgnoreExtraProperties

@Entity
@IgnoreExtraProperties
data class UserEntity(
    @PrimaryKey val uid: String = "",
    @ColumnInfo var name: String? = null,
    @ColumnInfo var username: String? = null,
    @ColumnInfo var email: String? = null,
    @ColumnInfo(name = "habitsPath") var habitList: List<HabitEntity>? = null,
    @ColumnInfo(name = "imagesPath") var imagesList: List<HabitImageEntity> = listOf(),
    @ColumnInfo(name = "followingPath") var followingUsers: List<String> = listOf(),
    @ColumnInfo(name = "followersPath") var followerUsers: List<String> = listOf()
)
