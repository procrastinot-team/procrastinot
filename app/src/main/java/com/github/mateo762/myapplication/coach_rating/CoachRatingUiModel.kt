package com.github.mateo762.myapplication.coach_rating

/**
 * Data class for the UiModel.
 */
data class CoachRatingUiModel(
    val totalNumberRatings: Int,
    val rating: Float,
    val fiveStarProgress: Int,
    val fourStarProgress: Int,
    val threeStarProgress: Int,
    val twoStarProgress: Int,
    val oneStarProgress: Int
)
