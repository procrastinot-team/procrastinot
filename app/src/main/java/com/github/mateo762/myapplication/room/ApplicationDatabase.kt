package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserEntity::class, HabitEntity::class, PostEntity::class], version = 3)
@TypeConverters(HabitTypeConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getPostDao(): PostDao
    abstract fun getHabitDao(): HabitDao

    companion object : SingletonHolder<ApplicationDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext, ApplicationDatabase::class.java,
            "procrastinot.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    })
}