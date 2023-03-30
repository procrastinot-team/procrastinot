package com.github.mateo762.myapplication.room

import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habitEntity")
    fun getAll(): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(habits: List<HabitEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(habit: HabitEntity)

    @Delete
    fun delete(habit: HabitEntity)
}