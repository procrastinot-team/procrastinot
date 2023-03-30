package com.github.mateo762.myapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.Habit
import java.time.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(time: LocalDateTime, habits: List<Habit>, images: Array<Int>) {
    val today = time.dayOfWeek
    val now = time.toLocalTime()
    val todayHabits = habits.filter { it.days.contains(today) }
        .sortedBy { parseTime(it.startTime) }
    val nextUpHabit = habits.flatMap { habit -> habit.days.map { day -> habit to day } }
        .filter { (habit, day) ->
            day > today || (day == today && minutesUntilStartTime(habit.startTime, now) >= 0)
        }
        .minByOrNull { (habit, day) ->
            val dayDiff = if (day.ordinal >= today.ordinal) {
                day.ordinal - today.ordinal
            } else {
                7 - (today.ordinal - day.ordinal)
            }
            dayDiff * 24 * 60 + minutesUntilStartTime(habit.startTime, now)
        }
        ?.first

    val nextUpHabitDay = habits.flatMap { habit -> habit.days.map { day -> habit to day } }
        .filter { (habit, day) ->
            day > today || (day == today && minutesUntilStartTime(habit.startTime, now) >= 0)
        }
        .minByOrNull { (habit, day) ->
            val dayDiff = if (day.ordinal >= today.ordinal) {
                day.ordinal - today.ordinal
            } else {
                7 - (today.ordinal - day.ordinal)
            }
            dayDiff * 24 * 60 + minutesUntilStartTime(habit.startTime, now)
        }
        ?.second

    val nextUpLocalDate = if (nextUpHabitDay != null) {
        time.toLocalDate().plusDays(
            if (nextUpHabitDay.ordinal >= today.ordinal) {
                nextUpHabitDay.ordinal - today.ordinal
            } else {
                7 - (today.ordinal - nextUpHabitDay.ordinal)
            }.toLong()
        )
    } else {
        time.toLocalDate()
    }

    val nextUpLocalDateTime = if (nextUpHabit != null) {
        LocalDateTime.of(nextUpLocalDate, parseTime(nextUpHabit.startTime))
    } else {
        time
    }

    val allocatedHours = Duration.between(time, nextUpLocalDateTime).toHours()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Today
        Box(
            modifier = Modifier
                .background(Color(0xFFE7E0EC), RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Column {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.h3,
                )
                if (todayHabits.isEmpty()) {
                    Text(
                        text = "No habits due for today.",
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .testTag("today_empty")
                    )
                } else {
                    todayHabits.forEach {
                        val isCompleted = parseTime(it.startTime).isBefore(now)
                        val testTag = if (isCompleted) {
                            "today_completed_${it.name}"
                        } else {
                            "today_pending_${it.name}"
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .testTag(testTag)
                        ) {
                            Text(
                                text = it.name,
                                style = if (isCompleted) {
                                    MaterialTheme.typography.h5.copy(
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                } else {
                                    MaterialTheme.typography.h5
                                }
                            )
                            Text(
                                text = "0\uD83D\uDD25",
                                style = MaterialTheme.typography.h5,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Next up
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // No easy way to call colors defined in theme, so must be input directly
                .background(Color(0xFFFCF2F9), RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Column {
                Text(
                    text = "Next up",
                    style = MaterialTheme.typography.h3,
                )
                if (nextUpHabit == null) {
                    Text(
                        text = "No upcoming habits.",
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .testTag("next_up_empty")
                    )
                } else {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .testTag("next_up_${nextUpHabit.name}")
                        ) {
                            Text(
                                text = nextUpHabit.name,
                                style = MaterialTheme.typography.h5
                            )
                            Text(
                                text = "0\uD83D\uDD25",
                                style = MaterialTheme.typography.h5,
                                textAlign = TextAlign.End
                            )
                        }
                        Text(
                            text = "Allocated in $allocatedHours hours",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 22.dp)
                                .testTag("next_up_allocated_hours_${allocatedHours}")
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ImageRow(images = images, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun ImageRow(images: Array<Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Row(modifier = modifier) {
            for (image in images) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.7f)
                        .background(Color(0xFFFCF2F9), RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("image")
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseTime(timeString: String): LocalTime {
    val components = timeString.split(":").map { it.toInt() }
    return LocalTime.of(components[0], components[1])
}

@RequiresApi(Build.VERSION_CODES.O)
fun minutesUntilStartTime(startTime: String, now: LocalTime): Long {
    val start = parseTime(startTime)
    val duration = Duration.between(now, start)
    return duration.toMinutes()
}