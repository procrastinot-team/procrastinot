import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
fun WeekScreen(habits: List<Habit>) {
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

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
                    .background(Color.Transparent)
            ) {
                Spacer(modifier = Modifier.width(68.dp))
                DayOfWeek.values().forEach { dayOfWeek ->
                    Text(
                        text = "" + dayOfWeek.name[0],
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                HourLabels(hours, hourFormatter)

                Spacer(modifier = Modifier.width(8.dp))

                DayOfWeek.values().forEach { dayOfWeek ->
                    Column(modifier = Modifier.weight(1f)) {
                        hours.forEach { hour ->
                            val habitsForHour = habitGrid[dayOfWeek.value - 1][hour.hour]
                            DayColumn(hour, dayOfWeek, habitsForHour)
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayColumn(hour: LocalTime, dayOfWeek: DayOfWeek, habits: List<Habit>) {
    Box {
        habits.forEach { habit ->
            HabitItem(habit)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitItem(habit: Habit) {
    val startTime = LocalTime.parse(habit.startTime, DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = LocalTime.parse(habit.endTime, DateTimeFormatter.ofPattern("HH:mm"))

    val startHour = startTime.hour
    val startMinute = startTime.minute
    val startInterval = if (startMinute < 30) startHour * 2 else startHour * 2 + 1

    val endHour = endTime.hour
    val endMinute = endTime.minute
    val endInterval = if (endMinute == 0) endHour * 2 else endHour * 2 + 1

    val itemHeight = (endInterval - startInterval) * 64 / 2

    val topPadding = startInterval * 64 / 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight.dp)
            .absoluteOffset(y = topPadding.dp)
            .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = habit.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourLabels(hours: List<LocalTime>, hourFormatter: DateTimeFormatter) {
    Column(
        modifier = Modifier.width(60.dp).background(Color.Transparent)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        hours.forEach { hour ->
            Text(
                text = hour.format(hourFormatter),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.height(60.dp),
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}