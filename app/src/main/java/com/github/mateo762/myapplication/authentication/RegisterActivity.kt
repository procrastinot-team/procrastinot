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
            showToastMessage(R.string.error_empty_register)
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val db: DatabaseReference = Firebase.database.reference

                        val user = FirebaseAuth.getInstance().currentUser

                        val uid = user?.uid
                        if (uid == null) {
                            showToastMessage(R.string.user_data_error)
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
                                    showToastMessage(R.string.success_register)
                                    val intent = Intent(this, UsernameActivity.EntryPoint::class.java)
                                    startActivity(intent)
                                }.addOnFailureListener {
                                    showToastMessage(R.string.try_again_error)
                                }
                        }
                    } else {
                        showToastMessage(task.exception!!.message)
                    }
                }
        }
    }

    private fun showToastMessage(resId:Int) {
        Toast.makeText(baseContext, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToastMessage(message:String?) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}