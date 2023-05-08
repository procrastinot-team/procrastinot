package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.models.UserEntity

@Database(entities = [UserEntity::class, HabitEntity::class, PostEntity::class, HabitImageEntity::class], version = 9)
@TypeConverters(HabitTypeConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getPostDao(): PostDao
    abstract fun getHabitDao(): HabitDao
    abstract fun getHabitImageDao(): HabitImageDao

    companion object : SingletonHolder<ApplicationDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext, ApplicationDatabase::class.java,
            "procrastinot.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    })
}