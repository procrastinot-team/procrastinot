package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitImageEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HabitImageRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var habitImageRepository: HabitImageRepository

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
        val testDatabase =
            Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()

        // Replace the production database instance with the test database instance
        habitImageRepository = HabitImageRepository(testDatabase.getHabitImageDao())
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
        val habitImages = listOf(
            HabitImageEntity("habit_image_id_0", "habit_id_0", "url_habit_0", "date_habit_0"),
            HabitImageEntity("habit_image_id_1", "habit_id_1", "url_habit_1", "date_habit_1"),
        )
        habitImageRepository.insertAllHabitImages(habitImages)
        val cachedHabitImages = habitImageRepository.getAllHabitImages()
        assertEquals(2, cachedHabitImages.size)
        assertEquals(habitImages[0], cachedHabitImages[0])
        assertEquals(habitImages[1], cachedHabitImages[1])
    }
}
