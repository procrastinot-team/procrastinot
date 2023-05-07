package com.github.mateo762.myapplication.coach_rating

import kotlinx.coroutines.flow.Flow

/**
 * Interface for the rating service that retrieve the rating of the coaches.
 */
interface RatingService {

    /**
     * Method that get all of the ratings of a coach.
     *
     * @param uid the uid of the coach.
     */
    fun getRatings(uid: String): Flow<List<Int>>
}