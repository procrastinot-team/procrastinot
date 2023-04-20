package com.github.mateo762.myapplication.post

import android.media.Image

data class Post(
    val caption: String,
    val description: String,
    val username: String,
    val assocHabit: String,
    val image: Image? // pending type from other tasks
)