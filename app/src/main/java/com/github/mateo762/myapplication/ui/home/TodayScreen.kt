package com.github.mateo762.myapplication.ui.home

import android.content.Context
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
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.UploadFragment
import com.google.accompanist.coil.rememberCoilPainter
import java.time.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(time: LocalDateTime, habits: List<HabitEntity>, images: List<HabitImageEntity>) {
    val context = LocalContext.current
    val now = time.toLocalTime()

    val (todayHabits, nextUpHabit, nextUpHabitDay) = filterAndSortHabits(habits, time)

    val imagesForNextUpHabit = filterImagesForNextUpHabit(images, nextUpHabit)

    val nextUpLocalDateTime = getNextUpLocalDateTime(time, nextUpHabit, nextUpHabitDay)

    val allocatedHours = Duration.between(time, nextUpLocalDateTime).toHours()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TodayHabitsSection(todayHabits, now)
        Spacer(modifier = Modifier.height(16.dp))
        NextUpHabitsSection(nextUpHabit, allocatedHours)
        Spacer(modifier = Modifier.height(16.dp))
        if (nextUpHabit != null) {
            ImageRow(
                images = imagesForNextUpHabit,
                modifier = Modifier.fillMaxWidth(),
                nextUpHabitName = nextUpHabit.name
            )
        }
        GoToUploadButton(context)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayHabitsSection(todayHabits: List<HabitEntity>, now: LocalTime) {
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
                todayHabits.forEach { habit ->
                    val isCompleted = parseTime(habit.startTime).isBefore(now)
                    val testTag = if (isCompleted) {
                        "today_completed_${habit.name}"
                    } else {
                        "today_pending_${habit.name}"
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
                            text = habit.name,
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
}


@Composable
fun NextUpHabitsSection(nextUpHabit: HabitEntity?, allocatedHours: Long) {
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageRow(images: List<HabitImageEntity>, modifier: Modifier = Modifier, nextUpHabitName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Row(modifier = modifier) {
            for (image in images) {
                val painter = rememberCoilPainter(request = image.url, fadeIn = true)
                DisplayImage(painter, image, nextUpHabitName, Modifier.weight(1f))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayImage(
    painter: Painter,
    image: HabitImageEntity,
    nextUpHabitName: String,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(12.dp)
    val date = LocalDateTime.parse(image.date)
    val numberMonth = date.monthValue
    val numberDay = date.dayOfMonth
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
                .testTag("image_${nextUpHabitName}_${numberMonth}_${numberDay}")
        )
    }
}

@Composable
fun GoToUploadButton(context: Context) {
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

@RequiresApi(Build.VERSION_CODES.O)
fun filterAndSortHabits(
    habits: List<HabitEntity>,
    time: LocalDateTime
): Triple<List<HabitEntity>, HabitEntity?, DayOfWeek?> {
    val today = time.dayOfWeek
    val now = time.toLocalTime()

    val todayHabits = habits.filter { habit -> habit.days.contains(today) }
        .sortedBy { habit -> parseTime(habit.startTime) }

    val nextUpHabitData = habits.flatMap { habit -> habit.days.map { day -> habit to day } }
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

    val nextUpHabit = nextUpHabitData?.first
    val nextUpHabitDay = nextUpHabitData?.second

    return Triple(todayHabits, nextUpHabit, nextUpHabitDay)
}

@RequiresApi(Build.VERSION_CODES.O)
fun filterImagesForNextUpHabit(images: List<HabitImageEntity>, nextUpHabit: HabitEntity?): List<HabitImageEntity> {
    return images
        .sortedByDescending { it.date }
        .filter { it.habitId == nextUpHabit?.id }
        .take(3)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNextUpLocalDateTime(
    time: LocalDateTime,
    nextUpHabit: HabitEntity?,
    nextUpHabitDay: DayOfWeek?
): LocalDateTime {
    val nextUpLocalDate = if (nextUpHabitDay != null) {
        time.toLocalDate().plusDays(
            if (nextUpHabitDay.ordinal >= time.dayOfWeek.ordinal) {
                nextUpHabitDay.ordinal - time.dayOfWeek.ordinal
            } else {
                7 - (time.dayOfWeek.ordinal - nextUpHabitDay.ordinal)
            }.toLong()
        )
    } else {
        time.toLocalDate()
    }

    return if (nextUpHabit != null) {
        LocalDateTime.of(nextUpLocalDate, parseTime(nextUpHabit.startTime))
    } else {
        time
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