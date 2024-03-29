package com.github.mateo762.myapplication.room

import com.github.mateo762.myapplication.models.HabitImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitImageRepository @Inject constructor(private val habitImageDao: HabitImageDao) {
    suspend fun getAllHabitImages(): List<HabitImageEntity> {
        return withContext(Dispatchers.IO) {
            habitImageDao.getAll()
        }
    }

    suspend fun insertAllHabitImages(habitImages: List<HabitImageEntity>) {
        withContext(Dispatchers.IO) {
            habitImageDao.cacheAll(habitImages)
        }
    }
}
