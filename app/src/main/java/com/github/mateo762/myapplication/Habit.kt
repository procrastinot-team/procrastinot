package com.github.mateo762.myapplication

import java.time.DayOfWeek

data class Habit(
    var habitName: String,
    var habitDays: ArrayList<DayOfWeek>,
    var habitStartTime: String,
    var habitEndTime: String
)
