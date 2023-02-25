package com.github.mateo762.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        val intent = intent
        val name = intent.getStringExtra("name")
        val greetingText = findViewById<TextView>(R.id.greetingText)
        greetingText.text = "Hello $name!"
    }
}