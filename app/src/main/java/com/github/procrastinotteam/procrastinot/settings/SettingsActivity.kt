package com.github.procrastinotteam.procrastinot


import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.github.mateo762.myapplication.util.showToast
import com.github.procrastinotteam.procrastinot.notifications.HabitNotificationService


class SettingsActivity : BaseActivity() {
    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 101
    }

    private lateinit var notificationService: HabitNotificationService
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupToolbar()

        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationService =
            HabitNotificationService(this.applicationContext, notificationManager)

        val notificationButton = findViewById<Button>(R.id.notificationButton)
        val permissionButton = findViewById<Button>(R.id.permissionButton)

        notificationButton.setOnClickListener {
            onNotificationButtonClicked(this)
        }

        permissionButton.setOnClickListener {
            onPermissionButtonClicked(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    notificationService.displayNotification()
                } else {
                    //no-op
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun onNotificationButtonClicked(context: Context) {
        if (notificationManager.areNotificationsEnabled()) {
            notificationService.displayNotification()
        } else {
            context.showToast(R.string.notification_permission_not_granted)
        }
    }

    private fun onPermissionButtonClicked(context: Context) {
        if (this.checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            context.showToast(R.string.notification_permission_granted)
        } else {
            this.requestPermissions(
                arrayOf(POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}