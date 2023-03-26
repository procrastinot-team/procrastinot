package com.github.mateo762.myapplication.authentication

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.notifications.NotificationInfoActivity
import com.github.mateo762.myapplication.ui.authentication.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen(registerUser = ::registerUser)
        }

        // Initialize Firebase auth instance
        auth = Firebase.auth

        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun registerUser(email: String, password: String) {

        println("1st stage: $email")
        // add null check on text values
        if (email.isEmpty() || password.isEmpty()) {
            println("2nd stage: $email")
            Toast.makeText(baseContext, "No values inserted. Please fill in the email and " +
                    "password to sign up",
                Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Successfully registered!",
                        Toast.LENGTH_SHORT).show()
                        var intent: Intent =
                            if (this.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) || !notificationManager.areNotificationsEnabled()) {
                                Intent(this, NotificationInfoActivity::class.java)
                            } else {
                                Intent(this, HomeActivity::class.java)
                            }
                        startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, task.exception!!.message,
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}