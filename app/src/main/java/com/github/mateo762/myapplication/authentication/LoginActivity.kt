package com.github.mateo762.myapplication.authentication

import LoginScreen
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.room.HabitEntity
import com.github.mateo762.myapplication.room.UserEntity
import com.github.mateo762.myapplication.username.UsernameActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        } else {
            // Error in registering
            Toast.makeText(
                this, "Encountered error while registering with google",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(signInUser = ::signInUser, googleSignIn = ::googleSignIn)
        }

        // Initialize Firebase auth instance
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val from = intent.getStringExtra("from")
        if (from == "RegisterWithGoogle") {
            googleSignIn()
        }
    }

    private fun signInUser(email: String, password: String) {

        // add null check on text values
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, R.string.error_empty_login,
                Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, R.string.success_login,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            baseContext, task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (intent.hasExtra("from")) {
                            val db: DatabaseReference = Firebase.database.reference

                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid
                            val email = user?.email
                            val displayName = user?.displayName
                            if (uid == null || email == null || displayName == null) {
                                Toast.makeText(
                                    this@LoginActivity, R.string.user_data_error,Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val users: MutableMap<String, UserEntity> = HashMap()
                                val u = UserEntity(uid,displayName,"username", email, ArrayList<HabitEntity>(), listOf(), listOf(), listOf())
                                users[uid] = u
                                db.child("users").updateChildren(users as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Toast.makeText(baseContext, R.string.success_register,
                                            Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, UsernameActivity::class.java)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            this@LoginActivity, R.string.try_again_error, Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                        } else {
                              Toast.makeText(baseContext, R.string.success_login,
                                  Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            baseContext, task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                baseContext, task.exception!!.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }
}