package com.github.mateo762.myapplication.habits.fragments.week

import android.graphics.Color
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


private var colorIndex = 0

@RequiresApi(Build.VERSION_CODES.O)
fun habitToWeekViewEvent(habit: Habit, eventId: Long, color: Int): List<WeekViewEvent> {
    val events = mutableListOf<WeekViewEvent>()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startTime = LocalTime.parse(habit.startTime, timeFormatter)
    val endTime = LocalTime.parse(habit.endTime, timeFormatter).minusMinutes(1)

    val now = LocalDateTime.now()
    val startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    for (dayOfWeek in habit.days) {
        var localDate = now.with(TemporalAdjusters.previousOrSame(dayOfWeek))

        while (localDate.isBefore(endOfWeek) || localDate.isEqual(endOfWeek)) {
            if (localDate.isAfter(startOfWeek) || localDate.isEqual(startOfWeek)) {
                val startCalendar = Calendar.getInstance()
                val endCalendar = startCalendar.clone() as Calendar

                startCalendar.time = Date.from(
                    localDate.withHour(startTime.hour).withMinute(startTime.minute)
                        .withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant()
                )
                endCalendar.time = Date.from(
                    localDate.withHour(endTime.hour).withMinute(endTime.minute)
                        .atZone(ZoneId.systemDefault()).toInstant()
                )

                val event = WeekViewEvent(eventId, habit.name, startCalendar, endCalendar)
                event.color = color
                events.add(event)
            }

            localDate = localDate.with(TemporalAdjusters.next(dayOfWeek))
        }
    }

    return events
}
