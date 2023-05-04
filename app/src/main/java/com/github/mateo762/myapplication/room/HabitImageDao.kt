package com.github.mateo762.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mateo762.myapplication.models.HabitImageEntity

@Dao
interface HabitImageDao {
    @Query("SELECT * FROM habitImageEntity")
    fun getAll(): List<HabitImageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun cacheAll(habits: List<HabitImageEntity>)
}