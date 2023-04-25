package com.github.mateo762.myapplication.habits

import com.github.mateo762.myapplication.room.HabitEntity

interface HabitService {
    fun addHabit(user: String, habit: HabitEntity, callback: HabitServiceCallback)
    fun getHabits(user: String, callback: HabitServiceCallback)
}