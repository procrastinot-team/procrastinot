package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.compose.ui.test.hasText
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ProfileActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)

    private lateinit var context: Context

    private lateinit var username: String

    private lateinit var UID: String

    private lateinit var fiveStarProgress: ProgressBar
    private lateinit var fourStarProgress: ProgressBar
    private lateinit var threeStarProgress: ProgressBar
    private lateinit var twoStarProgress: ProgressBar
    private lateinit var oneStarProgress: ProgressBar
    private lateinit var ratingBar: RatingBar

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()

        activityRule.scenario.onActivity {
            it.runOnUiThread {
                fiveStarProgress = it.findViewById(R.id.fiveStarsProgress)
                fourStarProgress = it.findViewById(R.id.fourStarsProgress)
                threeStarProgress = it.findViewById(R.id.threeStarsProgress)
                twoStarProgress = it.findViewById(R.id.twoStarsProgress)
                oneStarProgress = it.findViewById(R.id.oneStarsProgress)
                ratingBar = it.findViewById(R.id.ratingBar)
            }
        }

        context = ApplicationProvider.getApplicationContext()
    }
    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testProfileActivity_isDisplayed() {
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()))
    }

    @Test
    fun onToolbarBackButtonClicked() {
        onView(withContentDescription("Navigate up")).perform(click())
        Thread.sleep(500)
        assertTrue(activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun onProfileGalleryTitleDisplayed() {
        onView(withId(R.id.profileGalleryTitle)).check(matches(withText(context.getString(R.string.profile_progress_gallery_title))))
    }

    @Test
    fun testUserHabitCount() {
        Thread.sleep(500)
        onView(withId(R.id.habit_count)).check(matches(withText(containsString("Posted habits:"))))
    }

    @Test
    fun testUserAvgHabitPerWeek() {
        Thread.sleep(500)
        onView(withId(R.id.avg_per_week)).check(matches(withText(containsString("Avg. Days in Week:"))))
    }


    @Test
    fun testUserEarliestTask() {
        Thread.sleep(500)
        onView(withId(R.id.earliest)).check(matches(withText(containsString("Earliest start:"))))
    }

    @Test
    fun testUserLatestTask() {
        Thread.sleep(500)
        onView(withId(R.id.latest)).check(matches(withText(containsString("Latest end:"))))
    }

    @Test
    fun testUserFollowingCount() {
        Thread.sleep(500)
        onView(withId(R.id.following)).check(matches(withText(containsString("Following:"))))
    }

    @Test
    fun testUserFollowersCount() {
        Thread.sleep(500)
        onView(withId(R.id.followers)).check(matches(withText(containsString("Followers:"))))
    }


    @Test
    fun testRatingDisplayed() = runTest {
        onView(withId(R.id.coachRatingView)).check(matches(isDisplayed()))

        advanceUntilIdle()

        onView(withId(R.id.coachRatingTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingNumber)).check(matches(withText("4.5")))
        onView(withId(R.id.totalRatings)).check(matches(isDisplayed()))
        onView(withId(R.id.totalRatings)).check(matches(withText(context.getString(R.string.total_number_ratings, 2))))
        onView(withId(R.id.fiveStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.fourStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.threeStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.twoStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.oneStarsProgress)).check(matches(isDisplayed()))

        assertEquals(fiveStarProgress.progress, 50)
        assertEquals(fourStarProgress.progress, 50)
        assertEquals(threeStarProgress.progress, 0)
        assertEquals(twoStarProgress.progress, 0)
        assertEquals(oneStarProgress.progress, 0)
        assertEquals(ratingBar.rating, 4.5f)
    }

    // todo Mockito cannot mock this class: class com.google.firebase.auth.FirebaseUser.
//    @Test
//    fun testNumberOfTextViewInProfileActivity() {
//        // Create a mock FirebaseUser
//        val mockFirebaseUser = mock(FirebaseUser::class.java)
//        `when`(mockFirebaseUser.uid).thenReturn("someUserId")
//
//        // Mock the FirebaseAuth instance to return the mock FirebaseUser
//        val mockFirebaseAuth = mock(FirebaseAuth::class.java)
//        `when`(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)
//
//        val habits = listOf(
//            Habit("habit1", "Habit 1", listOf(DayOfWeek.MONDAY),"00:00","01:00"),
//            Habit("habit2", "Habit 2", listOf(DayOfWeek.TUESDAY),"00:00","01:00")
//        )
//
//        // Set up Firebase database and add habits for the user
//        val db = Firebase.database.reference
//        val ref = db.child("users/${mockFirebaseUser.uid}/habitsPath")
//        for (habit in habits) {
//            ref.child(habit.id).setValue(habit)
//        }
//
//
//        // Set up the activity under test
//        val intent = Intent(context, ProfileActivity::class.java).putExtra("USER_ID", mockFirebaseUser.uid)
//
//        activityScenario = ActivityScenario.launch(intent)
//
//        // Clean up the database
//        ref.removeValue()
//    }

    // TODO: fix the test to pass on Cirrus CI
//    @Test
//    fun testEditAndSaveButtons() {
        // First, we switch to edit mode and check if we can edit and if we see the save button
//        onView(withId(R.id.btnEdit)).perform(click()).check(matches(not(isDisplayed())))
//        onView(withId(R.id.btnSave)).check(matches(isDisplayed()))
//        onView(withId(R.id.editTextEmail)).check(matches(isClickable()))
//        onView(withId(R.id.editTextUserName)).check(matches(isClickable()))

        // Then we save and check that the displaying is set back properly
//        onView(withId(R.id.btnSave)).perform(click()).check(matches(not(isDisplayed())))
//        onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
//        onView(withId(R.id.editTextEmail)).check(matches(not(isClickable())))
//        onView(withId(R.id.editTextUserName)).check(matches(not(isClickable())))
//    }
}