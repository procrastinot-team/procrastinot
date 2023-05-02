package com.github.mateo762.myapplication.week

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.habits.fragments.week.habitToWeekViewEvent
import com.github.mateo762.myapplication.models.HabitEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

@RunWith(AndroidJUnit4::class)
class HabitToWeekEventTest {
    @Test
    fun testHabitToWeekViewEvent() {
        val habit = HabitEntity(
            name = "Test Habit",
            startTime = "08:00",
            endTime = "09:00",
            days = listOf(DayOfWeek.MONDAY)
        )

        val events = habitToWeekViewEvent(habit, 1, Color.BLUE, LocalDateTime.of(2023, 4, 15, 15, 0))

        assertEquals(1, events.size)

        val event = events.first()

        assertEquals("Test Habit", event.name)
        assertEquals(1, event.id)
        assertEquals(Color.BLUE, event.color)

        val expectedStartTime = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.APRIL)
            set(Calendar.DAY_OF_MONTH, 17)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val expectedEndTime = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.APRIL)
            set(Calendar.DAY_OF_MONTH, 17)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        assertTrue(event.startTime.get(Calendar.DAY_OF_WEEK) == expectedStartTime.get(Calendar.DAY_OF_WEEK))
        assertTrue(event.startTime.get(Calendar.MONTH) == expectedStartTime.get(Calendar.MONTH))
        assertTrue(event.startTime.get(Calendar.DAY_OF_MONTH) == expectedStartTime.get(Calendar.DAY_OF_MONTH))
        assertTrue(event.startTime.get(Calendar.HOUR_OF_DAY) == expectedStartTime.get(Calendar.HOUR_OF_DAY))
        assertTrue(event.startTime.get(Calendar.MINUTE) == expectedStartTime.get(Calendar.MINUTE))
        assertTrue(event.startTime.get(Calendar.SECOND) == expectedStartTime.get(Calendar.SECOND))

        assertTrue(event.endTime.get(Calendar.DAY_OF_WEEK) == expectedEndTime.get(Calendar.DAY_OF_WEEK))
        assertTrue(event.endTime.get(Calendar.MONTH) == expectedEndTime.get(Calendar.MONTH))
        assertTrue(event.endTime.get(Calendar.DAY_OF_MONTH) == expectedEndTime.get(Calendar.DAY_OF_MONTH))
        assertTrue(event.endTime.get(Calendar.HOUR_OF_DAY) == expectedEndTime.get(Calendar.HOUR_OF_DAY))
        assertTrue(event.endTime.get(Calendar.MINUTE) == expectedEndTime.get(Calendar.MINUTE))
        assertTrue(event.endTime.get(Calendar.SECOND) == expectedEndTime.get(Calendar.SECOND))
    }
}