package com.github.mateo762.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class HabitStringDays(
    val name: String,
    val days: List<String>,
    val startTime: String,
    val endTime: String
)

fun habitToStringDays(habit: Habit): HabitStringDays {
    return HabitStringDays(
        name = habit.name,
        days = habit.days.map { it.toString() },
        startTime = habit.startTime,
        endTime = habit.endTime
    )
}