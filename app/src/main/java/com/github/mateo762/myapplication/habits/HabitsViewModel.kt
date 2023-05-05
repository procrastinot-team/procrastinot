package com.github.mateo762.myapplication.habits

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.room.HabitImageRepository
import com.github.mateo762.myapplication.room.HabitRepository
import java.time.LocalDateTime
import javax.inject.Inject

class HabitsViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    var date = LocalDateTime.of(2023, 4, 15, 17, 0)
    var habits = mutableStateOf(emptyList<HabitEntity>())
    var images = mutableStateOf(emptyList<HabitImageEntity>())
}
