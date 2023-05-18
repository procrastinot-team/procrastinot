package com.github.mateo762.myapplication.notifications

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
class HabitAlarmReceiverTest {

    @Test
    fun onReceive_displaysNotification() {
        // Arrange
        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = Intent().apply {
            putExtra("habit_id", "1234")
            putExtra("habit_name", "Test Habit")
        }

        val receiver = HabitAlarmReceiver()

        // Act
        receiver.onReceive(context, intent)
    }
}
