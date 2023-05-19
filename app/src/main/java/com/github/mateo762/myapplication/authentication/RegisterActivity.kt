package com.github.mateo762.myapplication.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.authentication.RegisterScreen
import com.github.mateo762.myapplication.username.UsernameActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen(registerUser = ::registerUser)
        }

        // Initialize Firebase auth instance
        auth = Firebase.auth
    }

    private fun registerUser(name:String, surname:String, email: String, password: String) {

        // add null check on text values
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, R.string.error_empty_register, Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val db: DatabaseReference = Firebase.database.reference

                        val user = FirebaseAuth.getInstance().currentUser

                        val uid = user?.uid
                        if (uid == null) {
                            Toast.makeText(baseContext, R.string.user_data_error, Toast.LENGTH_SHORT).show()
                        } else {
                            val displayName = name.plus(" ").plus(surname)
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build()

                            user.updateProfile(profileUpdates)

                            val users: MutableMap<String, UserEntity> = HashMap()
                            val u = UserEntity(uid,displayName, "username", email,ArrayList(), listOf(), listOf(), listOf())
                            users[uid] = u
                            db.child("users").updateChildren(users as Map<String, Any>)
                                .addOnSuccessListener {
                                    PreferenceHelper.setLoggedIn(baseContext, true)
                                    Toast.makeText(baseContext, R.string.success_register, Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, UsernameActivity.EntryPoint::class.java)
                                    startActivity(intent)
                                }.addOnFailureListener {
                                    Toast.makeText(baseContext, R.string.try_again_error, Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}