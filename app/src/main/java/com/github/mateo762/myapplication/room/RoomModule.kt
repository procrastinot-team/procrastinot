package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "procrastinot.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: ApplicationDatabase): HabitDao {
        return database.getHabitDao()
    }

    @Provides
    @Singleton
    fun provideHabitImageDao(database: ApplicationDatabase): HabitImageDao {
        return database.getHabitImageDao()
    }

    @Provides
    @Singleton
    fun providePostDao(database: ApplicationDatabase): PostDao {
        return database.getPostDao()
    }


}
