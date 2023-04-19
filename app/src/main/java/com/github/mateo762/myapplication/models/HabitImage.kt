package com.github.mateo762.myapplication.models

data class HabitImage(
    val habitId: String,
    val url: String,
    val date: String
){
    constructor() : this("", "", "")
}