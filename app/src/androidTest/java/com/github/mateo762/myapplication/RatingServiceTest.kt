package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.coach_rating.RatingService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RatingServiceTest {

    private lateinit var ratingService: RatingService

    @Before
    fun setUp() {
        ratingService = object : RatingService {
            override fun getRatings(uid: String) = flowOf(listOf(4, 5, 3, 4, 5))
        }
    }

    @Test
    fun testGetRatings() = runBlockingTest {
        // Define the coach's UID
        val uid = "coach123"

        // Call the getRatings method
        val ratings = mutableListOf<Int>()
        ratingService.getRatings(uid).collect { rating ->
            ratings.addAll(rating)
        }

        // Check the ratings list
        assertEquals(listOf(4, 5, 3, 4, 5), ratings)
    }
}