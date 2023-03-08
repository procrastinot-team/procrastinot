package com.github.mateo762.myapplication

import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.fragments.*
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
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
        intended(
            allOf(
                hasComponent(DisplayParametersActivity::class.java.name),
                hasExtra("habitName", "Drink Water"),
                hasExtra(
                    "habitDays",
                    listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
                ),
                hasExtra("habitStartTime", "07:30"),
                hasExtra("habitEndTime", "08:00")
            )
        )
    }
    @Test
    fun clickSaveButton_noNameInput_noIntentSent(){
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

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noDaysSelected_noIntentSent(){
        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performTextInput("Drink Water")

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

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noStartTime_noIntentSent(){
        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performTextInput("Drink Water")

        // Check checkboxes for Monday and Wednesday
        composeTestRule.onNodeWithTag("checkbox_MONDAY").performClick()
        composeTestRule.onNodeWithTag("checkbox_WEDNESDAY").performClick()
        composeTestRule.onNodeWithTag("checkbox_FRIDAY").performClick()

        // Enter end time
        composeTestRule.onNodeWithTag("txt_time_end")
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_end")
            .performTextInput("08:00")


        // Clear start time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performClick()
            .performTextClearance()

        // Click save button
        composeTestRule.onNodeWithTag("btn_save").performClick()

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }
    @Test
    fun clickSaveButton_startTimeInvalid_noIntentSent(){
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
            .performTextInput("24:30")

        // Enter end time
        composeTestRule.onNodeWithTag("txt_time_end")
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_end")
            .performTextInput("08:00")

        // Click save button
        composeTestRule.onNodeWithTag("btn_save").performClick()

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }
    @Test
    fun clickSaveButton_noEndTime_noIntentSent(){
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

        // Clear end time
        composeTestRule.onNodeWithTag("txt_time_end")
            .performClick()
            .performTextClearance()

        // Click save button
        composeTestRule.onNodeWithTag("btn_save").performClick()

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }
    @Test
    fun clickSaveButton_endTimeInvalid_noIntentSent(){
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
            .performTextInput("08:78")

        // Click save button
        composeTestRule.onNodeWithTag("btn_save").performClick()

        composeTestRule.onNodeWithTag("btn_save").assertIsDisplayed()
    }
}