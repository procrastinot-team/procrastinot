package com.github.mateo762.myapplication.coaching

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.coaching.fragments.OffersFragment
import com.github.mateo762.myapplication.coaching.fragments.RequestsFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.profile.ProfileServiceFirebaseImpl
import com.github.mateo762.myapplication.ui.coaching.OffersScreen
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.DayOfWeek
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OffersFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var db: DatabaseReference

    private var userId: String = UUID.randomUUID().toString()


    @Before
    fun setup() {
        hiltRule.inject()

        MockitoAnnotations.openMocks(this)
        composeTestRule.setContent {
            OffersScreen(emptyList(), "") { _ -> }
        }
    }

    @Test
    fun testGetFirebaseHabits() = runTest {
        //TODO
        assert(true)
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

        db.child("habits/").push().setValue(habitWithCoachRequested).await()
        db.child("users/${userId}/").push().setValue(habitWithCoachRequested).await()



        val profileService = ProfileServiceFirebaseImpl(db)
        var array: ArrayList<HabitImageEntity>? = null

        //when
        profileService.getHabitsImages(userId).collect {
            array = it
        }

        //then
        checkNotNull(array)
        Assert.assertEquals(array?.size, 1)

    }

    @Test
    fun testGetCurrentUser(){
        val offersFragment = OffersFragment()
        val currentUser = offersFragment.getCurrentUser()

        //Assert that the currentUser is a UserEntity(uid="", name="")
        println("The current user is: $currentUser")

        assert(currentUser.username == null)
        assert(currentUser.email == null)
    }
//
//
//    @Test
//    fun testUpdateCoachStateCallbackUpdatesSnapshot() {
//        val coach1 = UserEntity(
//            "9i3402934ojfssmfoiwjeoi293",
//            "Test Coach 1",
//            "test_coach_1",
//            "test_coach1@gmail.com",
//            emptyList(),
//            emptyList(),
//            emptyList(),
//            emptyList()
//        )
//        val coach2 = UserEntity(
//            "68790239u8yughjkladosou19028",
//            "Test Coach 2",
//            "test_coach_2",
//            "test_coach2@gmail.com",
//            emptyList(),
//            emptyList(),
//            emptyList(),
//            emptyList()
//        )
//        val habitWithCoachRequested = HabitEntity(
//            "0", "Requested",
//            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
//            "9:00", "12:00",
//            isCoached = false, coachRequested = true,
//            listOf(coach1.uid, coach2.uid), ""
//        )
//        val mockDataSnapshot = mock(DataSnapshot::class.java)
//        `when`(mockDataSnapshot.getValue(HabitEntity::class.java)).thenReturn(
//            habitWithCoachRequested
//        )
//        val mockDatabaseReference = mock(DatabaseReference::class.java)
//        `when`(mockDataSnapshot.ref).thenReturn(mockDatabaseReference)
//
//        val requestsFragment = RequestsFragment()
//        requestsFragment.updateCoachStateCallback(
//            mockDataSnapshot,
//            habitWithCoachRequested,
//            coach1
//        )
//
//        // Verify data update
//        verify(mockDataSnapshot).ref
//        verify(mockDatabaseReference).updateChildren(
//            mapOf(
//                "isCoached" to true,
//                "coach" to coach1.uid
//            )
//        )
//    }


}
