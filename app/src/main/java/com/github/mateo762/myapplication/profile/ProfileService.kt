package com.github.mateo762.myapplication.profile

import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import kotlinx.coroutines.flow.Flow

interface ProfileService {
    fun getHabitsImages(userId: String): Flow<ArrayList<HabitImageEntity>>

    fun getHabits(userId: String): Flow<ArrayList<HabitEntity>>
}