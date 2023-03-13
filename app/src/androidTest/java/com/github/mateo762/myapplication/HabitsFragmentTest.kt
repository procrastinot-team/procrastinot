package com.github.mateo762.myapplication


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalFoundationApi
@RunWith(AndroidJUnit4::class)
class HabitsFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun display_MorningWalk_habit() {
        composeTestRule.onNodeWithTag("Morning Walk")
            .performScrollTo()
            .assertIsDisplayed()
    }
    @Test
    fun display_Reading_habit() {
        composeTestRule.onNodeWithTag("Reading")
            .performScrollTo()
            .assertIsDisplayed()
    }
    @Test
    fun display_Meditation_habit() {
        composeTestRule.onNodeWithTag("Meditation")
            .performScrollTo()
            .assertIsDisplayed()
    }
    @Test
    fun display_Walking_habit() {
        composeTestRule.onNodeWithTag("Walking")
            .performScrollTo()
            .assertIsDisplayed()
    }
    @Test
    fun display_Gym_habit() {
        composeTestRule.onNodeWithTag("Gym")
            .performScrollTo()
            .assertIsDisplayed()
    }
}