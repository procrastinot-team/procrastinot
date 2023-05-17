package com.github.mateo762.myapplication.coaching

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.coaching.fragments.RequestsFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RequestsFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val habitsState = mutableStateOf(emptyList<HabitEntity>())

    // A list with each habit with coachRequested and !isCoached, with the coaches offered
    private val coachableHabits =
        mutableStateOf(mutableListOf<Map<HabitEntity, List<UserEntity>>>())

    // A list with each habit with isCoached, with the coach selected
    private val coachedHabits = mutableStateOf(mutableListOf<Map<HabitEntity, UserEntity>>())

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

    @Test
    suspend fun testGetCoachableAndCoachedHabits() {
        // Load test data
        val testHabitsState = listOf(habitWithCoachRequested, habitWithCoachRequested2)
        habitsState.value = testHabitsState
        RequestsFragment().getCoachableAndCoachedHabits()
        assert(
            coachableHabits.value.contains(
                mapOf(
                    habitWithCoachRequested to listOf(coach1, coach2),
                    habitWithCoachRequested to emptyList()
                )
            )
        )
        assert(coachedHabits.value.isEmpty())
    }
}
