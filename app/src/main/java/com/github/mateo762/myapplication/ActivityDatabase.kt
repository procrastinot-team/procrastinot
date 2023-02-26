package com.github.mateo762.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EntityActivity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}