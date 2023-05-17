package com.github.mateo762.myapplication.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class HabitAlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HabitAlarmReceiver", "Alarm received!")
        val habitId = intent.getStringExtra("habit_id")
        val habitName = intent.getStringExtra("habit_name")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationService = HabitNotificationService(context, notificationManager)

        if (habitName != null) {
            notificationService.displayNotification(habitName)
        }
    }
}

