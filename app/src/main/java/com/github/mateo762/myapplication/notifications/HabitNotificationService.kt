package com.github.mateo762.myapplication.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import java.util.concurrent.atomic.AtomicInteger

/**
 * Service for habit notifications.
 */
class HabitNotificationService(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    private var notificationIdCounter: AtomicInteger = AtomicInteger(1)

    companion object {
        const val HABIT_CHANNEL_ID = "habit_channel"
        const val PENDING_INTENT_REQUEST_CODE = 13
    }

    init {
        createNotificationChannel()
    }

    /**
     * Method that displays a notification.
     */
    fun displayNotification(habitName: String) {
        val activityIntent = Intent(context, TakePhotoActivity::class.java)
        val pendingActivityIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, HABIT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText("Have you completed $habitName? Take a picture quick")
            .setContentIntent(pendingActivityIntent)
            .build()

        notificationManager.notify(notificationIdCounter.getAndIncrement(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                HABIT_CHANNEL_ID,
                context.getString(R.string.habit_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = context.getString(R.string.habit_notification_channel_description)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}