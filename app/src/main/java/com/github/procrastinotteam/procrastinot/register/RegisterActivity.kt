package com.github.procrastinotteam.procrastinot.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.procrastinotteam.procrastinot.home.HomeActivity
import com.github.procrastinotteam.procrastinot.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterUI()
        }

        // Initialize Firebase auth instance
        auth = Firebase.auth
    }

    @Composable
    private fun RegisterUI() {
        val email = remember { mutableStateOf("") }
        val emailFiller by remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordFiller by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emailFiller,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("text_email")
            )
            Text(
                text = passwordFiller,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = { Text(text = "Password") },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("text_password")
            )
            Button(
                onClick = {
                      registerUser(email.value,password.value)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_register")
            ) {
                Text(text = "Register")
            }
            Button(
                onClick = {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_already_login")
            ) {
                Text(text = "Already have an account? Login")
            }
            Button(
                onClick = {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.putExtra("from","RegisterWithGoogle")
                    startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_register_google")
            ) {
                Text(text = "Register with Google")
            }
        }
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