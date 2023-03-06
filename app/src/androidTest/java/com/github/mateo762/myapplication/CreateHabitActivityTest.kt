package com.github.mateo762.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.fragments.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.util.*


@RunWith(AndroidJUnit4::class)
class CreateHabitActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(CreateHabitActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        // Initialize the Intents framework
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release the Intents framework
        Intents.release()
    }

    @Test
    fun testCreateHabit() {
        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performTextInput("Drink Water")

        // Check checkboxes for Monday and Wednesday
        composeTestRule.onNodeWithTag("checkbox_MONDAY").performClick()
        composeTestRule.onNodeWithTag("checkbox_WEDNESDAY").performClick()
        composeTestRule.onNodeWithTag("checkbox_FRIDAY").performClick()

        // Enter start time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_start")
            .performTextInput("07:30")

        // Enter end time
        composeTestRule.onNodeWithTag("txt_time_end")
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_end")
            .performTextInput("08:00")

        // Click save button
        composeTestRule.onNodeWithTag("btn_save").performClick()


        // Verify that the intent was sent correctly
        Intents.intended(
            allOf(
                hasComponent(CreateHabitActivity::class.java.name),
                hasExtra("habitName", "Drink Water"),
                hasExtra("habitDays", listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)),
                hasExtra("habitStartTime", "07:30"),
                hasExtra("habitEndTime", "08:00")
            )
        )
    }
}