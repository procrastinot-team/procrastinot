package com.github.mateo762.myapplication.habits

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.mateo762.myapplication.models.HabitEntity

class HabitsViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<HabitEntity>())
}
