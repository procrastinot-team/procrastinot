package com.github.mateo762.myapplication.profile

data class ProfileStatsUiModel(
    val totalNumberOfHabits: Int,
    val averageDaysInWeek: Int,
    val earliestStart: String,
    val latestEnd: String
)