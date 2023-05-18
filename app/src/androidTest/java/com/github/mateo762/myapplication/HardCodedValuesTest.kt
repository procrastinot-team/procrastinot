package com.github.mateo762.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek

class HardCodedValuesTest {

    @Test
    fun getHardCodedImages_returnsCorrectList() {
        val expected = listOf(
            "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149",
            "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149",
            "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149"
        )

        val actual = getHardCodedImages()

        assertEquals(expected, actual)
    }

    @Test
    fun getOneHabit_returnsCorrectHabit() {
        val habit = getOneHabit().first()

        assertEquals("Stretch", habit.name)
        assertEquals(listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ), habit.days)
        assertEquals("14:00", habit.startTime)
        assertEquals("15:30", habit.endTime)
    }
}
