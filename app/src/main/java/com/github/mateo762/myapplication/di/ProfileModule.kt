package com.github.mateo762.myapplication.di

import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.followers.UserRepositoryImpl
import com.github.mateo762.myapplication.profile.ProfileService
import com.github.mateo762.myapplication.profile.ProfileServiceFirebaseImpl
import com.github.mateo762.myapplication.profile.UserImageStorageService
import com.github.mateo762.myapplication.profile.UserImageStorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Singleton
    @Binds
    abstract fun bindProfileService(
        profileServiceFirebaseImpl: ProfileServiceFirebaseImpl
    ): ProfileService

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindUserImageStorageService(
        userImageStorageServiceImpl: UserImageStorageServiceImpl
    ): UserImageStorageService
}