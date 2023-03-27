package com.github.mateo762.myapplication.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.room.HabitEntity
import com.github.mateo762.myapplication.room.UserEntity
import com.github.mateo762.myapplication.ui.authentication.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
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

                        val db: DatabaseReference = Firebase.database.reference

                        val user = FirebaseAuth.getInstance().currentUser
                        val uid = user?.uid
                        if (uid == null) {
                            Toast.makeText(
                                this@RegisterActivity, R.string.email_error,Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val users: MutableMap<String, UserEntity> = HashMap()
                            val u = UserEntity(uid,email,ArrayList<HabitEntity>())
                            users[uid] = u
                            db.child("users").updateChildren(users as Map<String, Any>)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@RegisterActivity, R.string.success_habit, Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this@RegisterActivity, R.string.try_again_error, Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, task.exception!!.message,
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}