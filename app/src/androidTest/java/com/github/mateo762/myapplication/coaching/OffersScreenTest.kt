package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class OffersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val user1 = UserEntity(
        "9i3402934ojfssmfoiwjeoi293",
        "Test Coach 1",
        "test_coach_1",
        "test_coach1@gmail.com",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )
    private val user2 = UserEntity(
        "68790239u8yughjkladosou19028",
        "Test Coach 2",
        "test_coach_2",
        "test_coach2@gmail.com",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )

    private val coachableHabit1 = HabitEntity(
        "0", "Requested",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = false, coachRequested = true,
        listOf(user1.uid, user2.uid), "", habitOwnerName = user1.name!!
    )

    private val coachableHabit2 = HabitEntity(
        "1", "Requested 2",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = false, coachRequested = true,
        emptyList(), "", habitOwnerName = user2.name!!
    )

    private val habitWithCoachSelected = HabitEntity(
        "2", "Already coached",
        listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
        "13:00", "16:00",
        isCoached = true, coachRequested = true,
        listOf(user1.uid, user2.uid), user2.uid
    )

    @Test
    fun testDisplayNoOffers() {
        composeTestRule.setContent {
            DisplayNoOffers()
        }

        composeTestRule.onNodeWithTag("nothing_to_see_box")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("nothing_to_see_text")
            .assertTextEquals("There are no requests for coaching")
    }

    @Test
    fun testDisplayCoachOffers() {
        val coachableHabits = listOf(coachableHabit1, coachableHabit2)

        //Let the current user be user1
        composeTestRule.setContent {
            for (habit in coachableHabits) {
                DisplayCoachingOffer(habit = habit) {
                    //Do nothing
                }
            }
        }

        composeTestRule.onAllNodesWithTag("coaching_habits_box")
            .assertCountEquals(coachableHabits.size)

        for (habit in coachableHabits){
            //Check that the coachable habits are displayed
            composeTestRule.onNodeWithTag("coachable_habit_${habit.name}")
                .assertIsDisplayed()

            //Check that the coachable habits have the correct text
            composeTestRule.onNodeWithTag("coachable_habit_${habit.name}")
                .assertTextEquals(habit.name)

            composeTestRule.onNodeWithTag("candidate_card_name_${habit.habitOwnerName}")
                .assertTextEquals(habit.habitOwnerName)
        }

        //Check that the buttons are displayed
        composeTestRule.onAllNodesWithTag("habit_button")
            .assertCountEquals(2)

        //Check that the buttons have the correct text
        val buttons = composeTestRule.onAllNodesWithTag("habit_button")
            .assertCountEquals(2)

        buttons[0].assertTextEquals("Apply")
        buttons[1].assertTextEquals("Apply")
    }

    @Test
    fun testDisplayAppliedCoachOffer() {
        val coachableHabits = listOf(coachableHabit1, coachableHabit2)

        //Let the current user be user1
        composeTestRule.setContent {
            for (habit in coachableHabits) {
                DisplayAppliedCoachOffer(habit = habit)
            }
        }

        composeTestRule.onAllNodesWithTag("coaching_habits_box_gray")
            .assertCountEquals(coachableHabits.size)

        for (habit in coachableHabits){
            //Check that the coachable habits are displayed
            composeTestRule.onNodeWithTag("coachable_habit_gray_${habit.name}")
                .assertIsDisplayed()

            //Check that the coachable habits have the correct text
            composeTestRule.onNodeWithTag("coachable_habit_gray_${habit.name}")
                .assertTextEquals(habit.name)

            composeTestRule.onNodeWithTag("candidate_card_name_${habit.habitOwnerName}")
                .assertTextEquals(habit.habitOwnerName)
        }

        //Check that the buttons are displayed
        composeTestRule.onAllNodesWithTag("habit_button")
            .assertCountEquals(2)

        //Check that the buttons have the correct text
        val buttons = composeTestRule.onAllNodesWithTag("habit_button")
            .assertCountEquals(2)

        buttons[0].assertTextEquals("Applied")
        buttons[1].assertTextEquals("Applied")
    }

    @Test
    fun testCoacheeCard(){
        val coachableHabits = listOf(coachableHabit1, coachableHabit2)

        composeTestRule.setContent {
            for (habit in coachableHabits) {
                CoacheeCard(habit = habit)
            }
        }

        for (habit in coachableHabits){

            composeTestRule.onNodeWithTag("candidate_card_name_${habit.habitOwnerName}")
                .assertTextEquals(habit.habitOwnerName)
        }
    }
}