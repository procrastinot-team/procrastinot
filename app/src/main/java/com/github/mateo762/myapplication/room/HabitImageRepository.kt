package com.github.mateo762.myapplication.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitImageRepository @Inject constructor(private val habitImageDao: HabitImageDao) {
    suspend fun getAllHabitImages(): List<HabitImageEntity> {
        return withContext(Dispatchers.IO) {
            habitImageDao.getAll()
        }
    }

    suspend fun insertAllHabits(habitImages: List<HabitImageEntity>) {
        withContext(Dispatchers.IO) {
            habitImageDao.cacheAll(habitImages)
        }
    }
}
