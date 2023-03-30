package com.github.mateo762.myapplication.ui.habits

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek

@Composable
fun CreateHabitScreen() {
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
                    .background(Color(0xFFFCF2F9))
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
                        habitStartTime = if (it.length <= 5) {
                            if (it.length >= 3 && it[2] != ':') {
                                TextFieldValue(text = habitStartTime.text)
                            } else {
                                TextFieldValue(text = it)
                            }
                        } else {
                            habitStartTime
                        }
                    },
                    label = { Text("What time does the habit start? (HH:MM)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("txt_time_start")
                        .background(Color(0xFFFCF2F9))
                )

                TextField(
                    value = habitEndTime.text,
                    onValueChange = {
                        habitEndTime = if (it.length <= 5) {
                            if (it.length >= 3 && it[2] != ':') {
                                TextFieldValue(text = habitEndTime.text)
                            } else {
                                TextFieldValue(text = it)
                            }
                        } else {
                            habitEndTime
                        }
                    },
                    label = { Text("What time does the habit start? (HH:MM)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("txt_time_end")
                        .background(Color(0xFFFCF2F9))
                )


                Button(
                    onClick = {
                        if (habitName.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please enter a habit name",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else if (habitDays.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please select at least one day",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!isValidTime(habitStartTime.text) || !isValidTime(
                                habitEndTime.text
                            )
                        ) {
                            Toast.makeText(
                                context,
                                "Please enter a valid time (HH:MM)",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // This intent would now save into a DB / Firebase
                            // For now, it returns to the calling activity
                            val intent =
                                Intent(context, HabitsActivity::class.java)
                            intent.putExtra("habitName", habitName)
                            intent.putExtra("habitDays", ArrayList(habitDays))
                            intent.putExtra("habitStartTime", habitStartTime.text)
                            intent.putExtra("habitEndTime", habitEndTime.text)
                            context.startActivity(intent)

                            //
                            val myHabit = Habit(
                                habitName,
                                ArrayList(habitDays),
                                habitStartTime.text,
                                habitEndTime.text
                            )
                            val db: DatabaseReference = Firebase.database.reference
                            // makfazlic should be replaced with the userId retrieved from the auth
                            val userRef = db.child("users").child("makfazlic")
                            val key = userRef.push().key
                            if (key != null) {
                                db.child("users").child("makfazlic").child(key).setValue(myHabit)
                                    .addOnSuccessListener {
                                        println("Success")

                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Try again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7B4FC3), contentColor = Color.White),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag("btn_save")
                ) {
                    Text("Save Habit")
                }
            }
        }
    }
}

private fun isValidTime(time: String): Boolean {
    val pattern = Regex(pattern = "^([0-1]\\d|[22-3]):([0-5][0-9])$")
    return pattern.matches(time)
}