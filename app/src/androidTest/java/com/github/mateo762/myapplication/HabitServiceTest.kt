package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.habits.HabitService
import com.github.mateo762.myapplication.habits.HabitServiceCallback
import com.github.mateo762.myapplication.models.HabitEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek

class HabitServiceTest {

    private lateinit var habitService: HabitService
    private lateinit var addedHabits: MutableList<HabitEntity>

    @Before
    fun setUp() {
        addedHabits = mutableListOf()
        habitService = object : HabitService {
            override fun addHabit(user: String, habit: HabitEntity, callback: HabitServiceCallback) {
                addedHabits.add(habit)
                callback.onSuccess()
            }

            override fun getHabits(user: String, callback: HabitServiceCallback) {
                // Not implemented for testing
            }
        }
    }

    @Test
    fun testAddHabit() {
        val user = "user123"
        val habit = HabitEntity(
            id = "1",
            name = "Exercise",
            days = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            startTime = "08:00",
            endTime = "09:00",
            isCoached = true,
            coachRequested = true,
            coachOffers = listOf("Offer 1", "Offer 2"),
            coach = "Coach 1"
        )

        habitService.addHabit(user, habit, object : HabitServiceCallback {
            override fun onSuccess() {
                // Assert that the habit was added successfully
                assertEquals(1, addedHabits.size)
                assertEquals(habit, addedHabits[0])
            }

            override fun onFailure() {
                // This should not be called in this test
                throw AssertionError("Unexpected onFailure() call")
            }
        })
    }

    // Add more test cases for other methods as needed
}