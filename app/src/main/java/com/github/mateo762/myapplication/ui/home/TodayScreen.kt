package com.github.mateo762.myapplication.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.HabitImage
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.TAG
import com.github.mateo762.myapplication.ui.upload.UploadFragment
import com.google.accompanist.coil.rememberCoilPainter
import java.time.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(time: LocalDateTime, habits: List<Habit>, images: List<HabitImage>) {
    Log.d(TAG, "Habit images today screen. $images")
    val context = LocalContext.current
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

    val imagesForNextUpHabit = images
        .sortedByDescending { it.date }
        .filter { it.habitId == nextUpHabit?.id }
        .take(3)
    Log.d(TAG, "Nextuphabit id: ${nextUpHabit?.id}")
    Log.d(TAG, "images id: ${images}")
    Log.d(TAG, "imagesForNextUpHabit id: ${imagesForNextUpHabit}")


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
                .background(colorResource(R.color.card_background_dark), RoundedCornerShape(8.dp))
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
                .background(colorResource(R.color.card_background_light), RoundedCornerShape(8.dp))
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
        ImageRow(imagesUrls = imagesForNextUpHabit.map { it -> it.url }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val fragmentManager = (context as? FragmentActivity)?.supportFragmentManager
            fragmentManager?.beginTransaction()?.replace(
                R.id.navHostFragment,
                UploadFragment()
            )?.commit()
        }) {
            Text("Go to Upload")
        }
    }
}

@Composable
fun ImageRow(imagesUrls: List<String>, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Row(modifier = modifier) {
            for (imageUrl in imagesUrls) {
                val painter = rememberCoilPainter(request = imageUrl, fadeIn = true)
                DisplayImage(painter, imageUrl, Modifier.weight(1f))
            }
        }
    }
}
@Composable
fun DisplayImage(painter: Painter, imageUrl: String, modifier: Modifier = Modifier) {
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .padding(4.dp) // Add padding for separation
            .aspectRatio(0.7f)
            .background(
                colorResource(R.color.card_background_light),
                cornerShape
            )
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(cornerShape) // Clip the image with the same corner shape
                .testTag("image")
        )
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