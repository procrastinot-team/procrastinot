package com.github.mateo762.myapplication.di

import com.github.mateo762.myapplication.habits.HabitService
import com.github.mateo762.myapplication.habits.HabitServiceFirebaseImpl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HabitModule {

    @Singleton
    @Provides
    fun bindHabitService() : HabitService {
        val db: DatabaseReference = Firebase.database.reference
        return HabitServiceFirebaseImpl(db)
    }
}