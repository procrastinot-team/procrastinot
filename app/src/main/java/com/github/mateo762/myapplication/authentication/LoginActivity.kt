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
import com.github.mateo762.myapplication.home.HomeActivity.HomeEntryPoint
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
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
            Toast.makeText(this, R.string.error_register, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if the user is already logged in
        if (PreferenceHelper.isLoggedIn(this)) {
            startActivity(Intent(this, HomeEntryPoint::class.java))
            finish()
            return
        }

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
            showToastMessage(R.string.error_empty_login)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this) {
                    showToastMessage(R.string.success_login)
                    val intent = Intent(this, HomeEntryPoint::class.java)
                    startActivity(intent)
                    PreferenceHelper.setLoggedIn(this, true)
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
                                showToastMessage( R.string.user_data_error)
                            } else {
                                val users: MutableMap<String, UserEntity> = HashMap()
                                val u = UserEntity(uid,displayName,"username", email, ArrayList<HabitEntity>(), listOf(), listOf(), listOf())
                                users[uid] = u
                                db.child("users").updateChildren(users as Map<String, Any>)
                                    .addOnSuccessListener {
                                        showToastMessage(R.string.success_register)
                                        val intent = Intent(this, UsernameActivity.EntryPoint::class.java)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        showToastMessage(R.string.try_again_error)
                                    }
                            }

                        } else {
                            showToastMessage(R.string.success_login)
                            val intent = Intent(this, HomeEntryPoint::class.java)
                            startActivity(intent)
                        }
                        PreferenceHelper.setLoggedIn(this, true)
                    } else {
                        showToastMessage(task.exception!!.message)
                    }
                }
            }
        } else {
            showToastMessage(task.exception!!.message)
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun showToastMessage(resId:Int) {
        Toast.makeText(baseContext, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToastMessage(message:String?) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}