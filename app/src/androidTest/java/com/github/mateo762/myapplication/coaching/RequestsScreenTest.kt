package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class RequestsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    private val coach1 = UserEntity(
        "9i3402934ojfssmfoiwjeoi293",
        "Test Coach 1",
        "test_coach_1",
        "test_coach1@gmail.com",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )
    private val coach2 = UserEntity(
        "68790239u8yughjkladosou19028",
        "Test Coach 2",
        "test_coach_2",
        "test_coach2@gmail.com",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )
    private val habitWithCoachRequested = HabitEntity(
        "0", "Requested",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = false, coachRequested = true,
        listOf(coach1.uid, coach2.uid), ""
    )

    private val habitWithCoachRequested2 = HabitEntity(
        "1", "Requested 2",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = false, coachRequested = true,
        emptyList(), ""
    )

    private val habitWithCoachSelected = HabitEntity(
        "2", "Already coached",
        listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
        "13:00", "16:00",
        isCoached = true, coachRequested = true,
        listOf(coach1.uid, coach2.uid), coach2.uid
    )

    @Test
    fun testDisplayNothing() {
        composeTestRule.setContent {
            DisplayNothing()
        }

        composeTestRule.onNodeWithTag("nothing_to_see_box")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("nothing_to_see_text")
            .assertTextEquals("Nothing to see here for now...")
    }

    @Test
    fun testDisplayCoachSelection() {
        val habitMap = mapOf(
            habitWithCoachRequested to listOf(
                coach1,
                coach2
            ),
        )
        composeTestRule.setContent {
            DisplayCoachSelection(habitMap) { _, _ -> }
        }

        composeTestRule.onAllNodesWithTag("habit_selection_box")
            .assertCountEquals(1)

        for (habit in habitMap) {
            composeTestRule.onNodeWithTag("habit_name_${habit.key.name}")
                .assertTextEquals(habit.key.name)
            composeTestRule.onNodeWithTag("candidate_card_name_${habit.value[0].name}")
                .assertTextEquals(coach1.name!!)
            composeTestRule.onNodeWithTag("candidate_card_name_${habit.value[1].name}")
                .assertTextEquals(coach2.name!!)
            composeTestRule.onNodeWithTag("candidate_card_username_${habit.value[0].username}")
                .assertTextEquals(coach1.username!!)
            composeTestRule.onNodeWithTag("candidate_card_username_${habit.value[1].username}")
                .assertTextEquals(coach2.username!!)
            composeTestRule.onNodeWithTag("candidate_card_email_${habit.value[0].email}")
                .assertTextEquals(coach1.email!!)
            composeTestRule.onNodeWithTag("candidate_card_email_${habit.value[1].email}")
                .assertTextEquals(coach2.email!!)
        }

        composeTestRule.onAllNodesWithTag("habit_spacer")
            .assertCountEquals(1)

        composeTestRule.onAllNodesWithTag("candidate_card_button")
            .assertCountEquals(2)

        val nodes = composeTestRule.onAllNodesWithTag("candidate_card_button")
            .assertCountEquals(2)

        nodes[0].assertTextEquals("Select")
        nodes[1].assertTextEquals("Select")

        composeTestRule.onAllNodesWithTag("candidate_card_image")
            .assertCountEquals(2)
    }

    @Test
    fun testEmptyCandidateCard() {
        composeTestRule.setContent {
            EmptyCandidateCard()
        }

        composeTestRule.onNodeWithTag("no_candidates_text")
            .assertTextEquals("No candidates applied yet!")
    }

    @Test
    fun testCurrentCoachCard() {
        val coachedMap = mapOf(habitWithCoachSelected to coach2)
        composeTestRule.setContent {
            DisplayCurrentCoach(habitMap = coachedMap)
        }

        // DisplayCurrentCoach
        composeTestRule.onNodeWithTag("current_coach_display_box_for_${habitWithCoachSelected.id}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("current_coach_habit_name${habitWithCoachSelected.name}")
            .assertTextEquals("Already coached")

        // CoachCard
        coach2.name?.let {
            composeTestRule.onNodeWithTag("coach_card_name")
                .assertIsDisplayed()
                .assertTextEquals("Coach: $it")
        }
        coach2.username?.let {
            composeTestRule.onNodeWithTag("coach_card_username")
                .assertIsDisplayed()
                .assertTextEquals("@$it")
        }
        coach2.email?.let {
            composeTestRule.onNodeWithTag("coach_card_email")
                .assertIsDisplayed()
                .assertTextEquals(it)
        }

    }

    @Test
    fun testEmptyMaps() {
        composeTestRule.setContent {
            RequestsScreen(
                coachableHabits = emptyList(),
                coachedHabits = emptyList()
            ) { _: UserEntity, _: HabitEntity -> }
        }
        composeTestRule.onNodeWithTag("nothing_to_see_box")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("nothing_to_see_text")
            .assertTextEquals("Nothing to see here for now...")
    }

    @Test
    fun testNoCandidatesForHabit() {
        composeTestRule.setContent {
            DisplayCoachSelection(mapOf(habitWithCoachRequested2 to emptyList())) { _: UserEntity, _: HabitEntity -> }
        }
        composeTestRule.onNodeWithTag("no_candidates_text")
            .assertTextEquals("No candidates applied yet!")
    }
}