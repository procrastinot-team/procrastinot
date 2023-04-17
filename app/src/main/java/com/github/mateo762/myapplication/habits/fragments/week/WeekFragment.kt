package com.github.mateo762.myapplication.habits.fragments.week

// ...
import android.graphics.Color
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.getHardCodedHabits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

open class WeekFragment : Fragment(), WeekView.EventClickListener, WeekView.EventLongPressListener,
    WeekView.EmptyViewLongPressListener, MonthLoader.MonthChangeListener {

    private lateinit var weekView: CustomWeekView

    @RequiresApi(Build.VERSION_CODES.O)
    var habits = getHardCodedHabits()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        weekView = view.findViewById(R.id.weekView)
        setupWeekView()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWeekView() {
        weekView.setOnEventClickListener(this)
        weekView.eventLongPressListener = this
        weekView.emptyViewLongPressListener = this
        weekView.monthChangeListener = this
        weekView.firstDayOfWeek = Calendar.MONDAY
        weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

                return weekday.toUpperCase()
            }

            override fun interpretTime(hour: Int, minutes: Int): String {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, 0)

                val sdf = SimpleDateFormat("h a", Locale.getDefault())
                return sdf.format(calendar.time)
            }
        }
        // Wrap the code block with launch
        var colorIndex = 0
        var eventId = 1L
        val habitsList = habits


        // Use launchWhenResumed instead of launch
        lifecycleScope.launchWhenResumed {
            var colorIndex = 0
            var eventId = 1L
            val habitsList = habits

            withContext(Dispatchers.Default) {
                habitsList.forEach { habit ->
                    val weekViewEvents = habitToWeekViewEvent(
                        habit,
                        eventId,
                        colorsArray[colorIndex++],
                        LocalDateTime.now()
                    )
                    eventsList.addAll(weekViewEvents)
                    eventId += weekViewEvents.size
                }
            }

            // Update the UI after processing is complete
            weekView.notifyDatasetChanged()
        }


        // Scroll to the Monday of the current week
        val today = Calendar.getInstance()
        val daysFromMonday = (today.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7
        today.add(Calendar.DATE, -daysFromMonday)
        weekView.goToDate(today)


    }

    private val eventsList = mutableListOf<WeekViewEvent>()

    override fun onMonthChange(newYear: Int, newMonth: Int): List<WeekViewEvent> {
        // Return the events based on the visible month on the calendar
        return eventsList
    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        // Handle event click
    }

    override fun onEventLongPress(event: WeekViewEvent, eventRect: RectF) {
        // Handle event long press
    }

    override fun onEmptyViewLongPress(time: Calendar) {
        // Handle empty view long press
    }

    private val colorsArray = arrayOf(
        Color.parseColor("#F44336"), // Red
        Color.parseColor("#E91E63"), // Pink
        Color.parseColor("#3F51B5"), // Indigo
        Color.parseColor("#2196F3"), // Blue
        Color.parseColor("#03A9F4"), // Light Blue
        Color.parseColor("#00BCD4"), // Cyan
        Color.parseColor("#009688"), // Teal
        Color.parseColor("#4CAF50"), // Green
        Color.parseColor("#8BC34A"), // Light Green
        Color.parseColor("#CDDC39"), // Lime
        Color.parseColor("#FFEB3B"), // Yellow
        Color.parseColor("#FFC107"), // Amber
        Color.parseColor("#FF9800"), // Orange
        Color.parseColor("#FF5722"), // Deep Orange
        Color.parseColor("#795548")  // Brown
    )
}
