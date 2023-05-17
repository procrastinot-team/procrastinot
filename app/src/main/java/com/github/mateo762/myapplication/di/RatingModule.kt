package com.github.mateo762.myapplication.di

import com.github.mateo762.myapplication.coach_rating.RatingService
import com.github.mateo762.myapplication.coach_rating.RatingServiceFirebaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RatingModule {

    @Singleton
    @Binds
    abstract fun bindRatingService(
        ratingServiceFirebaseImpl: RatingServiceFirebaseImpl
    ): RatingService
}