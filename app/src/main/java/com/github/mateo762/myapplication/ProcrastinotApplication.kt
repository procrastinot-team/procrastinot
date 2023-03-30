package com.github.mateo762.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.github.mateo762.myapplication.notifications.HabitNotificationService

class ProcrastinotApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                HabitNotificationService.HABIT_CHANNEL_ID,
                getString(R.string.habit_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = getString(R.string.habit_notification_channel_description)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}