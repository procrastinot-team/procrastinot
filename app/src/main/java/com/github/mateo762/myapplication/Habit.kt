package com.github.mateo762.myapplication

import java.time.DayOfWeek

data class Habit(
    var name: String,
    var days: ArrayList<DayOfWeek>,
    var startTime: String,
    var endTime: String
)
