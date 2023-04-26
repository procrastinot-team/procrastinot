package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)

    private lateinit var activityScenario: ActivityScenario<ProfileActivity>

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
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
    fun testEditButton_click() {
        onView(withId(R.id.btnEdit)).perform(click())
        onView(withId(R.id.btnSave)).check(matches(isDisplayed()))
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
    fun testEmailAndNameEditTexts_notEditableInitially() {
        onView(withId(R.id.editTextUserName)).check(matches(not(isEnabled())))
        onView(withId(R.id.editTextEmail)).check(matches(not(isEnabled())))
    }

    @Test
    fun testEmailAndNameEditTexts_editableAfterClickingEditButton() {
        onView(withId(R.id.btnEdit)).perform(click())

        onView(withId(R.id.editTextUserName)).check(matches(isEnabled()))
        onView(withId(R.id.editTextEmail)).check(matches(isEnabled()))
    }

    @Test
    fun testEmailAndNameEditTexts_notEditableAfterClickingSaveButton() {
        onView(withId(R.id.btnEdit)).perform(click())
        onView(withId(R.id.btnSave)).perform(click())

        onView(withId(R.id.editTextUserName)).check(matches(not(isEnabled())))
        onView(withId(R.id.editTextEmail)).check(matches(not(isEnabled())))
    }

    @Test
    fun testFollowButton_initiallyVisibleAndUnfollowButtonHidden() {
        startProfileActivityWithDifferentUserId("differentUserId")
        onView(withId(R.id.btnFollow)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.btnUnfollow)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testFollowButton_visibleAndUnfollowButtonHidden_afterClickingUnfollowButton() {
        startProfileActivityWithDifferentUserId("differentUserId")
        onView(withId(R.id.btnFollow)).perform(click()) // make sure btnUnfollow is visible
        onView(withId(R.id.btnUnfollow)).perform(click())

        onView(withId(R.id.btnFollow)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.btnUnfollow)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    private fun startProfileActivityWithDifferentUserId(userId: String) {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity::class.java).apply {
            putExtra("userId", userId)
        }
        activityScenario = ActivityScenario.launch(intent)
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