package com.github.mateo762.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.mateo762.myapplication.notifications.HabitNotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProcrastinotApplication : Application() {

}