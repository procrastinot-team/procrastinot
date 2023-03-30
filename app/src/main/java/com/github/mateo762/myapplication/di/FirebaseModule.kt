package com.github.mateo762.myapplication.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseInstance(): FirebaseDatabase{
        return Firebase.database
    }

    @Provides
    @Singleton
    fun provideFirebaseInstanceEmulated(): FirebaseDatabase{
        val db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
        return db
    }

}