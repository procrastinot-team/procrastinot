package com.github.mateo762.myapplication.post

import com.github.mateo762.myapplication.models.HabitImage

data class Post(
    val caption: String,
    val description: String,
    val username: String,
    val assocHabit: String,
    val habitImage: HabitImage
)