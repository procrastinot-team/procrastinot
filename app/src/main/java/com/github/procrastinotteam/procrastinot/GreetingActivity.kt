package com.github.procrastinotteam.procrastinot

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

class GreetingActivity : AppCompatActivity() {

    private var greeting by mutableStateOf("Hello")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name")
        greeting = "Hello $name!"

        setContent {
            Column {
                Text(
                    text = greeting,
                    modifier = Modifier.testTag("display_name")
                )
            }
        }
    }
}
