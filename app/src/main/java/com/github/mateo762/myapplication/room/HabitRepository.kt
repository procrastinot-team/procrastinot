package com.github.mateo762.myapplication.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitRepository @Inject constructor(private val habitDao: HabitDao) {
    suspend fun getAllHabits(): List<HabitEntity> {
        return withContext(Dispatchers.IO) {
            habitDao.getAll()
        }
    }

    suspend fun insertAllHabits(habits: List<HabitEntity>) {
        withContext(Dispatchers.IO) {
            habitDao.insertAll(habits)
        }
    }
}
