package com.github.mateo762.myapplication


import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.github.mateo762.myapplication.TestData.emptyHabits
import com.github.mateo762.myapplication.TestData.hardCodedHabits
import com.github.mateo762.myapplication.TestData.hardCodedImages
import com.github.mateo762.myapplication.TestData.noTodayHabits
import com.github.mateo762.myapplication.ui.home.TodayScreen
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TodayFragmentTest {

    private val time: LocalDateTime = LocalDateTime.of(2023, 3, 21, 12, 55)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTodayFragment_todayNextUp_noEmpty() {
        setUpTodayScreen(hardCodedHabits)
        composeTestRule
            .onNodeWithTag("today_completed_Ride bike")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_completed_Drink water")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_pending_Exercise")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_pending_Walk dog")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_Exercise")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_allocated_hours_5")
            .assertExists()
        composeTestRule
            .onAllNodesWithTag("image")
            .assertCountEquals(3)
        composeTestRule
            .onNodeWithTag("today_empty")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("next_up_empty")
            .assertDoesNotExist()
    }


    @Test
    fun testTodayFragment_NoToday() {
        setUpTodayScreen(noTodayHabits)
        composeTestRule.onNodeWithTag("today_empty").assertExists()
        composeTestRule.onNodeWithTag("next_up_Meditate").assertExists()
        composeTestRule.onNodeWithTag("next_up_allocated_hours_18").assertExists()
    }


    @Test
    fun testTodayFragment_NoHabits() {
        setUpTodayScreen(emptyHabits)
        composeTestRule.onNodeWithTag("today_empty").assertExists()
        composeTestRule.onNodeWithTag("next_up_empty").assertExists()
    }

    private fun setUpTodayScreen(habits: List<Habit>) {
        composeTestRule.setContent {
            TodayScreen(time = time, habits = habits, images = hardCodedImages)
        }
    }
}