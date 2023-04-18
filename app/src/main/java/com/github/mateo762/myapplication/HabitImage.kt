package com.github.mateo762.myapplication

data class HabitImage(
    val habitId: String,
    val url: String,
    val date: String
){
    constructor() : this("", "", "")
}