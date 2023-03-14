package com.github.mateo762.myapplication.habits.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import java.time.DayOfWeek
import java.time.LocalTime


class ListFragment : Fragment() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View {
            return ComposeView(requireContext()).apply {
                setContent {
                    HabitListScreen(habits = getHardCodedHabits())
                }
            }
        }
    }

    data class Habit(
        val name: String,
        val daysOfWeek: List<DayOfWeek>,
        val startTime: LocalTime,
        val endTime: LocalTime
    )

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun HabitListScreen(habits: List<Habit>) {
        Scaffold(
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(habits) { habit ->
                    HabitListItem(habit = habit)
                }
            }
        }
    }

    @Composable
    fun HabitListItem(habit: Habit) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag(habit.name)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = habit.daysOfWeek.joinToString(),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Start time: ${habit.startTime}",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "End time: ${habit.endTime}",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getHardCodedHabits(): List<Habit> {
        return listOf(
            Habit(
                name = "Morning Walk",
                daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                startTime = LocalTime.of(6, 30),
                endTime = LocalTime.of(7, 30),
            ),
            Habit(
                name = "Reading",
                daysOfWeek = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
                startTime = LocalTime.of(20, 0),
                endTime = LocalTime.of(21, 0)
            ),
            Habit(
                name = "Meditation",
                daysOfWeek = listOf(DayOfWeek.SUNDAY),
                startTime = LocalTime.of(7, 0),
                endTime = LocalTime.of(8, 0)
            ),
            Habit(
                name = "Walking",
                daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                startTime = LocalTime.of(6, 30),
                endTime = LocalTime.of(7, 30)
            ),
            Habit(
                name = "Gym",
                daysOfWeek = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
                startTime = LocalTime.of(20, 0),
                endTime = LocalTime.of(21, 0)
            ),
            Habit(
                name = "Swimming",
                daysOfWeek = listOf(DayOfWeek.SUNDAY),
                startTime = LocalTime.of(7, 0),
                endTime = LocalTime.of(8, 0)
            )
        )
    }