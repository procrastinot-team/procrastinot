package com.github.mateo762.myapplication.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
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

    /**
     * Method that displays a notification.
     */
    fun displayNotification() {
        val activityIntent = Intent(context, HomeActivity::class.java)
        val pendingActivityIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, HABIT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(context.getString(R.string.notification_content_text))
            .setContentIntent(pendingActivityIntent)
            .build()

        notificationManager.notify(notificationIdCounter.getAndIncrement(), notification)
    }
}