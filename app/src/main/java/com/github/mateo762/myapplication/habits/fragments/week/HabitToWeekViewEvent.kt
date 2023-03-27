package com.github.mateo762.myapplication.habits.fragments.week

import android.os.Build
import androidx.annotation.RequiresApi
import com.alamkanak.weekview.WeekViewEvent
import com.github.mateo762.myapplication.Habit
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun habitToWeekViewEvents(habit: Habit, eventId: Long): List<WeekViewEvent> {
    val events = mutableListOf<WeekViewEvent>()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startTime = LocalTime.parse(habit.startTime, timeFormatter)
    val endTime = LocalTime.parse(habit.endTime, timeFormatter)

    habit.days.forEach { dayOfWeek ->
        val startCalendar = Calendar.getInstance()
        val endCalendar = startCalendar.clone() as Calendar

        val localDate = LocalDateTime.now()
            .with(TemporalAdjusters.nextOrSame(dayOfWeek))
            .withHour(startTime.hour)
            .withMinute(startTime.minute)
            .withSecond(0)
            .withNano(0)

        startCalendar.time = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
        endCalendar.time = Date.from(localDate.withHour(endTime.hour).withMinute(endTime.minute).atZone(ZoneId.systemDefault()).toInstant())

        val event = WeekViewEvent(eventId, habit.name, startCalendar, endCalendar)
        events.add(event)
    }

    return events
}
