package com.github.mateo762.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
fun getHardCodedHabits(): List<Habit> {
    return listOf(
        Habit(
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "08:30"
        ),
        Habit(
            name = "Drink water",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "09:00",
            endTime = "17:00"
        ),
        Habit(
            name = "Exercise",
            days = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        Habit(
            name = "Meditate",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "07:15"
        ),
        Habit(
            name = "Walk dog",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "20:00",
            endTime = "20:10"
        ),
        Habit(
            name = "Ride bike",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "09:10"
        )
    )
}

fun getHardCodedImages(): Array<Int> {
    return arrayOf(
        R.drawable.ic_new,
        R.drawable.ic_new,
        R.drawable.ic_new
    )
}