package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.coaching.fragments.RequestsFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RequestsFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val habitEntity = HabitEntity(id = "1", coachRequested = true)
        val habitSnapshot = mock(DataSnapshot::class.java)
        `when`(habitSnapshot.getValue(HabitEntity::class.java)).thenReturn(habitEntity)

        val childSnapshot = mock(DataSnapshot::class.java)
        `when`(childSnapshot.getValue(HabitEntity::class.java)).thenReturn(habitEntity)

        composeTestRule.setContent {
            RequestsScreen(emptyList(), emptyList()) { _, _ -> }
        }
    }


    @Test
    fun testUpdateCoachStateCallbackUpdatesSnapshot() {
        val coach1 = UserEntity(
            "9i3402934ojfssmfoiwjeoi293",
            "Test Coach 1",
            "test_coach_1",
            "test_coach1@gmail.com",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val coach2 = UserEntity(
            "68790239u8yughjkladosou19028",
            "Test Coach 2",
            "test_coach_2",
            "test_coach2@gmail.com",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val habitWithCoachRequested = HabitEntity(
            "0", "Requested",
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
            "9:00", "12:00",
            isCoached = false, coachRequested = true,
            listOf(coach1.uid, coach2.uid), ""
        )
        val mockDataSnapshot = mock(DataSnapshot::class.java)
        `when`(mockDataSnapshot.getValue(HabitEntity::class.java)).thenReturn(
            habitWithCoachRequested
        )
        val mockDatabaseReference = mock(DatabaseReference::class.java)
        `when`(mockDataSnapshot.ref).thenReturn(mockDatabaseReference)

        val requestsFragment = RequestsFragment()
        requestsFragment.updateCoachStateCallback(
            mockDataSnapshot,
            habitWithCoachRequested,
            coach1
        )

        // Verify data update
        verify(mockDataSnapshot).ref
        verify(mockDatabaseReference).updateChildren(
            mapOf(
                "isCoached" to true,
                "coach" to coach1.uid
            )
        )
    }

}
