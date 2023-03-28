package com.github.mateo762.myapplication.habits.fragments.week

import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.getHardCodedHabits
import java.util.*

class WeekFragment : Fragment(), WeekView.EventClickListener, WeekView.EventLongPressListener,
    WeekView.EmptyViewLongPressListener, MonthLoader.MonthChangeListener {

    private lateinit var weekView: WeekView

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
        var eventId = 1L
        var habitsList = getHardCodedHabits()
        habitsList.forEach { habit ->
            val weekViewEvents = habitToWeekViewEvent(habit, eventId)
            eventsList.addAll(weekViewEvents)
            eventId += weekViewEvents.size
        }
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
}
