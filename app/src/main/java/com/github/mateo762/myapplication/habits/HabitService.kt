package com.github.mateo762.myapplication.habits

import com.github.mateo762.myapplication.Habit

interface HabitService {
    fun addHabit(user: String, habit: Habit, callback: HabitServiceCallback)
    fun getHabits(user: String, callback: HabitServiceCallback)
}