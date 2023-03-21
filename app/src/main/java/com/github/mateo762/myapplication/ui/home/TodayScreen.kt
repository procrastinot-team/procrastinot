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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.Habit
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(habits: List<Habit>, images: Array<Int>) {
    val today = LocalDate.now().dayOfWeek
    val now = LocalTime.now()
    val todayHabits = habits.filter { it.days.contains(today) }
    val nextUpHabit = habits.flatMap { habit -> habit.days.map { day -> habit to day } }
        .filter { (habit, day) ->
            day > today || (day == today && minutesUntilStartTime(habit.startTime, now) >= 0)
        }
        .minByOrNull { (habit, day) ->
            val dayDiff = day.ordinal - today.ordinal
            dayDiff * 24 * 60 + minutesUntilStartTime(habit.startTime, now)
        }
        ?.first

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Today
        Box(
            modifier = Modifier
                .background(Color(0xFFFED4D6), RoundedCornerShape(8.dp))
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
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                } else {
                    todayHabits.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.h5
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
                .background(Color(0xFFC4D4F4), RoundedCornerShape(8.dp))
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
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                } else {
                    val allocatedHours =
                        ChronoUnit.HOURS.between(now, parseTime(nextUpHabit.startTime))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
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
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 22.dp)
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
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
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