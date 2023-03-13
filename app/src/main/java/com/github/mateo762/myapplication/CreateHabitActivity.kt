package com.github.mateo762.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

class CreateHabitActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitInputScreen()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitInputScreen() {
    val context = LocalContext.current
    var habitName by remember { mutableStateOf("") }
    var habitDays by remember { mutableStateOf(emptyList<DayOfWeek>()) }
    var habitStartTime by remember { mutableStateOf(TextFieldValue("00:00")) }
    var habitEndTime by remember { mutableStateOf(TextFieldValue("23:59")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Create new habit",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )

            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Name of the habit") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("txt_name")
            )

            Column {

                DayOfWeek.values().forEach { day ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = day in habitDays,
                            onCheckedChange = {
                                habitDays = if (it) {
                                    habitDays + day
                                } else {
                                    habitDays - day
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .testTag("checkbox_$day")
                        )
                        Text(day.toString(), modifier = Modifier.padding(start = 8.dp))
                    }
                }

                TextField(
                    value = habitStartTime.text,
                    onValueChange = {
                        if (it.length <= 5) {
                            if (it.length >= 3 && it[2] != ':') {
                                habitStartTime = TextFieldValue(text = habitStartTime.text)
                            } else {
                                habitStartTime = TextFieldValue(text = it)
                            }
                        } else {
                            habitStartTime = habitStartTime
                        }
                    },
                    label = { Text("What time does the habit start? (HH:MM)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("txt_time_start")
                )

                TextField(
                    value = habitEndTime.text,
                    onValueChange = {
                        if (it.length <= 5) {
                            if (it.length >= 3 && it[2] != ':') {
                                habitEndTime = TextFieldValue(text = habitEndTime.text)
                            } else {
                                habitEndTime = TextFieldValue(text = it)
                            }
                        } else {
                            habitEndTime = habitEndTime
                        }
                    },
                    label = { Text("What time does the habit start? (HH:MM)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("txt_time_end")
                )


                Button(
                    onClick = {
                        if (habitName.isBlank()) {
                            Toast.makeText(context, "Please enter a habit name", Toast.LENGTH_LONG)
                                .show()
                        } else if (habitDays.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please select at least one day",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!isValidTime(habitStartTime.text) || !isValidTime(habitEndTime.text)) {
                            Toast.makeText(
                                context,
                                "Please enter a valid time (HH:MM)",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val intent = Intent(context, DisplayParametersActivity::class.java)
                            intent.putExtra("habitName", habitName)
                            intent.putExtra("habitDays", ArrayList(habitDays))
                            intent.putExtra("habitStartTime", habitStartTime.text)
                            intent.putExtra("habitEndTime", habitEndTime.text)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag("btn_save")
                ) {
                    Text("Save Habit")
                }
                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag("btn_cancel")
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

private fun isValidTime(time: String): Boolean {
    val pattern = Regex(pattern = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$")
    return pattern.matches(time)
}




