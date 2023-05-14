package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.CandidateCard
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RequestsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private var noCoachableHabits = mutableListOf<HabitEntity>()
    private var coachableWithCoachSelected = mutableListOf<HabitEntity>()
    private var coachableWithCoachRequested = mutableListOf<HabitEntity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        setupHabitLists()
    }

    @Test
    fun testNoHabits() {
        // Assert the nothing to see here for now message is displayed
        setUpRequestsScreen(emptyList())
        composeTestRule.onNodeWithTag("nothing_to_see_box").assertExists()
        composeTestRule.onNodeWithTag("nothing_to_see_box").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nothing_to_see_text").assertExists()
        composeTestRule.onNodeWithTag("nothing_to_see_text").assertIsDisplayed()
    }

    @Test
    // No active requests open
    fun testNoCoachableHabits() {
        setUpRequestsScreen(noCoachableHabits)
        composeTestRule.onNodeWithTag("habit_selection_box").assertExists()
        composeTestRule.onNodeWithTag("habit_selection_box").assertIsDisplayed()
        composeTestRule.onNodeWithTag("habit_name").assertExists()
        composeTestRule.onNodeWithTag("habit_name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("habit_name")
            .assertTextContains(noCoachableHabits.first().name)
        composeTestRule.onNodeWithTag("no_candidates_text").assertExists()
        composeTestRule.onNodeWithTag("no_candidates_text").assertIsDisplayed()
    }

    @Test
    fun testCandidateCard() {
        setUpCandidateCard(coach1)
        composeTestRule.onNodeWithTag("candidate_card_name_${coach1.name}").assertExists()
        composeTestRule.onNodeWithTag("candidate_card_name_${coach1.name}").assertIsDisplayed()
        composeTestRule.onNodeWithTag("candidate_card_name_${coach1.name}")
            .assertTextContains(coach1.name!!)
        composeTestRule.onNodeWithTag("candidate_card_username_${coach1.username}").assertExists()
        composeTestRule.onNodeWithTag("candidate_card_username_${coach1.username}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("candidate_card_username_${coach1.username}")
            .assertTextContains(coach1.username!!)
        composeTestRule.onNodeWithTag("candidate_card_email_${coach1.email}").assertExists()
        composeTestRule.onNodeWithTag("candidate_card_email_${coach1.email}").assertIsDisplayed()
        composeTestRule.onNodeWithTag("candidate_card_email_${coach1.email}")
            .assertTextContains(coach1.email!!)
        composeTestRule.onNodeWithTag("candidate_card_rating").assertTextContains(4.45.toString())
    }

    private fun setUpRequestsScreen(habits: List<HabitEntity>) {
        composeTestRule.setContent {
            RequestsScreen(habits)
        }
    }

    private fun setUpCandidateCard(coach: UserEntity) {
        composeTestRule.setContent {
            coach.name?.let {
                coach.username?.let { it1 ->
                    coach.email?.let { it2 ->
                        CandidateCard(name = it, username = it1, rating = 4.45, email = it2) {}
                    }
                }
            }
        }
    }

    private fun setupHabitLists() {
        noCoachableHabits.add(habitWithoutCoachRequested)
        coachableWithCoachSelected.add(habitWithCoachAlreadySelected)
        coachableWithCoachRequested.add(habitWithCoachRequested)
    }

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
    private val habitWithCoachAlreadySelected = HabitEntity(
        "1", "Selected 1",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = true, coachRequested = true,
        listOf(coach1.uid, coach2.uid), coach1.uid
    )
    private val habitWithoutCoachRequested = HabitEntity(
        "2", "Not requested",
        listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        "9:00", "12:00",
        isCoached = false, coachRequested = false,
        emptyList(), ""
    )

}