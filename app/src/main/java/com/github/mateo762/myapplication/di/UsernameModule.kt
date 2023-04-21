package com.github.mateo762.myapplication.di

import com.github.mateo762.myapplication.username.UsernameService
import com.github.mateo762.myapplication.username.UsernameServiceFirebaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsernameModule {

    @Singleton
    @Binds
    abstract fun bindUsernameService(
        usernameServiceFirebaseImpl: UsernameServiceFirebaseImpl
    ): UsernameService
}