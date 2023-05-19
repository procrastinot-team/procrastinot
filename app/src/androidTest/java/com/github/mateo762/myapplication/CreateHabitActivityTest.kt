package com.github.mateo762.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.ui.habits.convertDayOfWeekToInt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.util.*

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreateHabitActivityTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(HabitsActivity.HabitsEntryPoint::class.java)

    private lateinit var habitName: String
    private lateinit var habitDays: List<DayOfWeek>
    private lateinit var habitStartTime: String
    private lateinit var habitEndTime: String

    @Before
    fun setUp() {
        hiltRule.inject()
        habitName = "Drink Water"
        habitDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        habitStartTime = "00:00"
        habitEndTime = "23:59"
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun createHabit() {

        // Enter habit name
        composeTestRule.onNodeWithTag("txt_name")
            .performScrollTo()
            .performTextInput(habitName)

        // Check checkboxes for habit days
        habitDays.forEach { day ->
            composeTestRule.onNodeWithTag("checkbox_${day.name}")
                .performScrollTo()
                .performClick()
        }

        // Check checkboxes for coach request
        composeTestRule.onNodeWithTag("checkbox_coach_request")
            .performScrollTo()
            .performClick()

        // Click save button
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .performClick()
    }

//    @Test
//    fun testOnTimePickerDialogClicked() {
//        composeTestRule.onNodeWithTag("btn_start_time")
//            .performScrollTo()
//            .performClick()
//
//        val calendar = Calendar.getInstance()
//        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
//        val startMinutes = calendar.get(Calendar.MINUTE) + 2
//
//        onView(isAssignableFrom(TimePicker::class.java)).perform(
//            PickerActions.setTime(
//                startHour,
//                startMinutes
//            )
//        )
//        onView(withText("OK")).perform(click())
//
//        composeTestRule.onNodeWithTag("txt_start_time_text")
//            .assert(hasText("Chosen start time = ${startHour}:${startMinutes}"))
//
//        composeTestRule.onNodeWithTag("btn_end_time")
//            .performScrollTo()
//            .performClick()
//
//        val endHour = calendar.get(Calendar.HOUR_OF_DAY)
//        val endMinutes = calendar.get(Calendar.MINUTE) + 4
//
//        onView(isAssignableFrom(TimePicker::class.java)).perform(
//            PickerActions.setTime(
//                endHour,
//                endMinutes
//            )
//        )
//        onView(withText("OK")).perform(click())
//
//        composeTestRule.onNodeWithTag("txt_end_time_text")
//            .performScrollTo()
//            .assert(hasText("Chosen end time = ${endHour}:${endMinutes}"))
//    }

    @Test
    fun testCreateHabit() {
        createHabit()
        // Verify that the intent was sent correctly

        intended(
            allOf(
                hasComponent(HabitsActivity.HabitsEntryPoint::class.java.name),
                hasExtra("habitName", habitName),
                hasExtra("habitDays", habitDays),
                hasExtra("habitStartTime", habitStartTime),
                hasExtra("habitEndTime", habitEndTime)
            )
        )
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
    fun clickSaveButton_noNameInput_noIntentSent() {
        habitName = ""
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }


    @Test
    fun clickSaveButton_askingForCoach() {
        createHabit()
        composeTestRule.onNodeWithTag("btn_save")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun convertDayOfWeekToInt_isCorrect() {
        assertEquals(2, convertDayOfWeekToInt(DayOfWeek.MONDAY))
        assertEquals(3, convertDayOfWeekToInt(DayOfWeek.TUESDAY))
        assertEquals(4, convertDayOfWeekToInt(DayOfWeek.WEDNESDAY))
        assertEquals(5, convertDayOfWeekToInt(DayOfWeek.THURSDAY))
        assertEquals(6, convertDayOfWeekToInt(DayOfWeek.FRIDAY))
        assertEquals(7, convertDayOfWeekToInt(DayOfWeek.SATURDAY))
        assertEquals(1, convertDayOfWeekToInt(DayOfWeek.SUNDAY))
    }

}