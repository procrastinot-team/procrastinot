package com.github.mateo762.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
fun getHardCodedHabits(): List<Habit> {

    return listOf(
        Habit(
            name = "Stretch",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "07:15"
        ),
        Habit(
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "09:00"
        ),
        Habit(
            name = "Exercise",
            days = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        Habit(
            name = "Walk",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "06:00",
            endTime = "07:00"
        ),
        Habit(
            name = "Study",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "20:00",
            endTime = "22:00"
        ),
        Habit(
            name = "Ride bike",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
            ),
            startTime = "14:25",
            endTime = "16:00"
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

@RequiresApi(Build.VERSION_CODES.O)
fun getOneHabit(): List<Habit>{
    return listOf(Habit(
        name = "Stretch",
        days = arrayListOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ),
        startTime = "14:00",
        endTime = "15:30"
    ))
}