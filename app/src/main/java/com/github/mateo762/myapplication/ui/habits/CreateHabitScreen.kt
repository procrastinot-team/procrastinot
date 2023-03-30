package com.github.mateo762.myapplication.ui.habits

import android.app.TimePickerDialog
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek
import java.util.*

@Composable
fun CreateHabitScreen() {
    val context = LocalContext.current
    var habitName by remember { mutableStateOf("") }
    var habitDays by remember { mutableStateOf(emptyList<DayOfWeek>()) }
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    var habitStartTime = remember { mutableStateOf("00:00") }
    var habitEndTime = remember { mutableStateOf("23:59") }
    var isChoosingStartTime = true

    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            if (isChoosingStartTime) {
                habitStartTime.value = "$mHour:$mMinute"
            } else {
                habitEndTime.value = "$mHour:$mMinute"
            }
        }, mHour, mMinute, true
    )

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
                    .testTag("txt_name"),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(R.color.card_background_light)
                )
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
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            isChoosingStartTime = true
                            mTimePickerDialog.show()
                        }, modifier = Modifier
                            .padding(top = 16.dp)
                            .testTag("btn_start_time")
                    )
                    {
                        Text(text = stringResource(R.string.create_habit_start_time_button_text))
                    }
                    Text(
                        text = stringResource(
                            R.string.create_habit_start_time_text,
                            habitStartTime.value
                        ), modifier = Modifier
                            .padding(16.dp)
                            .testTag("txt_start_time_text")
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            isChoosingStartTime = false
                            mTimePickerDialog.show()
                        }, modifier = Modifier
                            .padding(top = 16.dp)
                            .testTag("btn_end_time")
                    )
                    {
                        Text(text = stringResource(R.string.create_habit_end_time_button_text))
                    }
                    Text(
                        text = stringResource(
                            R.string.create_habit_end_time_text,
                            habitEndTime.value
                        ), modifier = Modifier
                            .padding(16.dp)
                            .testTag("txt_end_time_text")
                    )
                }

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
                        } else {
                            // This intent would now save into a DB / Firebase
                            // For now, it returns to the calling activity
                            val intent =
                                Intent(context, HabitsActivity::class.java)
                            intent.putExtra("habitName", habitName)
                            intent.putExtra("habitDays", ArrayList(habitDays))
                            intent.putExtra("habitStartTime", habitStartTime.value)
                            intent.putExtra("habitEndTime", habitEndTime.value)
                            context.startActivity(intent)

                            //
                            val myHabit = Habit(
                                habitName,
                                ArrayList(habitDays),
                                habitStartTime.value,
                                habitEndTime.value
                            )
                            val db: DatabaseReference = Firebase.database.reference
                            //todo makfazlic should be replaced with the userId retrieved from the auth
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
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.bottom_highlight),
                        contentColor = Color.White
                    ),
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