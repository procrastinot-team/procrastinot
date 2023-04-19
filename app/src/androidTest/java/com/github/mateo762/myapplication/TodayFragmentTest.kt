package com.github.mateo762.myapplication


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.mateo762.myapplication.TestData.emptyHabits
import com.github.mateo762.myapplication.TestData.hardCodedHabits
import com.github.mateo762.myapplication.TestData.hardCodedImages
import com.github.mateo762.myapplication.TestData.noTodayHabits
import com.github.mateo762.myapplication.models.Habit
import com.github.mateo762.myapplication.ui.home.TodayScreen
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TodayFragmentTest {

    private lateinit var time: LocalDateTime

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testTodayFragment_todayNextUp_noEmpty_1() {
        time = LocalDateTime.of(2023, 3, 21, 12, 55)
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
            .onNodeWithTag("image_Exercise_4_15")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Exercise_4_14")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Exercise_2_16")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Exercise_1_16")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("today_empty")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("next_up_empty")
            .assertDoesNotExist()
    }

    @Test
    fun testTodayFragment_todayNextUp_noEmpty_2() {
        time = LocalDateTime.of(2022, 4, 21, 21, 30)
        setUpTodayScreen(hardCodedHabits)
        composeTestRule
            .onNodeWithTag("today_completed_Ride bike")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_completed_Drink water")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_completed_Exercise")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_completed_Walk dog")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_Meditate")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_allocated_hours_9")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_4_15")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_4_14")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_2_16")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_1_16")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("today_empty")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("next_up_empty")
            .assertDoesNotExist()
    }

    @Test
    fun testTodayFragment_todayNextUp_noEmpty_3() {
        time = LocalDateTime.of(2022, 4, 20, 5, 0)
        setUpTodayScreen(hardCodedHabits)
        composeTestRule
            .onNodeWithTag("today_pending_Meditate")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_pending_Read")
            .assertExists()
        composeTestRule
            .onNodeWithTag("today_pending_Drink water")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_Meditate")
            .assertExists()
        composeTestRule
            .onNodeWithTag("next_up_allocated_hours_2")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_4_15")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_4_14")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_2_16")
            .assertExists()
        composeTestRule
            .onNodeWithTag("image_Meditate_1_16")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("today_empty")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("next_up_empty")
            .assertDoesNotExist()
    }



    @Test
    fun testTodayFragment_NoToday() {
        time = LocalDateTime.of(2023, 3, 21, 12, 55)
        setUpTodayScreen(noTodayHabits)
        composeTestRule.onNodeWithTag("today_empty").assertExists()
        composeTestRule.onNodeWithTag("next_up_Meditate").assertExists()
        composeTestRule.onNodeWithTag("next_up_allocated_hours_18").assertExists()
    }


    @Test
    fun testTodayFragment_NoHabits() {
        time = LocalDateTime.of(2023, 3, 21, 12, 55)
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