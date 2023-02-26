package com.github.mateo762.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.mainGreetButton)
        button.setOnClickListener {
            val name = findViewById<TextView>(R.id.mainName)
            val greetIntent = Intent(this@MainActivity, GreetingActivity::class.java)
            greetIntent.putExtra("name", name.text.toString())
            this@MainActivity.startActivity(greetIntent)
        }

        val boredApiButton = findViewById<Button>(R.id.mainBoredButton)
        boredApiButton.setOnClickListener {
            val apiIntent = Intent(this@MainActivity, BoredActivity::class.java)
            this@MainActivity.startActivity(apiIntent)
        }
    }
}