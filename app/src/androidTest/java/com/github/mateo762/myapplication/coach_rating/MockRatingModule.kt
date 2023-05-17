package com.github.mateo762.myapplication.coach_rating

import com.github.mateo762.myapplication.di.RatingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RatingModule::class]
)
class MockRatingModule {

    private val ratingService = MockRatingService()

    @Singleton
    @Provides
    fun provideURatingService(): RatingService {
        return ratingService
    }

    class MockRatingService: RatingService {
        override fun getRatings(uid: String): Flow<List<Int>> {
            return flow {
                val array = ArrayList<Int>()
                array.add(5)
                array.add(4)
                emit(array)
            }
        }
    }

    class MockRatingServiceWithException: RatingService {
        override fun getRatings(uid: String): Flow<List<Int>> {
            throw RuntimeException()
        }
    }

    class MockRatingServiceEmptyArray: RatingService {
        override fun getRatings(uid: String): Flow<List<Int>> {
            return flow {
                emit(ArrayList())
            }
        }
    }
}