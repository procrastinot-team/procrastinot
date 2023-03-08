package com.github.mateo762.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
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

    private lateinit var habitName: String
    private lateinit var habitDays: List<DayOfWeek>
    private lateinit var habitStartTime: String
    private lateinit var habitEndTime: String

    @Before
    fun setUp() {
        habitName = "Drink Water"
        habitDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        habitStartTime = "07:30"
        habitEndTime = "08:00"
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun createHabit() {
        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performTextInput(habitName)

        // Check checkboxes for habit days
        habitDays.forEach { day ->
            composeTestRule.onNodeWithTag("checkbox_${day.name}")
                .performClick()
        }
        // Clear start and end time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performClick()
            .performTextClearance()
        composeTestRule.onNodeWithTag("txt_time_end")
            .performClick()
            .performTextClearance()

        // Enter start and end time
        composeTestRule.onNodeWithTag("txt_time_start")
            .performTextInput(habitStartTime)
        composeTestRule.onNodeWithTag("txt_time_end")
            .performTextInput(habitEndTime)

        // Click save button
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .performClick()
    }

    @Test
    fun testCreateHabit() {
        createHabit()
        // Verify that the intent was sent correctly
        intended(
            allOf(
                hasComponent(DisplayParametersActivity::class.java.name),
                hasExtra("habitName", habitName),
                hasExtra("habitDays", habitDays),
                hasExtra("habitStartTime", habitStartTime),
                hasExtra("habitEndTime", habitEndTime)
            )
        )
    }

    @Test
    fun clickSaveButton_noNameInput_noIntentSent() {
        habitName = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noDaysSelected_noIntentSent() {
        habitDays = listOf()
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noStartTime_noIntentSent() {
        habitStartTime = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_startTimeInvalid_noIntentSent() {
        habitStartTime = "25:00"
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_noEndTime_noIntentSent() {
        habitEndTime = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickSaveButton_endTimeInvalid_noIntentSent() {
        habitEndTime = "08:69"
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun clickGoBackButton_IntentToMainActivity() {
        composeTestRule.onNodeWithTag("btn_cancel")
            .performScrollTo()
            .performClick()
        intended(hasComponent(MainActivity::class.java.name))
    }
}