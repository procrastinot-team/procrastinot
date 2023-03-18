package com.github.mateo762.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): List<HabitEntity>

    @Insert
    fun insertAll(vararg habits: HabitEntity)

    @Delete
    fun delete(habit: HabitEntity)
}