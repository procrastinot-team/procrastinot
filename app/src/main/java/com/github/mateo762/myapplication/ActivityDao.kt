package com.github.mateo762.myapplication

import androidx.room.*

@Dao
interface ActivityDao {
    @Query("SELECT * FROM entityActivity")
    suspend fun getAll(): List<EntityActivity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActivities(entityActivity: EntityActivity): Long

    // @Update
    // suspend fun updateActivity(entityActivity: EntityActivity)

    // @Delete
    // suspend fun delete(entityActivity: EntityActivity)
}