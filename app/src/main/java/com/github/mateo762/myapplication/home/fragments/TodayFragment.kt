package com.github.mateo762.myapplication.home.fragments

import com.github.mateo762.myapplication.R
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.Habit
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class TodayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                HabitTodayListScreen(habits = getHardCodedHabits())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

val imageArray = arrayOf(
    R.drawable.ic_new,
    R.drawable.ic_new,
    R.drawable.ic_new
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitTodayListScreen(habits: List<Habit>) {
    val today = LocalDate.now().dayOfWeek
    val now = LocalTime.now()
    val todayHabits = habits.filter { it.habitDays.contains(today) }
    val nextUpHabit = habits.flatMap { habit -> habit.habitDays.map { day -> habit to day } }
        .filter { (habit, day) ->
            day > today || (day == today && minutesUntilStartTime(habit.habitStartTime, now) >= 0)
        }
        .minByOrNull { (habit, day) ->
            val dayDiff = day.ordinal - today.ordinal
            dayDiff * 24 * 60 + minutesUntilStartTime(habit.habitStartTime, now)
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
                                text = it.habitName,
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
                        ChronoUnit.HOURS.between(now, parseTime(nextUpHabit.habitStartTime))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = nextUpHabit.habitName,
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
        ImageRow(images = imageArray, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun ImageRow(images: Array<Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
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

@RequiresApi(Build.VERSION_CODES.O)
private fun getHardCodedHabits(): List<Habit> {
    return listOf(
        Habit(
            habitName = "Read",
            habitDays = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            habitStartTime = "08:00",
            habitEndTime = "08:30"
        ),
        Habit(
            habitName = "Drink water",
            habitDays = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            habitStartTime = "09:00",
            habitEndTime = "17:00"
        ),
        Habit(
            habitName = "Exercise",
            habitDays = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            habitStartTime = "18:00",
            habitEndTime = "19:00"
        ),
        Habit(
            habitName = "Meditate",
            habitDays = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            habitStartTime = "07:00",
            habitEndTime = "07:15"
        ),
        Habit(
            habitName = "Walk dog",
            habitDays = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            habitStartTime = "20:00",
            habitEndTime = "20:10"
        ),
        Habit(
            habitName = "Ride bike",
            habitDays = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            habitStartTime = "07:00",
            habitEndTime = "09:10"
        )
    )
}