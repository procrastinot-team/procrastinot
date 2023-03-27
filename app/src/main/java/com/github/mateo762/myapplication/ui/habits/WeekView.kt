import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mateo762.myapplication.Habit
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(habits: List<Habit>) {
    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val hours = (0..23).map { LocalTime.of(it, 0) }

    val habitGrid = Array(7) { Array(24) { mutableListOf<Habit>() } }
    habits.forEach { habit ->
        habit.days.forEach { dayOfWeek ->
            val startTime = LocalTime.parse(habit.startTime, hourFormatter)
            val startHour = startTime.hour
            habitGrid[dayOfWeek.value - 1][startHour].add(habit)
        }
    }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = 0.dp, max = 1000.dp) // Set a fixed height constraint
                .verticalScroll(verticalScrollState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        item { Spacer(modifier = Modifier.width(68.dp)) }
                        items(7) { index ->
                            Text(
                                text = DayOfWeek.of(index + 1).name[0].toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item { HourLabels(hours, hourFormatter) }

                        items(7) { dayIndex ->
                            LazyColumn() {
                                items(48) { hourIndex ->
                                    val habitsForHour =
                                        habitGrid[dayIndex][hourIndex / 2]
                                    if (hourIndex % 2 == 0) {
                                        HourSeparator()
                                    } else {
                                        HabitCell(
                                            habitsForHour,
                                            hourIndex / 2,
                                            dayIndex
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourSeparator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colors.onSurface)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitCell(habits: List<Habit>, hour: Int, dayOfWeek: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colors.surface)
            .padding(4.dp)
            .wrapContentSize(align = Alignment.TopStart)
    ) {
        habits.forEach { habit ->
            val startTime = LocalTime.parse(habit.startTime, DateTimeFormatter.ofPattern("HH:mm"))
            val endTime = LocalTime.parse(habit.endTime, DateTimeFormatter.ofPattern("HH:mm"))
            val startHour = startTime.hour
            val endHour = endTime.hour
            val startMinute = startTime.minute
            val endMinute = endTime.minute
            val startInterval = if (startMinute < 30) startHour * 2 else startHour * 2 + 1
            val endInterval = if (endMinute == 0) endHour * 2 else endHour * 2 + 1
            val itemHeight = (endInterval - startInterval) * 32
            val topPadding = startInterval * 32
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight.dp)
                    .padding(top = topPadding.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = habit.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourLabelss(hours: List<LocalTime>, hourFormatter: DateTimeFormatter) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        hours.forEach { hour ->
            Text(
                text = hour.format(hourFormatter),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.height(32.dp),
                color = MaterialTheme.colors.onSurface
            )
            HourSeparator()
        }
    }
}


