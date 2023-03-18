package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, HabitEntity::class, PostEntity::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun habitDao(): HabitDao

    companion object : SingletonHolder<ApplicationDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, ApplicationDatabase::class.java,
            "procrastinot.db").build()
    })
}