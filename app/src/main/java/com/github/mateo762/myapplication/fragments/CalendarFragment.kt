package com.github.mateo762.myapplication.fragments

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.notifications.HabitNotificationService

class CalendarFragment : Fragment() {

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 101
    }

    private lateinit var notificationService: HabitNotificationService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationService =
                HabitNotificationService(context.applicationContext, notificationManager)

            val notificationButton = view.findViewById<Button>(R.id.notificationButton)
            val permissionButton = view.findViewById<Button>(R.id.permissionButton)

            notificationButton.setOnClickListener {
                if (activity?.checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notificationService.displayNotification()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.notification_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            permissionButton.setOnClickListener {
                if (activity?.checkSelfPermission(POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        context,
                        getString(R.string.notification_permission_granted),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    activity?.requestPermissions(
                        arrayOf(POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
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
}