package com.github.mateo762.myapplication.models

import com.github.mateo762.myapplication.room.HabitImageEntity

data class Post(
    val caption: String,
    val description: String,
    val username: String,
    val assocHabit: String,
    val habitImage: HabitImageEntity
)