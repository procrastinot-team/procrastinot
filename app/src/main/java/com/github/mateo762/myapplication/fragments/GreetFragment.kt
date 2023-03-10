package com.github.mateo762.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.GreetingActivity
import com.github.mateo762.myapplication.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class GreetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    GreetUI()
                }
            }
        }
    }

    @Composable
    private fun GreetUI() {
        val nameState = remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter your name:",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text(text = "Name") },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("text_name")
            )
            Button(
                onClick = {
                    val intent = Intent(requireContext(), GreetingActivity::class.java)
                    intent.putExtra("name", nameState.value)

                    startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_greet")
            ) {
                Text(text = "Greet")
            }
            Button(
                onClick = {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    FirebaseAuth.getInstance().signOut()
                    startActivity(intent)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_sign_out")
            ) {
                Text(text = "Sign Out")
            }
            Button(
                onClick = {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        println("Already logged in")
                        println(user.email)
                        println(user.uid)
                    } else {
                        println("Error: User not logged in")
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("btn_check")
            ) {
                Text(text = "Check user logged in")
            }
        }
    }
}