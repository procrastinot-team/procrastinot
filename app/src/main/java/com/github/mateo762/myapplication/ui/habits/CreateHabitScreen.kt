package com.github.mateo762.myapplication.ui.habits

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.models.HabitEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateHabitScreen() {
    val TAG = "CreateHabitScreen"
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
                        } else if (!isValidTime(habitStartTime.value) || !isValidTime(
                                habitEndTime.value
                            )
                        ) {
                            Toast.makeText(
                                context, R.string.invalid_time_error, Toast.LENGTH_SHORT
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

                            val myHabit = HabitEntity(
                                UUID.randomUUID().toString(),
                                habitName,
                                ArrayList(habitDays),
                                habitStartTime.value,
                                habitEndTime.value
                            )
                            val user = FirebaseAuth.getInstance().currentUser

                            val uid = user?.uid
                            if (uid == null) {
                                Toast.makeText(
                                    context, R.string.user_data_error, Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val db = Firebase.database.reference

                                val habitData = hashMapOf(
                                    "id" to myHabit.id,
                                    "name" to myHabit.name,
                                    "days" to myHabit.days,
                                    "startTime" to myHabit.startTime,
                                    "endTime" to myHabit.endTime
                                )

                                val habitRef =
                                    db.child("users").child(uid).child("habitsPath").push()

                                habitRef.setValue(habitData)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Habit added with ID: ${habitRef.key}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding habit", e)
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

private fun isValidTime(time: String): Boolean {
    val pattern = Regex(pattern = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])\$")
    return pattern.matches(time)
}