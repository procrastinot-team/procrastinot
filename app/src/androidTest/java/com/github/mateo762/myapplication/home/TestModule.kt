package com.github.mateo762.myapplication.home

import android.app.Application
import android.net.ConnectivityManager
import com.github.mateo762.myapplication.room.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Singleton
    @Provides
    fun provideHabitRepository(): HabitRepository {
        // Provide a mocked instance of HabitRepository for testing
        return mockk()
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        // Provide a mocked instance of ConnectivityManager for testing
        return mockk()
    }

    // Provide other mocked dependencies as needed

}