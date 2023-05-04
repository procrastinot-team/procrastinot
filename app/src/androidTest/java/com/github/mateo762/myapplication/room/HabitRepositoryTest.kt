package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HabitRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var database: ApplicationDatabase

    // Inject the production database dependency
    @Inject
    lateinit var productionDatabase: ApplicationDatabase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Before
    fun setupDatabase() {
        // Create a new test database instance
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testDatabase = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()

        // Replace the production database instance with the test database instance
        habitRepository = HabitRepository(testDatabase.getHabitDao())
        productionDatabase.close()
        database = testDatabase
    }

    @After
    fun closeDatabase() {
        // Close the test database after the test
        database.close()
    }

    @Test
    fun testHabitsCache() = runBlocking {
        val dayListA = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        val dayListB = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY)
        val habits = listOf(
            HabitEntity("habit_id_0", "test_habit_0", dayListA, "9:00", "12:00"),
            HabitEntity("habit_id_1", "test_habit_1", dayListB, "15:00", "17:00"),
        )
        habitRepository.insertAllHabits(habits)
        val cachedHabits = habitRepository.getAllHabits()
        assertEquals(2, cachedHabits.size)
        assertEquals(habits[0], cachedHabits[0])
        assertEquals(habits[1], cachedHabits[1])
    }
}
