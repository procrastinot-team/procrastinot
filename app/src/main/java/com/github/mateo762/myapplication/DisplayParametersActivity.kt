package com.github.mateo762.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.time.DayOfWeek

class DisplayParametersActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_parameters)

        val habitName = intent.getStringExtra("habitName")
        val habitDays = intent.getSerializableExtra("habitDays") as List<DayOfWeek>
        val habitDaysStringArray = habitDays.map { it.toString() }.toTypedArray()
        val habitStartTime = intent.getStringExtra("habitStartTime")
        val habitEndTime = intent.getStringExtra("habitEndTime")

        val parametersText = "Name: $habitName\nDays: ${habitDaysStringArray.joinToString()}\nStart Time: $habitStartTime\nEnd Time: $habitEndTime"

        val parametersTextView = findViewById<TextView>(R.id.parametersTextView)
        parametersTextView.text = parametersText
    }
}