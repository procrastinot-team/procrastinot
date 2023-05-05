package com.github.mateo762.myapplication

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.ui.home.HabitListScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SummaryFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var activityScenario: ActivityScenario<HomeActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(HomeActivity::class.java)
    }

    val habits = listOf(
        HabitEntity("Habit 1", "Habit 1", listOf(DayOfWeek.TUESDAY), "08:00", "09:00"),
        HabitEntity("Habit 2", "Habit 2", listOf(DayOfWeek.THURSDAY, DayOfWeek.SUNDAY), "10:00", "11:00"),
        HabitEntity("Habit 3", "Habit 3", listOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "12:00", "13:00"),
    )

//    @Composable
//    fun myHabits(onClick: () -> Unit) {
//        HabitListScreen(habits = habits)
//    }

    @Test
    fun testSummaryFragment() {
        Espresso.onView(withId(R.id.summaryFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a SummaryFragment
        TestCase.assertTrue(fragment is SummaryFragment)
//
//        composeTestRule.onNodeWithTag("Habit 1").assertExists()
//        composeTestRule.onNodeWithTag("Habit 2").assertExists()
//        composeTestRule.onNodeWithTag("Habit 3").assertExists()
//
//        composeTestRule.onNodeWithTag("Habit 1").performClick()
//        composeTestRule.onNodeWithTag("Habit 2").performClick()
//        composeTestRule.onNodeWithTag("Habit 3").performClick()
//
//        habits.forEach { habit ->
//            composeTestRule.onNodeWithText(habit.name).assertExists()
//            composeTestRule.onNodeWithText(habit.days.joinToString()).assertExists()
//            composeTestRule.onNodeWithText("Start time: ${habit.startTime}").assertExists()
//            composeTestRule.onNodeWithText("End time: ${habit.endTime}").assertExists()
//        }
    }

    private fun getCurrentFragment(): Fragment? {
        // Get the current fragment from the ActivityScenario
        var fragment: Fragment? = null
        activityScenario.onActivity { activity ->
            // Get the current fragment by its container ID
            fragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragment)
        }
        return fragment
    }
}