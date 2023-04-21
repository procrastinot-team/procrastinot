package com.github.mateo762.myapplication.ui.authentication

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.authentication.LoginActivity

@Composable
fun RegisterScreen(registerUser: (String, String, String, String) -> Unit) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val nameFiller by remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val surnameFiller by remember { mutableStateOf("") }
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
            text = nameFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text(text = "Name") },
            modifier = Modifier
                .padding(8.dp)
                .testTag("text_name")
        )
        Text(
            text = surnameFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = surname.value,
            onValueChange = { surname.value = it },
            label = { Text(text = "Surname") },
            modifier = Modifier
                .padding(8.dp)
                .testTag("text_surname")
        )
        Text(
            text = emailFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .padding(8.dp)
                .testTag("text_email")
        )
        Text(
            text = passwordFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(text = "Password") },
            modifier = Modifier
                .padding(8.dp)
                .testTag("text_password")
        )
        Button(
            onClick = {
                registerUser(name.value, surname.value, email.value, password.value)
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_register")
        ) {
            Text(text = "Register")
        }
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_already_login")
        ) {
            Text(text = "Already have an account? Login")
        }
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra("from", "RegisterWithGoogle")
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_register_google")
        ) {
            Text(text = "Register with Google")
        }
    }
}