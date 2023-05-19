package com.github.mateo762.myapplication.notifications

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.databinding.ActivityNotificationInfoBinding
import com.github.mateo762.myapplication.home.HomeActivity.HomeEntryPoint
import com.github.mateo762.myapplication.settings.SettingsActivity

/**
 * Activity for the notification info and activation of notification.
 */
class NotificationInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationInfoBinding

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.negativeButton.setOnClickListener {
            navigationToHomeActivity()
        }

        binding.positiveButton.setOnClickListener {
            this.requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SettingsActivity.NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                navigationToHomeActivity()
            }
        }
    }

    private fun navigationToHomeActivity() {
        val intent = Intent(this, HomeEntryPoint::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}