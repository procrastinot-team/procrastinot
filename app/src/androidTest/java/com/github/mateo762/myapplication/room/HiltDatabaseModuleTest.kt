package com.github.mateo762.myapplication.room

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HiltDatabaseModuleTest {
    @Test
    fun testProvideHabitDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val injectedDatabase = Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "injected_database"
        ).build()

        assertNotNull(injectedDatabase)
    }
} 