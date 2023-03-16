package com.github.procrastinotteam.procrastinot

import java.time.DayOfWeek

data class Habit(
    var habitName: String,
    var habitDays: ArrayList<DayOfWeek>,
    var habitStartTime: String,
    var habitEndTime: String
)
